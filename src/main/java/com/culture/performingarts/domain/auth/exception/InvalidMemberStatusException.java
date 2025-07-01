package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class InvalidMemberStatusException extends BusinessException {
    
    public InvalidMemberStatusException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}