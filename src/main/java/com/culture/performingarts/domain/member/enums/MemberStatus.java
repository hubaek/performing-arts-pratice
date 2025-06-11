package com.culture.performingarts.domain.member.enums;

/**
 * 회원 상태 열거형
 * 공연예술단체 단원의 활동 상태를 관리합니다.
 */
public enum MemberStatus {
    
    /**
     * 활동 - 정상적으로 활동 중인 단원
     */
    ACTIVE("활동"),
    
    /**
     * 휴단 - 일시적으로 활동을 중단한 단원 (복귀 가능)
     */
    LEAVE_OF_ABSENCE("휴단"),
    
    /**
     * 탈퇴 - 단체를 떠난 단원 (기록 보존용)
     */
    INACTIVE("탈퇴");
    
    private final String description;
    
    MemberStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 활동 가능한 상태인지 확인
     * @return 활동 가능하면 true
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 복귀 가능한 상태인지 확인 (휴단 상태)
     * @return 복귀 가능하면 true
     */
    public boolean canReturn() {
        return this == LEAVE_OF_ABSENCE;
    }
}