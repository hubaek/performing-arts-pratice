package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}