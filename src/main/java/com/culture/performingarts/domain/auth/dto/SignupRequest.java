package com.culture.performingarts.domain.auth.dto;

import com.culture.performingarts.domain.member.enums.Gender;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다")
    private String name;
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다")
    private String email;
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 포함해야 합니다")
    private String password;
    
    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String confirmPassword;
    
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "연락처는 000-0000-0000 형식으로 입력해주세요")
    private String phoneNumber;
    
    @NotNull(message = "생년월일은 필수입니다")
    @Past(message = "생년월일은 과거 날짜여야 합니다")
    private LocalDate birthDate;
    
    @NotNull(message = "성별은 필수입니다")
    private Gender gender;
    
    @NotNull(message = "입과년도는 필수입니다")
    @Min(value = 1900, message = "입과년도는 1900년 이후여야 합니다")
    @Max(value = 2100, message = "입과년도는 2100년 이전이어야 합니다")
    private Integer joinYear;
    
    @Size(max = 50, message = "전공은 50자를 초과할 수 없습니다")
    private String major;
    
    @Size(max = 50, message = "소속은 50자를 초과할 수 없습니다")
    private String department;
    
    @Size(max = 50, message = "직책은 50자를 초과할 수 없습니다")
    private String position;
    
    @Size(max = 100, message = "업무/담당은 100자를 초과할 수 없습니다")
    private String responsibility;
    
    @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다")
    private String remarks;
    
    @Size(max = 20, message = "고유번호는 20자를 초과할 수 없습니다")
    private String uniqueCode;
    
    private Long teamId;
    
    @Builder
    public SignupRequest(String name, String email, String password, String confirmPassword,
                        String phoneNumber, LocalDate birthDate, Gender gender, Integer joinYear,
                        String major, String department, String position, String responsibility,
                        String remarks, String uniqueCode, Long teamId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.joinYear = joinYear;
        this.major = major;
        this.department = department;
        this.position = position;
        this.responsibility = responsibility;
        this.remarks = remarks;
        this.uniqueCode = uniqueCode;
        this.teamId = teamId;
    }
    
    /**
     * 비밀번호 일치 여부 확인
     */
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}