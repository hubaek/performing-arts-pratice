package com.culture.perfomingarts.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdateRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "연락처는 000-0000-0000 형식으로 입력해주세요.")
    private String phoneNumber;

    private String department;

    private String position;

    private String responsibility;

    private String remarks;

    private Long teamId;

    @Builder
    public MemberUpdateRequestDto(String name, String phoneNumber, String department,
                                 String position, String responsibility, String remarks, Long teamId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.position = position;
        this.responsibility = responsibility;
        this.remarks = remarks;
        this.teamId = teamId;
    }
}
