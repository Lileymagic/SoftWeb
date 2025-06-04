    package com.example.softengineerwebpr.domain.file.service.impl;

    import com.example.softengineerwebpr.common.exception.BusinessLogicException;
    import com.example.softengineerwebpr.common.exception.ErrorCode; //
    import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
    import com.example.softengineerwebpr.domain.file.entity.File; //
    import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
    import com.example.softengineerwebpr.domain.file.repository.FileRepository; //
    import com.example.softengineerwebpr.domain.file.service.FileService;
    import com.example.softengineerwebpr.domain.user.entity.User; //
    import jakarta.annotation.PostConstruct;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.util.StringUtils;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.net.MalformedURLException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;
    import java.util.List;
    import java.util.UUID;
    import java.util.stream.Collectors;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    @Transactional
    public class FileServiceImpl implements FileService {

        private final FileRepository fileRepository;

        @Value("${file.upload-dir}")
        private String uploadDir;
        private Path fileStorageLocation;

        @PostConstruct
        public void init() {
            this.fileStorageLocation = Paths.get(this.uploadDir).toAbsolutePath().normalize();
            try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (Exception ex) {
                log.error("Could not create the directory where the uploaded files will be stored.", ex);
                throw new BusinessLogicException(ErrorCode.INTERNAL_SERVER_ERROR, "파일 저장 디렉토리를 생성할 수 없습니다.");
            }
        }

        @Override
        public FileResponseDto storeFile(MultipartFile multipartFile, FileReferenceType referenceType, Long referenceIdx, User uploadedBy) {
            if (multipartFile.isEmpty()) {
                throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "업로드할 파일이 비어있습니다.");
            }
            if (multipartFile.getSize() > 10 * 1024 * 1024) { // 10MB
                throw new BusinessLogicException(ErrorCode.FILE_SIZE_EXCEEDED);
            }

            String originalFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String extension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                extension = originalFileName.substring(i);
            }
            String storedFileName = UUID.randomUUID().toString() + extension;

            try {
                if (originalFileName.contains("..")) {
                    throw new BusinessLogicException(ErrorCode.INVALID_INPUT_VALUE, "파일명에 부적절한 문자가 포함되어 있습니다: " + originalFileName);
                }

                Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
                Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                File fileMetadata = File.builder()
                        .referenceType(referenceType)
                        .referenceIdx(referenceIdx)
                        .fileName(originalFileName)
                        .filePath(storedFileName)
                        .fileSize((int) multipartFile.getSize())
                        .uploadedBy(uploadedBy)
                        .build(); //

                File savedFile = fileRepository.save(fileMetadata);
                log.info("파일 저장 성공: originalName={}, storedName={}, referenceType={}, referenceIdx={}, uploader={}",
                        originalFileName, storedFileName, referenceType, referenceIdx, uploadedBy.getNickname());
                return FileResponseDto.fromEntity(savedFile); //

            } catch (IOException ex) {
                log.error("Could not store file {}. Please try again!", originalFileName, ex);
                throw new BusinessLogicException(ErrorCode.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다: " + originalFileName);
            }
        }

        @Override
        public List<FileResponseDto> storeFiles(List<MultipartFile> files, FileReferenceType referenceType, Long referenceIdx, User uploadedBy) {
            return files.stream()
                    .map(file -> storeFile(file, referenceType, referenceIdx, uploadedBy))
                    .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<FileResponseDto> getFilesForReference(FileReferenceType referenceType, Long referenceIdx) {
            List<File> files = fileRepository.findByReferenceTypeAndReferenceIdxOrderByUploadedAtAsc(referenceType, referenceIdx); //
            return files.stream()
                    .map(FileResponseDto::fromEntity) //
                    .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public Resource loadFileAsResource(Long fileId, User currentUser) {
            File fileMetadata = fileRepository.findById(fileId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "파일을 찾을 수 없습니다. ID: " + fileId));

            // TODO: currentUser가 이 파일에 접근할 권한이 있는지 확인하는 로직 추가
            // (예: 파일이 속한 게시글/댓글/업무의 프로젝트 멤버인지 또는 작성자 본인인지 등)

            try {
                Path filePath = this.fileStorageLocation.resolve(fileMetadata.getFilePath()).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() && resource.isReadable()) {
                    return resource;
                } else {
                    log.error("Could not read file: {} (ID: {})", fileMetadata.getFilePath(), fileId);
                    throw new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "파일을 읽을 수 없습니다: " + fileMetadata.getFileName());
                }
            } catch (MalformedURLException ex) {
                log.error("File path is invalid for file: {} (ID: {})", fileMetadata.getFilePath(), fileId, ex);
                throw new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "파일 경로가 유효하지 않습니다: " + fileMetadata.getFileName());
            }
        }

        @Override
        @Transactional(readOnly = true)
        public String getOriginalFileName(Long fileId) {
            File fileMetadata = fileRepository.findById(fileId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "파일 메타데이터를 찾을 수 없습니다. ID: " + fileId));
            return fileMetadata.getFileName();
        }

        @Override
        public void deleteFile(Long fileId, User currentUser) {
            File fileMetadata = fileRepository.findById(fileId)
                    .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "삭제할 파일을 찾을 수 없습니다. ID: " + fileId));

            if (!fileMetadata.getUploadedBy().getIdx().equals(currentUser.getIdx())) {
                // TODO: 프로젝트 관리자 등 추가적인 삭제 권한 확인 로직
                throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "파일을 삭제할 권한이 없습니다.");
            }

            try {
                Path filePath = this.fileStorageLocation.resolve(fileMetadata.getFilePath()).normalize();
                Files.deleteIfExists(filePath);
                fileRepository.delete(fileMetadata);
                log.info("파일 삭제 성공: fileName={}, storedPath={}, deleter={}",
                        fileMetadata.getFileName(), fileMetadata.getFilePath(), currentUser.getNickname());
            } catch (IOException ex) {
                log.error("Could not delete file {}. Please try again!", fileMetadata.getFileName(), ex);
                throw new BusinessLogicException(ErrorCode.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다: " + fileMetadata.getFileName());
            }
        }

        /**
         * 특정 참조 대상(게시글, 댓글, 업무)에 연결된 모든 파일을 삭제합니다. (새로 추가된 메소드 구현)
         */
        @Override
        public void deleteFilesForReference(FileReferenceType referenceType, Long referenceIdx, User currentUser) {
            List<File> filesToDelete = fileRepository.findByReferenceTypeAndReferenceIdxOrderByUploadedAtAsc(referenceType, referenceIdx); //
            if (filesToDelete.isEmpty()) {
                return; // 삭제할 파일 없음
            }

            // 일괄 삭제 권한 확인: 이 참조 대상(게시글, 댓글, 업무) 자체를 삭제할 수 있는 사용자인지 확인하는 것이 적절.
            // 예를 들어, 게시글 작성자이거나 프로젝트 관리자일 경우 등.
            // 여기서는 단순화를 위해 첫 번째 파일의 업로더를 기준으로 권한을 확인하거나,
            // 또는 이 메소드를 호출하는 상위 서비스(PostService 등)에서 이미 권한 검증이 완료되었다고 가정합니다.
            // 보다 엄격한 권한 검증이 필요합니다. (예: referenceIdx를 통해 부모 엔티티를 조회하고 그 권한을 확인)
            // User firstFileUploader = filesToDelete.get(0).getUploadedBy();
            // if (!firstFileUploader.getIdx().equals(currentUser.getIdx())) {
            // //   throw new BusinessLogicException(ErrorCode.ACCESS_DENIED, "관련 파일들을 삭제할 권한이 없습니다.");
            // // 위 로직은 모든 파일이 같은 사용자에 의해 업로드되었음을 가정하므로 부적절.
            // // 이 메소드는 보통 부모 엔티티 삭제 시 호출되므로, 부모 엔티티 삭제 권한으로 갈음하는 경우가 많음.
            // }
            log.info("{} 타입의 {} ID에 연결된 파일 {}개 삭제 시작 (요청자: {})", referenceType, referenceIdx, filesToDelete.size(), currentUser.getNickname());

            for (File file : filesToDelete) {
                // 개별 파일 삭제 시에도 권한 체크가 필요하다면 deleteFile 메소드를 재활용하거나,
                // 여기서는 상위 수준의 권한(예: 게시물 삭제 권한)이 이미 확인되었다고 가정하고 진행합니다.
                try {
                    Path filePath = this.fileStorageLocation.resolve(file.getFilePath()).normalize();
                    Files.deleteIfExists(filePath);
                } catch (IOException ex) {
                    log.error("물리적 파일 삭제 실패 (계속 진행): {}, 원인: {}", file.getFilePath(), ex.getMessage());
                    // 하나의 파일 삭제 실패가 전체 작업을 중단시키지 않도록 처리 (정책에 따라 다름)
                }
            }
            fileRepository.deleteAll(filesToDelete); // DB에서 메타데이터 일괄 삭제
            log.info("{} 타입의 {} ID에 연결된 파일 {}개 DB 메타데이터 삭제 완료", referenceType, referenceIdx, filesToDelete.size());
        }
    }