package com.culture.performingarts.domain.practiceParticipation.exception;

import com.culture.performingarts.global.exception.DuplicateResourceException;
import com.culture.performingarts.global.exception.ErrorCode;

public class PracticeParticipationDuplicateException extends DuplicateResourceException {
    
    public PracticeParticipationDuplicateException() {
        super("이미 참여한 연습입니다", ErrorCode.ALREADY_PARTICIPATED);
    }
    
    public PracticeParticipationDuplicateException(String message) {
        super(message, ErrorCode.ALREADY_PARTICIPATED);
    }
}