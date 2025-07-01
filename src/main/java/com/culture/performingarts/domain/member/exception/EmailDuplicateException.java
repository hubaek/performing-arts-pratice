package com.culture.performingarts.domain.member.exception;

import com.culture.performingarts.global.exception.DuplicateResourceException;
import com.culture.performingarts.global.exception.ErrorCode;

public class EmailDuplicateException extends DuplicateResourceException {
    
    public EmailDuplicateException() {
        super("이미 사용중인 이메일입니다", ErrorCode.EMAIL_DUPLICATE);
    }
    
    public EmailDuplicateException(String message) {
        super(message, ErrorCode.EMAIL_DUPLICATE);
    }
}