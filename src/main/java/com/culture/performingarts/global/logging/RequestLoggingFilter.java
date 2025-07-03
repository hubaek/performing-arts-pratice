package com.culture.performingarts.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP 요청 로깅 필터
 */
@Component
@Order(1)
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String USER_ID_KEY = "userId";
    private static final String IP_ADDRESS_KEY = "ipAddress";
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String ipAddress = getClientIpAddress(request);
        
        // MDC에 요청 정보 설정
        MDC.put(REQUEST_ID_KEY, requestId);
        MDC.put(IP_ADDRESS_KEY, ipAddress);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 요청 로깅 (정적 리소스 제외)
            if (!isStaticResource(uri)) {
                String fullUri = queryString != null ? uri + "?" + queryString : uri;
                log.info("HTTP Request started - method: {}, uri: {}, requestId: {}, ip: {}", 
                    method, fullUri, requestId, ipAddress);
            }
            
            filterChain.doFilter(request, response);
            
            long executionTime = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            
            // 응답 로깅
            if (!isStaticResource(uri)) {
                if (executionTime > 3000) {
                    log.warn("Slow HTTP Request - method: {}, uri: {}, status: {}, executionTime: {}ms, requestId: {}, ip: {}", 
                        method, uri, status, executionTime, requestId, ipAddress);
                } else {
                    log.info("HTTP Request completed - method: {}, uri: {}, status: {}, executionTime: {}ms, requestId: {}, ip: {}", 
                        method, uri, status, executionTime, requestId, ipAddress);
                }
            }
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("HTTP Request failed - method: {}, uri: {}, executionTime: {}ms, requestId: {}, ip: {}, error: {}", 
                method, uri, executionTime, requestId, ipAddress, e.getMessage(), e);
            throw e;
        } finally {
            // MDC 정리
            MDC.clear();
        }
    }
    
    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * 정적 리소스 요청 여부 확인
     */
    private boolean isStaticResource(String uri) {
        return uri.matches(".*(\\.(css|js|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot))$") ||
               uri.startsWith("/static/") ||
               uri.startsWith("/webjars/") ||
               uri.startsWith("/favicon.ico");
    }
}