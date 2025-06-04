package com.example.softengineerwebpr.domain.file.service; // [cite: 124]

import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.user.entity.User; //
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 단일 파일을 저장하고 메타데이터를 DB에 기록합니다.
     */
    FileResponseDto storeFile(MultipartFile file, FileReferenceType referenceType, Long referenceIdx, User uploadedBy);

    /**
     * 여러 파일을 저장하고 메타데이터를 DB에 기록합니다.
     */
    List<FileResponseDto> storeFiles(List<MultipartFile> files, FileReferenceType referenceType, Long referenceIdx, User uploadedBy);

    /**
     * 특정 참조 대상에 연결된 파일 목록을 조회합니다.
     */
    List<FileResponseDto> getFilesForReference(FileReferenceType referenceType, Long referenceIdx);

    /**
     * 파일 ID로 파일을 로드하여 다운로드할 수 있는 Resource 객체로 반환합니다.
     */
    Resource loadFileAsResource(Long fileId, User currentUser);

    /**
     * 파일 ID로 실제 파일명을 가져옵니다 (다운로드 시 Content-Disposition 헤더에 사용).
     */
    String getOriginalFileName(Long fileId);


    /**
     * 파일을 삭제합니다 (물리적 파일 및 DB 메타데이터).
     */
    void deleteFile(Long fileId, User currentUser);

    /**
     * 특정 참조 대상(게시글, 댓글, 업무)에 연결된 모든 파일을 삭제합니다. (새로 추가)
     * @param referenceType 파일이 첨부된 대상의 유형
     * @param referenceIdx 참조 대상의 ID
     * @param currentUser 현재 작업을 수행하는 사용자 (권한 확인용)
     */
    void deleteFilesForReference(FileReferenceType referenceType, Long referenceIdx, User currentUser);
}