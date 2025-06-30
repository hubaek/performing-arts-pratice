package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
    
    public InvalidTokenException(String message) {
        super(message, ErrorCode.INVALID_TOKEN);
    }
}