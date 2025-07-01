package com.culture.performingarts.domain.practiceParticipation.exception;

import com.culture.performingarts.global.exception.EntityNotFoundException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeParticipationNotFoundException extends EntityNotFoundException {
    
    public PracticeParticipationNotFoundException() {
        super("참여 정보를 찾을 수 없습니다", ErrorCode.PARTICIPATION_NOT_FOUND);
    }
    
    public PracticeParticipationNotFoundException(String message) {
        super(message, ErrorCode.PARTICIPATION_NOT_FOUND);
    }
}