package com.culture.performingarts.domain.team.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class TeamNotFoundException extends BusinessException {
    public TeamNotFoundException() {
        super(ErrorCode.TEAM_NOT_FOUND);
    }
    
    public TeamNotFoundException(String message) {
        super(message, ErrorCode.TEAM_NOT_FOUND);
    }
}