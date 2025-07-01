package com.culture.performingarts.global.exception;

public class DuplicateResourceException extends BusinessException {
    
    public DuplicateResourceException() {
        super("이미 존재하는 리소스입니다", ErrorCode.INVALID_INPUT_VALUE);
    }
    
    public DuplicateResourceException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
    
    public DuplicateResourceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}