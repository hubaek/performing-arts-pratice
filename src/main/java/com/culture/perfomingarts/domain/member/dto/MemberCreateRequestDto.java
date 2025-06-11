package com.culture.perfomingarts.domain.member.dto;

import com.culture.perfomingarts.domain.member.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원 생성 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "연락처는 000-0000-0000 형식으로 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @NotNull(message = "입과년도는 필수입니다.")
    private Integer joinYear;

    private String major;

    private String department;

    private String position;

    private String responsibility;

    private String remarks;

    private Long teamId;

    @Builder
    public MemberCreateRequestDto(String name, String email, String password, String phoneNumber,
                                 LocalDate birthDate, Gender gender, Integer joinYear, String major,
                                 String department, String position, String responsibility,
                                 String remarks, Long teamId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.joinYear = joinYear;
        this.major = major;
        this.department = department;
        this.position = position;
        this.responsibility = responsibility;
        this.remarks = remarks;
        this.teamId = teamId;
    }
}
