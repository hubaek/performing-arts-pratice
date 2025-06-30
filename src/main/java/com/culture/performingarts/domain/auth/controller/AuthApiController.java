package com.culture.performingarts.domain.auth.controller;

import com.culture.performingarts.domain.auth.dto.AuthResponseDto;
import com.culture.performingarts.domain.auth.dto.LoginRequestDto;
import com.culture.performingarts.domain.auth.dto.SignupRequestDto;
import com.culture.performingarts.domain.auth.dto.TokenRefreshRequestDto;
import com.culture.performingarts.domain.auth.service.AuthService;
import com.culture.performingarts.domain.member.dto.MemberResponseDto;
import com.culture.performingarts.domain.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 REST API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    
    private final AuthService authService;
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            AuthResponseDto response = authService.login(loginRequest);
            log.info("Login successful for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getEmail(), e);
            throw e;
        }
    }
    
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequest) {
        try {
            AuthResponseDto response = authService.signup(signupRequest);
            log.info("Signup successful for user: {}", signupRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Signup failed for user: {}", signupRequest.getEmail(), e);
            throw e;
        }
    }
    
    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequest) {
        try {
            AuthResponseDto response = authService.refreshToken(tokenRefreshRequest);
            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw e;
        }
    }
    
    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getCurrentUser() {
        try {
            Member currentMember = authService.getCurrentMember();
            MemberResponseDto response = MemberResponseDto.from(currentMember);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get current user info", e);
            throw e;
        }
    }
    
    /**
     * 로그아웃 (클라이언트에서 토큰 삭제로 처리)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // JWT는 stateless하므로 서버에서 별도 처리 없이
        // 클라이언트에서 토큰을 삭제하도록 안내
        log.info("Logout requested");
        return ResponseEntity.ok().build();
    }
}