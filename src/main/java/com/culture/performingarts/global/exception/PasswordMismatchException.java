package com.culture.performingarts.global.exception;

public class PasswordMismatchException extends BusinessException {
    
    public PasswordMismatchException() {
        super("비밀번호가 일치하지 않습니다", ErrorCode.PASSWORD_MISMATCH);
    }
    
    public PasswordMismatchException(String message) {
        super(message, ErrorCode.PASSWORD_MISMATCH);
    }
}