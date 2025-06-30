package com.culture.performingarts.domain.practice.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeNotFoundException extends BusinessException {
    public PracticeNotFoundException() {
        super(ErrorCode.PRACTICE_NOT_FOUND);
    }
    
    public PracticeNotFoundException(String message) {
        super(message, ErrorCode.PRACTICE_NOT_FOUND);
    }
}