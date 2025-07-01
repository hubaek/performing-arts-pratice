package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class InvalidMemberStatusException extends BusinessException {
    
    public InvalidMemberStatusException() {
        super("유효하지 않은 회원 상태입니다", ErrorCode.INVALID_MEMBER_STATUS);
    }
    
    public InvalidMemberStatusException(String message) {
        super(message, ErrorCode.INVALID_MEMBER_STATUS);
    }
}