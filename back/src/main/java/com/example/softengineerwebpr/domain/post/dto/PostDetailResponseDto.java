package com.example.softengineerwebpr.domain.post.dto;

import com.example.softengineerwebpr.domain.file.dto.FileResponseDto;
import com.example.softengineerwebpr.domain.file.entity.File; // File 엔티티 임포트
import com.example.softengineerwebpr.domain.post.entity.Post;
import com.example.softengineerwebpr.domain.user.dto.UserBasicInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors; // Collectors 임포트

@Getter
public class PostDetailResponseDto {
    private Long idx;
    private String title;
    private String content;
    private UserBasicInfoDto author;
    private Long taskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponseDto> files;

    @Builder
    public PostDetailResponseDto(Long idx, String title, String content, UserBasicInfoDto author,
                                 Long taskId, LocalDateTime createdAt, LocalDateTime updatedAt,
                                 List<FileResponseDto> files) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.author = author;
        this.taskId = taskId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.files = files;
    }

    // List<File> postFiles 파라미터 추가
    public static PostDetailResponseDto fromEntity(Post post, List<File> postFiles) {
        if (post == null) {
            return null;
        }

        UserBasicInfoDto authorDto = null;
        if (post.getUser() != null) {
            authorDto = UserBasicInfoDto.fromEntity(post.getUser());
        }

        Long taskId = null;
        if (post.getTask() != null) {
            taskId = post.getTask().getIdx();
        }

        List<FileResponseDto> fileDtos = Collections.emptyList();
        if (postFiles != null && !postFiles.isEmpty()) {
            // FileResponseDto.fromEntity를 사용하여 File 목록을 DTO 목록으로 변환
            fileDtos = postFiles.stream()
                    .map(FileResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return PostDetailResponseDto.builder()
                .idx(post.getIdx())
                .title(post.getTitle())
                .content(post.getContent())
                .author(authorDto)
                .taskId(taskId)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .files(fileDtos) // 변환된 DTO 리스트 사용
                .build();
    }
}