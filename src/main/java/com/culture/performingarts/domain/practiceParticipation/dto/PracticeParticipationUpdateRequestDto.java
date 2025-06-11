package com.culture.performingarts.domain.practiceParticipation.dto;

import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipationUpdateRequestDto {

    private PracticeParticipationStatus status;
    private String reason;
    private String comment;
    private Boolean isExcused;

    @Builder
    public PracticeParticipationUpdateRequestDto(PracticeParticipationStatus status,
                                               String reason, String comment, Boolean isExcused) {
        this.status = status;
        this.reason = reason;
        this.comment = comment;
        this.isExcused = isExcused;
    }
}