package com.culture.performingarts.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(405, "C002", "지원하지 않는 HTTP 메서드입니다"),
    ENTITY_NOT_FOUND(404, "C003", "엔터티를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 에러입니다"),
    ACCESS_DENIED(403, "C005", "접근 권한이 없습니다"),
    
    // Auth Domain
    INVALID_LOGIN_CREDENTIALS(401, "A001", "잘못된 로그인 정보입니다"),
    INVALID_TOKEN(401, "A002", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(401, "A003", "만료된 토큰입니다"),
    UNAUTHORIZED(401, "A004", "인증이 필요합니다"),
    
    // Member Domain
    EMAIL_DUPLICATE(400, "M001", "이미 사용중인 이메일입니다"),
    MEMBER_NOT_FOUND(404, "M002", "회원을 찾을 수 없습니다"),
    INVALID_MEMBER_STATUS(400, "M003", "유효하지 않은 회원 상태입니다"),
    PASSWORD_MISMATCH(400, "M004", "비밀번호가 일치하지 않습니다"),
    
    // Practice Domain
    PRACTICE_NOT_FOUND(404, "P001", "연습을 찾을 수 없습니다"),
    PRACTICE_ALREADY_COMPLETED(400, "P002", "이미 완료된 연습입니다"),
    INVALID_PRACTICE_DATE(400, "P003", "유효하지 않은 연습 날짜입니다"),
    PRACTICE_PERMISSION_DENIED(403, "P004", "연습에 대한 권한이 없습니다"),
    
    // Team Domain
    TEAM_NOT_FOUND(404, "T001", "팀을 찾을 수 없습니다"),
    TEAM_NAME_DUPLICATE(400, "T002", "이미 사용중인 팀 이름입니다"),
    TEAM_MEMBER_LIMIT_EXCEEDED(400, "T003", "팀 멤버 수 제한을 초과했습니다"),
    
    // Practice Participation Domain
    PARTICIPATION_NOT_FOUND(404, "PP001", "참여 정보를 찾을 수 없습니다"),
    ALREADY_PARTICIPATED(400, "PP002", "이미 참여한 연습입니다"),
    PARTICIPATION_DEADLINE_PASSED(400, "PP003", "참여 신청 기한이 지났습니다");
    
    private final int status;
    private final String code;
    private final String message;
}