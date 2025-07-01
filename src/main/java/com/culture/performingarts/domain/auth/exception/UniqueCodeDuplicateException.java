package com.culture.performingarts.domain.auth.exception;

import com.culture.performingarts.global.exception.DuplicateResourceException;
import com.culture.performingarts.global.exception.ErrorCode;

public class UniqueCodeDuplicateException extends DuplicateResourceException {
    
    public UniqueCodeDuplicateException() {
        super("이미 사용중인 고유번호입니다", ErrorCode.INVALID_INPUT_VALUE);
    }
    
    public UniqueCodeDuplicateException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}