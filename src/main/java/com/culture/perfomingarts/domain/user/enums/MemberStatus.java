package com.culture.perfomingarts.domain.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("입단"),
    PAUSE("휴단"),
    LEFT("탈단");

    private final String label;
    public String getLabel() {
        return label;
    }
}
