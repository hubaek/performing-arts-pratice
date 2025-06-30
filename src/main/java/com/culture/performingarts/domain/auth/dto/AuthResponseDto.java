package com.culture.performingarts.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 응답 DTO
 * 로그인 성공 시 JWT 토큰과 함께 반환됩니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthResponseDto {
    
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long memberId;
    private String name;
    private String email;
    
    @Builder
    public AuthResponseDto(String accessToken, String refreshToken, Long memberId, String name, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }
}