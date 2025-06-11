package com.culture.performingarts.domain.practiceParticipation.dto;

import com.culture.performingarts.domain.practiceParticipation.entity.PracticeParticipation;
import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipationResponseDto {

    private Long id;
    private Long practiceId;
    private Long userId;
    private PracticeParticipationStatus status;
    private String reason;
    private String comment;
    private Boolean isExcused;
    private Boolean isPresent;
    private Boolean isAbsent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PracticeParticipationResponseDto(Long id, Long practiceId, Long userId,
                                          PracticeParticipationStatus status, String reason,
                                          String comment, Boolean isExcused, Boolean isPresent,
                                          Boolean isAbsent, LocalDateTime createdAt,
                                          LocalDateTime updatedAt) {
        this.id = id;
        this.practiceId = practiceId;
        this.userId = userId;
        this.status = status;
        this.reason = reason;
        this.comment = comment;
        this.isExcused = isExcused;
        this.isPresent = isPresent;
        this.isAbsent = isAbsent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PracticeParticipationResponseDto fromEntity(PracticeParticipation participation) {
        return PracticeParticipationResponseDto.builder()
                .id(participation.getId())
                .practiceId(participation.getPracticeId())
                .userId(participation.getUserId())
                .status(participation.getStatus())
                .reason(participation.getReason())
                .comment(participation.getComment())
                .isExcused(participation.getIsExcused())
                .isPresent(participation.isPresent())
                .isAbsent(participation.isAbsent())
                .createdAt(participation.getCreatedAt())
                .updatedAt(participation.getUpdatedAt())
                .build();
    }
}