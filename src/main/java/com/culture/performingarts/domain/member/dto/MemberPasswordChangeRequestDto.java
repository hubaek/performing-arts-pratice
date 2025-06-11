package com.culture.performingarts.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 비밀번호 변경 요청 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPasswordChangeRequestDto {

    @NotBlank(message = "현재 비밀번호는 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    @Builder
    public MemberPasswordChangeRequestDto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    /**
     * 새 비밀번호와 확인 비밀번호가 일치하는지 검증
     */
    public boolean isPasswordMatched() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
