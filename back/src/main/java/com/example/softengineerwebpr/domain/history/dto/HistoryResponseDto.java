package com.example.softengineerwebpr.domain.history.dto; // domain.history.dto 패키지

import com.example.softengineerwebpr.domain.history.entity.History;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HistoryResponseDto {
    private Long idx;
    private String actionType;
    private String description;
    private String createdByNickname;
    private LocalDateTime createdAt;

    @Builder
    public HistoryResponseDto(Long idx, String actionType, String description, String createdByNickname, LocalDateTime createdAt) {
        this.idx = idx;
        this.actionType = actionType;
        this.description = description;
        this.createdByNickname = createdByNickname;
        this.createdAt = createdAt;
    }

    /**
     * History 엔티티를 HistoryResponseDto로 변환하는 정적 팩토리 메소드
     * @param history 변환할 History 엔티티 객체
     * @return 변환된 HistoryResponseDto 객체
     */
    public static HistoryResponseDto fromEntity(History history) { // <<<< 이 메소드가 필요합니다.
        if (history == null) return null;

        String nickname = "알 수 없음";
        if (history.getCreatedBy() != null) {
            nickname = history.getCreatedBy().getNickname();
        }

        return HistoryResponseDto.builder()
                .idx(history.getIdx())
                .actionType(history.getActionType().name())
                .description(history.getDescription())
                .createdByNickname(nickname)
                .createdAt(history.getCreatedAt())
                .build();
    }
}