package com.culture.performingarts.global.exception;

public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException() {
        super("인증이 필요합니다", ErrorCode.UNAUTHORIZED);
    }
    
    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}