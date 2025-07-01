package com.culture.performingarts.domain.team.exception;

import com.culture.performingarts.global.exception.EntityNotFoundException;
import com.culture.performingarts.global.exception.ErrorCode;

public class TeamNotFoundException extends EntityNotFoundException {
    
    public TeamNotFoundException() {
        super("팀을 찾을 수 없습니다", ErrorCode.TEAM_NOT_FOUND);
    }
    
    public TeamNotFoundException(String message) {
        super(message, ErrorCode.TEAM_NOT_FOUND);
    }
}