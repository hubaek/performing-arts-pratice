package com.culture.performingarts.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 민감정보 마스킹을 위한 유틸리티 클래스
 * 로깅 시 개인정보 및 보안 정보를 안전하게 마스킹합니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityMaskingUtil {
    
    private static final String MASK_CHARACTER = "*";
    private static final int JWT_VISIBLE_LENGTH = 10;
    private static final int EMAIL_LOCAL_VISIBLE_LENGTH = 2;
    
    /**
     * JWT 토큰을 마스킹합니다.
     * 앞의 10자만 보이고 나머지는 '*'로 마스킹됩니다.
     * 
     * @param token JWT 토큰
     * @return 마스킹된 토큰 문자열
     */
    public static String maskJwtToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return "null";
        }
        
        String trimmedToken = token.trim();
        if (trimmedToken.length() <= JWT_VISIBLE_LENGTH) {
            return MASK_CHARACTER.repeat(trimmedToken.length());
        }
        
        return trimmedToken.substring(0, JWT_VISIBLE_LENGTH) + 
               MASK_CHARACTER.repeat(trimmedToken.length() - JWT_VISIBLE_LENGTH);
    }
    
    /**
     * 이메일 주소를 마스킹합니다.
     * 로컬 부분의 앞 2자와 도메인만 보이고 나머지는 '*'로 마스킹됩니다.
     * 예: test@example.com → te***@example.com
     * 
     * @param email 이메일 주소
     * @return 마스킹된 이메일 문자열
     */
    public static String maskEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "null";
        }
        
        String trimmedEmail = email.trim();
        int atIndex = trimmedEmail.indexOf('@');
        
        if (atIndex <= 0 || atIndex == trimmedEmail.length() - 1) {
            // 잘못된 이메일 형식인 경우 전체 마스킹
            return MASK_CHARACTER.repeat(trimmedEmail.length());
        }
        
        String localPart = trimmedEmail.substring(0, atIndex);
        String domainPart = trimmedEmail.substring(atIndex);
        
        if (localPart.length() <= EMAIL_LOCAL_VISIBLE_LENGTH) {
            return MASK_CHARACTER.repeat(localPart.length()) + domainPart;
        }
        
        return localPart.substring(0, EMAIL_LOCAL_VISIBLE_LENGTH) + 
               MASK_CHARACTER.repeat(localPart.length() - EMAIL_LOCAL_VISIBLE_LENGTH) + 
               domainPart;
    }
    
    /**
     * 비밀번호를 완전히 마스킹합니다.
     * 
     * @param password 비밀번호
     * @return 마스킹된 비밀번호 문자열
     */
    public static String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "null";
        }
        return MASK_CHARACTER.repeat(8); // 일정한 길이로 마스킹
    }
    
    /**
     * 일반적인 개인정보를 마스킹합니다.
     * 앞의 2자만 보이고 나머지는 '*'로 마스킹됩니다.
     * 
     * @param personalInfo 개인정보 문자열
     * @param visibleLength 보여줄 앞자리 길이
     * @return 마스킹된 문자열
     */
    public static String maskPersonalInfo(String personalInfo, int visibleLength) {
        if (personalInfo == null || personalInfo.trim().isEmpty()) {
            return "null";
        }
        
        String trimmed = personalInfo.trim();
        if (trimmed.length() <= visibleLength) {
            return MASK_CHARACTER.repeat(trimmed.length());
        }
        
        return trimmed.substring(0, visibleLength) + 
               MASK_CHARACTER.repeat(trimmed.length() - visibleLength);
    }
    
    /**
     * 전화번호를 마스킹합니다.
     * 예: 010-1234-5678 → 010-****-5678
     * 
     * @param phoneNumber 전화번호
     * @return 마스킹된 전화번호
     */
    public static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "null";
        }
        
        String trimmed = phoneNumber.trim();
        // 전화번호 패턴 확인 (XXX-XXXX-XXXX 또는 XXXXXXXXXXX)
        if (trimmed.matches("^\\d{3}-\\d{4}-\\d{4}$")) {
            // XXX-XXXX-XXXX 형태
            String[] parts = trimmed.split("-");
            return parts[0] + "-" + MASK_CHARACTER.repeat(4) + "-" + parts[2];
        } else if (trimmed.matches("^\\d{11}$")) {
            // XXXXXXXXXXX 형태
            return trimmed.substring(0, 3) + 
                   MASK_CHARACTER.repeat(4) + 
                   trimmed.substring(7);
        } else {
            // 일반적인 마스킹
            return maskPersonalInfo(trimmed, 3);
        }
    }
}