package com.culture.perfomingarts.domain.practiceParticipation.dto;

import com.culture.perfomingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipationCreateRequestDto {

    private Long practiceId;
    private Long userId;
    private PracticeParticipationStatus status;
    private String reason;
    private String comment;
    private Boolean isExcused;

    @Builder
    public PracticeParticipationCreateRequestDto(Long practiceId, Long userId,
                                               PracticeParticipationStatus status,
                                               String reason, String comment, Boolean isExcused) {
        this.practiceId = practiceId;
        this.userId = userId;
        this.status = status;
        this.reason = reason;
        this.comment = comment;
        this.isExcused = isExcused;
    }
}