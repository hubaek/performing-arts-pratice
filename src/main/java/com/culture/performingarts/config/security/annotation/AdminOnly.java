package com.culture.performingarts.config.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자 권한이 필요한 메서드에 적용하는 어노테이션
 * 
 * 사용 예시:
 * @AdminOnly
 * public ResponseEntity<?> adminOnlyMethod() {
 *     // 관리자만 접근 가능한 로직
 * }
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN')")
public @interface AdminOnly {
}