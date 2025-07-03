package com.culture.performingarts.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 성능 로깅을 위한 AOP 컴포넌트
 * Service와 Repository 메서드의 실행 시간을 추적합니다.
 */
@Slf4j
@Aspect
@Component
public class PerformanceLoggingAspect {
    
    @Value("${logging.performance.threshold-ms:1000}")
    private long performanceThresholdMs;
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    /**
     * Service 클래스의 모든 public 메서드 실행 시간 추적
     */
    @Around("execution(public * com.culture.performingarts.domain.*.service.*.*(..))")
    public Object logServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "SERVICE");
    }
    
    /**
     * Repository 클래스의 모든 메서드 실행 시간 추적
     */
    @Around("execution(* com.culture.performingarts.domain.*.repository.*.*(..))")
    public Object logRepositoryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "REPOSITORY");
    }
    
    /**
     * Controller 클래스의 모든 public 메서드 실행 시간 추적
     */
    @Around("execution(public * com.culture.performingarts.domain.*.controller.*.*(..))")
    public Object logControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "CONTROLLER");
    }
    
    /**
     * 메서드 실행 시간을 측정하고 로깅합니다.
     */
    private Object logExecutionTime(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;
        
        try {
            stopWatch.start();
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            long executionTime = stopWatch.getTotalTimeMillis();
            
            // 임계값을 초과한 경우에만 로깅 (운영환경에서 로그 부하 감소)
            if (executionTime >= performanceThresholdMs) {
                if ("prod".equals(activeProfile)) {
                    log.warn("SLOW_EXECUTION [{}] {}: {}ms", layer, fullMethodName, executionTime);
                } else {
                    log.warn("SLOW_EXECUTION [{}] {}: {}ms (threshold: {}ms)", 
                            layer, fullMethodName, executionTime, performanceThresholdMs);
                }
            } else {
                // 개발환경에서는 모든 실행 시간 추적
                if (!"prod".equals(activeProfile)) {
                    log.debug("PERFORMANCE [{}] {}: {}ms", layer, fullMethodName, executionTime);
                }
            }
            
            return result;
            
        } catch (Exception ex) {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();
            
            if ("prod".equals(activeProfile)) {
                log.error("EXCEPTION_EXECUTION [{}] {}: {}ms - {}", 
                        layer, fullMethodName, executionTime, ex.getMessage());
            } else {
                log.error("EXCEPTION_EXECUTION [{}] {}: {}ms", 
                        layer, fullMethodName, executionTime, ex);
            }
            
            throw ex;
        }
    }
}