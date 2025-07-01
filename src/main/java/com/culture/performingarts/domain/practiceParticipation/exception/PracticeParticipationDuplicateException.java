package com.culture.performingarts.domain.practiceParticipation.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeParticipationDuplicateException extends BusinessException {
    
    public PracticeParticipationDuplicateException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}