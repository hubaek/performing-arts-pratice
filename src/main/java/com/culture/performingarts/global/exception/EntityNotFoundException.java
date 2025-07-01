package com.culture.performingarts.global.exception;

public class EntityNotFoundException extends BusinessException {
    
    public EntityNotFoundException() {
        super("엔터티를 찾을 수 없습니다", ErrorCode.ENTITY_NOT_FOUND);
    }
    
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
    
    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}