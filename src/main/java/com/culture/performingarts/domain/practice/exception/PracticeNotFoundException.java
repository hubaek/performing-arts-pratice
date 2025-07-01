package com.culture.performingarts.domain.practice.exception;

import com.culture.performingarts.global.exception.EntityNotFoundException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeNotFoundException extends EntityNotFoundException {
    
    public PracticeNotFoundException() {
        super("연습을 찾을 수 없습니다", ErrorCode.PRACTICE_NOT_FOUND);
    }
    
    public PracticeNotFoundException(String message) {
        super(message, ErrorCode.PRACTICE_NOT_FOUND);
    }
}