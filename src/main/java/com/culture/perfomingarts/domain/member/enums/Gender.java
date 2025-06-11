package com.culture.perfomingarts.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 성별을 나타내는 열거형
 * 데이터베이스에 저장될 때는 문자열 형태로 저장됩니다.
 */
@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타");

    private final String description;
}
