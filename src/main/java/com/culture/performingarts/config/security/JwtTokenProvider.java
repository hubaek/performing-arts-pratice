package com.culture.performingarts.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스
 */
@Slf4j
@Component
public class JwtTokenProvider {
    
    private final SecretKey secretKey;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;
    
    public JwtTokenProvider(
            @Value("${jwt.secret:performingArtsSecretKeyForJwtTokenGeneration123456789}") String secret,
            @Value("${jwt.access-token-validity-in-seconds:3600}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds:86400}") long refreshTokenValidityInSeconds) {
        
        // 비밀키가 짧으면 안전한 키 생성
        if (secret.length() < 32) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            log.warn("JWT secret key is too short. Generated a secure random key.");
        } else {
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
        
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }
    
    /**
     * Access Token 생성
     */
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenValidityInSeconds);
    }
    
    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenValidityInSeconds);
    }
    
    /**
     * JWT 토큰 생성
     */
    private String createToken(Authentication authentication, long validityInSeconds) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        Date expiryDate = Date.from(
            ZonedDateTime.now().plusSeconds(validityInSeconds).toInstant()
        );
        
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
    
    /**
     * 토큰에서 사용자명 추출
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getSubject();
    }
    
    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
    
    /**
     * 토큰 만료 시간 확인
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return claims.getExpiration();
    }
    
    /**
     * 토큰이 만료되었는지 확인
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}