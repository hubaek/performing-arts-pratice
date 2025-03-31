package com.culture.perfomingarts.domain.practiceParticipation.entity;

import com.culture.perfomingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long practiceId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PracticeParticipationStatus status; // 예: "출석", "지각", "결석"

    private String reason; // 결석 사유

    private String comment; // 비고
}
