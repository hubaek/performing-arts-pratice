package com.culture.performingarts.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 성능 모니터링 및 로깅을 위한 AOP
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String EXECUTION_TIME_KEY = "executionTime";
    
    /**
     * Service 레이어 메서드 실행 시간 측정
     */
    @Around("execution(* com.culture.performingarts.domain.*.service.*.*(..))")
    public Object logServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = getOrCreateRequestId();
        String methodName = joinPoint.getSignature().toShortString();
        
        long startTime = System.currentTimeMillis();
        
        try {
            log.debug("Service method started - method: {}, requestId: {}", methodName, requestId);
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            MDC.put(EXECUTION_TIME_KEY, String.valueOf(executionTime));
            
            if (executionTime > 1000) {
                log.warn("Slow service method detected - method: {}, executionTime: {}ms, requestId: {}", 
                    methodName, executionTime, requestId);
            } else {
                log.debug("Service method completed - method: {}, executionTime: {}ms, requestId: {}", 
                    methodName, executionTime, requestId);
            }
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Service method failed - method: {}, executionTime: {}ms, requestId: {}, error: {}", 
                methodName, executionTime, requestId, e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove(EXECUTION_TIME_KEY);
        }
    }
    
    /**
     * Repository 레이어 메서드 실행 시간 측정
     */
    @Around("execution(* com.culture.performingarts.domain.*.repository.*.*(..))")
    public Object logRepositoryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = getOrCreateRequestId();
        String methodName = joinPoint.getSignature().toShortString();
        
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (executionTime > 500) {
                log.warn("Slow repository query detected - method: {}, executionTime: {}ms, requestId: {}", 
                    methodName, executionTime, requestId);
            } else {
                log.debug("Repository method completed - method: {}, executionTime: {}ms, requestId: {}", 
                    methodName, executionTime, requestId);
            }
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Repository method failed - method: {}, executionTime: {}ms, requestId: {}, error: {}", 
                methodName, executionTime, requestId, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 요청 ID 생성 또는 조회
     */
    private String getOrCreateRequestId() {
        String requestId = MDC.get(REQUEST_ID_KEY);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString().substring(0, 8);
            MDC.put(REQUEST_ID_KEY, requestId);
        }
        return requestId;
    }
}