package com.culture.performingarts.domain.member.exception;

import com.culture.performingarts.global.exception.EntityNotFoundException;
import com.culture.performingarts.global.exception.ErrorCode;

public class MemberNotFoundException extends EntityNotFoundException {
    
    public MemberNotFoundException() {
        super("회원을 찾을 수 없습니다", ErrorCode.MEMBER_NOT_FOUND);
    }
    
    public MemberNotFoundException(String message) {
        super(message, ErrorCode.MEMBER_NOT_FOUND);
    }
}