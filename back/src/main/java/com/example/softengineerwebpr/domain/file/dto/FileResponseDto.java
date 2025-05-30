package com.example.softengineerwebpr.domain.file.dto;

import com.example.softengineerwebpr.domain.file.entity.File; // File 엔티티 임포트
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponseDto {
    private Long idx;
    private String fileName;
    private String filePath; // 실제로는 다운로드 URL 또는 파일 식별자를 제공할 수 있습니다.
    private Integer fileSize;
    private LocalDateTime uploadedAt;

    @Builder
    public FileResponseDto(Long idx, String fileName, String filePath, Integer fileSize, LocalDateTime uploadedAt) {
        this.idx = idx;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    // 주석 해제 및 File 엔티티 사용
    public static FileResponseDto fromEntity(File file) {
        if (file == null) return null;
        return FileResponseDto.builder()
                .idx(file.getIdx())
                .fileName(file.getFileName())
                .filePath(file.getFilePath()) // 예: "/api/files/download/" + file.getIdx() 또는 저장된 경로 그대로
                .fileSize(file.getFileSize())
                .uploadedAt(file.getUploadedAt())
                .build();
    }
}