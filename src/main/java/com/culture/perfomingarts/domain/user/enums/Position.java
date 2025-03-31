package com.culture.perfomingarts.domain.user.enums;

public enum Position {
    MEMBER("단원"),
    LEADER("단장"),
    MANAGER("과장");

    private final String label;

    Position(String label) {
        this.label = label;
    }
}
