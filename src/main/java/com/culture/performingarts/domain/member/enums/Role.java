package com.culture.performingarts.domain.member.enums;

/**
 * 회원 역할 열거형
 * 시스템 내에서 회원의 권한을 구분합니다.
 */
public enum Role {
    /**
     * 일반 사용자 (단원)
     */
    USER("ROLE_USER", "일반 사용자"),
    
    /**
     * 관리자 (관리 권한)
     */
    ADMIN("ROLE_ADMIN", "관리자");
    
    private final String authority;
    private final String description;
    
    Role(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }
    
    public String getAuthority() {
        return authority;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 관리자 권한을 가지고 있는지 확인
     * @return 관리자 권한이 있으면 true
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * 일반 사용자 권한을 가지고 있는지 확인
     * @return 일반 사용자 권한이면 true
     */
    public boolean isUser() {
        return this == USER;
    }
}