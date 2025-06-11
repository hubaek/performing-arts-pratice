package com.culture.perfomingarts.domain.practiceParticipation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipationBulkCreateRequestDto {

    private Long practiceId;
    private List<PracticeParticipationCreateRequestDto> participations;

    @Builder
    public PracticeParticipationBulkCreateRequestDto(Long practiceId,
                                                   List<PracticeParticipationCreateRequestDto> participations) {
        this.practiceId = practiceId;
        this.participations = participations;
    }
}