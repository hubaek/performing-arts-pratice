package com.culture.performingarts.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String code;
    private List<FieldErrorDetail> errors;
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(Collections.emptyList())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(FieldErrorDetail.of(bindingResult))
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .message(message)
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(Collections.emptyList())
                .build();
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String value;
        private String reason;
        
        public static List<FieldErrorDetail> of(BindingResult bindingResult) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> FieldErrorDetail.builder()
                            .field(error.getField())
                            .value(error.getRejectedValue() != null ? error.getRejectedValue().toString() : "")
                            .reason(error.getDefaultMessage())
                            .build())
                    .collect(Collectors.toList());
        }
    }
}