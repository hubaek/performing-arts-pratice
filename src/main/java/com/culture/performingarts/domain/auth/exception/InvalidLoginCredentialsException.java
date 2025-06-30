package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class InvalidLoginCredentialsException extends BusinessException {
    public InvalidLoginCredentialsException() {
        super(ErrorCode.INVALID_LOGIN_CREDENTIALS);
    }
    
    public InvalidLoginCredentialsException(String message) {
        super(message, ErrorCode.INVALID_LOGIN_CREDENTIALS);
    }
}