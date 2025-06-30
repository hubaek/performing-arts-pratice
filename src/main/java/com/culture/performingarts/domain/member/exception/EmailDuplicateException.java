package com.culture.performingarts.domain.member.exception;

import com.culture.performingarts.global.exception.BusinessException;
import com.culture.performingarts.global.exception.ErrorCode;

public class EmailDuplicateException extends BusinessException {
    public EmailDuplicateException() {
        super(ErrorCode.EMAIL_DUPLICATE);
    }
    
    public EmailDuplicateException(String message) {
        super(message, ErrorCode.EMAIL_DUPLICATE);
    }
}