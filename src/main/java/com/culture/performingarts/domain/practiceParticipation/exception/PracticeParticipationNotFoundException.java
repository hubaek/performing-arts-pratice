package com.culture.performingarts.domain.practiceParticipation.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeParticipationNotFoundException extends BusinessException {
    
    public PracticeParticipationNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}