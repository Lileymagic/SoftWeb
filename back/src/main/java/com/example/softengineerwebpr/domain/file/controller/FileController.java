package com.example.softengineerwebpr.domain.file.controller; // [cite: 123]

import com.example.softengineerwebpr.common.dto.ApiResponse; //
import com.example.softengineerwebpr.common.util.AuthenticationUtil; //
import com.example.softengineerwebpr.domain.file.dto.FileResponseDto; //
import com.example.softengineerwebpr.domain.file.entity.FileReferenceType; //
import com.example.softengineerwebpr.domain.file.service.FileService;
import com.example.softengineerwebpr.domain.user.entity.User; //
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final AuthenticationUtil authenticationUtil; //

    // 단일 파일 업로드 (예시: 게시글, 댓글, 업무 등에 파일 연결)
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileResponseDto>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("referenceType") FileReferenceType referenceType,
            @RequestParam("referenceIdx") Long referenceIdx,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        FileResponseDto fileResponse = fileService.storeFile(file, referenceType, referenceIdx, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "파일이 성공적으로 업로드되었습니다.", fileResponse));
    }

    // 여러 파일 동시 업로드 (필요시)
    @PostMapping("/upload/multiple")
    public ResponseEntity<ApiResponse<List<FileResponseDto>>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("referenceType") FileReferenceType referenceType,
            @RequestParam("referenceIdx") Long referenceIdx,
            Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication);
        if (files == null || files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다.")); // ApiResponse.error는 직접 정의 필요
        }
        List<FileResponseDto> fileResponses = fileService.storeFiles(files, referenceType, referenceIdx, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "파일들이 성공적으로 업로드되었습니다.", fileResponses));
    }


    // 특정 참조 객체(게시글, 댓글, 업무)의 파일 목록 조회
    @GetMapping("/reference/{referenceType}/{referenceIdx}")
    public ResponseEntity<ApiResponse<List<FileResponseDto>>> getFilesForReference(
            @PathVariable FileReferenceType referenceType,
            @PathVariable Long referenceIdx) {
        List<FileResponseDto> files = fileService.getFilesForReference(referenceType, referenceIdx);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "파일 목록 조회 성공", files));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        Resource resource = fileService.loadFileAsResource(fileId, currentUser);
        String originalFileName = fileService.getOriginalFileName(fileId); // 원본 파일명 가져오기

        // 파일명 인코딩 (한글 등 비 ASCII 문자 처리)
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error("Filename encoding failed for: {}", originalFileName, e);
            encodedFileName = "downloaded_file"; // 기본값
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable Long fileId, Authentication authentication) {
        User currentUser = authenticationUtil.getCurrentUser(authentication); //
        fileService.deleteFile(fileId, currentUser);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "파일이 성공적으로 삭제되었습니다."));
    }
}