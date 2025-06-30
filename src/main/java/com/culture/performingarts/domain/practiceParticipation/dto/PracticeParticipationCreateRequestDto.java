package com.culture.performingarts.domain.practiceParticipation.dto;

import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipationCreateRequestDto {

    @NotNull(message = "연습 ID는 필수입니다")
    private Long practiceId;
    
    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;
    
    @NotNull(message = "참여 상태는 필수입니다")
    private PracticeParticipationStatus status;
    
    @Size(max = 200, message = "사유는 200자를 초과할 수 없습니다")
    private String reason;
    
    @Size(max = 500, message = "코멘트는 500자를 초과할 수 없습니다")
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