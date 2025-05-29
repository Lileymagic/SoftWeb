package com.example.softengineerwebpr.domain.post.dto;

import com.example.softengineerwebpr.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostBasicResponseDto {
    private Long idx;
    private String title;
    private String authorNickname; // 작성자 닉네임
    private LocalDateTime createdAt;
    // 필요시 추가: private int commentCount; (댓글 수)

    @Builder
    public PostBasicResponseDto(Long idx, String title, String authorNickname, LocalDateTime createdAt) {
        this.idx = idx;
        this.title = title;
        this.authorNickname = authorNickname;
        this.createdAt = createdAt;
    }

    public static PostBasicResponseDto fromEntity(Post post) {
        if (post == null) {
            return null;
        }
        return PostBasicResponseDto.builder()
                .idx(post.getIdx())
                .title(post.getTitle())
                .authorNickname(post.getUser() != null ? post.getUser().getNickname() : "알 수 없음") // User 객체가 로드되었다고 가정
                .createdAt(post.getCreatedAt())
                .build();
    }
}