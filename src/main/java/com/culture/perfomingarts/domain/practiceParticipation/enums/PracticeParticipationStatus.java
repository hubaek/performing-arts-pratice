package com.culture.perfomingarts.domain.practiceParticipation.enums;

public enum PracticeParticipationStatus {
    ATTENDANCE("출석"),
    LATE("지각"),
    ABSENT("결석");

    private final String status;

    PracticeParticipationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
