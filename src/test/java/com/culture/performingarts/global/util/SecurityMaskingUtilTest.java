package com.culture.performingarts.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityMaskingUtilTest {
    
    @Test
    @DisplayName("JWT 토큰 마스킹 - 정상적인 토큰")
    void maskJwtToken_ValidToken() {
        // given
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        
        // when
        String masked = SecurityMaskingUtil.maskJwtToken(token);
        
        // then
        assertThat(masked).startsWith("eyJhbGciOi");
        assertThat(masked).contains("*");
        assertThat(masked.length()).isEqualTo(token.length());
    }
    
    @Test
    @DisplayName("JWT 토큰 마스킹 - 짧은 토큰")
    void maskJwtToken_ShortToken() {
        // given
        String token = "short";
        
        // when
        String masked = SecurityMaskingUtil.maskJwtToken(token);
        
        // then
        assertThat(masked).isEqualTo("*****");
    }
    
    @Test
    @DisplayName("JWT 토큰 마스킹 - null 토큰")
    void maskJwtToken_NullToken() {
        // when
        String masked = SecurityMaskingUtil.maskJwtToken(null);
        
        // then
        assertThat(masked).isEqualTo("null");
    }
    
    @Test
    @DisplayName("JWT 토큰 마스킹 - 빈 토큰")
    void maskJwtToken_EmptyToken() {
        // when
        String masked = SecurityMaskingUtil.maskJwtToken("");
        
        // then
        assertThat(masked).isEqualTo("null");
    }
    
    @Test
    @DisplayName("이메일 마스킹 - 정상적인 이메일")
    void maskEmail_ValidEmail() {
        // given
        String email = "test@example.com";
        
        // when
        String masked = SecurityMaskingUtil.maskEmail(email);
        
        // then
        assertThat(masked).isEqualTo("te**@example.com");
    }
    
    @Test
    @DisplayName("이메일 마스킹 - 긴 이메일")
    void maskEmail_LongEmail() {
        // given
        String email = "verylongemail@example.com";
        
        // when
        String masked = SecurityMaskingUtil.maskEmail(email);
        
        // then
        assertThat(masked).isEqualTo("ve***********@example.com");
    }
    
    @Test
    @DisplayName("이메일 마스킹 - 짧은 로컬 부분")
    void maskEmail_ShortLocalPart() {
        // given
        String email = "ab@example.com";
        
        // when
        String masked = SecurityMaskingUtil.maskEmail(email);
        
        // then
        assertThat(masked).isEqualTo("**@example.com");
    }
    
    @Test
    @DisplayName("이메일 마스킹 - 잘못된 형식")
    void maskEmail_InvalidFormat() {
        // given
        String email = "invalidemail";
        
        // when
        String masked = SecurityMaskingUtil.maskEmail(email);
        
        // then
        assertThat(masked).isEqualTo("************");
    }
    
    @Test
    @DisplayName("이메일 마스킹 - null 이메일")
    void maskEmail_NullEmail() {
        // when
        String masked = SecurityMaskingUtil.maskEmail(null);
        
        // then
        assertThat(masked).isEqualTo("null");
    }
    
    @Test
    @DisplayName("비밀번호 마스킹")
    void maskPassword() {
        // given
        String password = "mySecretPassword123";
        
        // when
        String masked = SecurityMaskingUtil.maskPassword(password);
        
        // then
        assertThat(masked).isEqualTo("********");
    }
    
    @Test
    @DisplayName("비밀번호 마스킹 - null")
    void maskPassword_Null() {
        // when
        String masked = SecurityMaskingUtil.maskPassword(null);
        
        // then
        assertThat(masked).isEqualTo("null");
    }
    
    @Test
    @DisplayName("개인정보 마스킹")
    void maskPersonalInfo() {
        // given
        String personalInfo = "홍길동";
        
        // when
        String masked = SecurityMaskingUtil.maskPersonalInfo(personalInfo, 1);
        
        // then
        assertThat(masked).isEqualTo("홍**");
    }
    
    @Test
    @DisplayName("전화번호 마스킹 - 하이픈 포함")
    void maskPhoneNumber_WithHyphen() {
        // given
        String phoneNumber = "010-1234-5678";
        
        // when
        String masked = SecurityMaskingUtil.maskPhoneNumber(phoneNumber);
        
        // then
        assertThat(masked).isEqualTo("010-****-5678");
    }
    
    @Test
    @DisplayName("전화번호 마스킹 - 하이픈 없음")
    void maskPhoneNumber_WithoutHyphen() {
        // given
        String phoneNumber = "01012345678";
        
        // when
        String masked = SecurityMaskingUtil.maskPhoneNumber(phoneNumber);
        
        // then
        assertThat(masked).isEqualTo("010****5678");
    }
    
    @Test
    @DisplayName("전화번호 마스킹 - 일반적인 마스킹")
    void maskPhoneNumber_GeneralMasking() {
        // given
        String phoneNumber = "02-123-4567";
        
        // when
        String masked = SecurityMaskingUtil.maskPhoneNumber(phoneNumber);
        
        // then
        assertThat(masked).startsWith("02-");
        assertThat(masked).contains("*");
    }
}