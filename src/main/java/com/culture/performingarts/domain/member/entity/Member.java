package com.culture.performingarts.domain.member.entity;

import com.culture.performingarts.common.entity.Timestamped;
import com.culture.performingarts.domain.member.enums.Gender;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import com.culture.performingarts.domain.member.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원 엔티티
 * 공연예술단체의 단원 정보를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다")
    @Column(nullable = false, length = 50)
    private String name; // 이름

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다")
    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일 (로그인 ID로 사용)

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    @Column(nullable = false)
    private String password; // 비밀번호 (암호화 필요)

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다")
    @Column(length = 20)
    private String phoneNumber; // 연락처

    @NotNull(message = "생년월일은 필수입니다")
    @Past(message = "생년월일은 과거 날짜여야 합니다")
    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일
    @NotNull(message = "성별은 필수입니다")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender; // 성별

    @NotNull(message = "입과년도는 필수입니다")
    @Min(value = 1900, message = "입과년도는 1900년 이후여야 합니다")
    @Max(value = 2100, message = "입과년도는 2100년 이전이어야 합니다")
    @Column(nullable = false)
    private Integer joinYear; // 입과년도

    @Size(max = 50, message = "전공은 50자를 초과할 수 없습니다")
    @Column(length = 50)
    private String major; // 전공

    @Size(max = 50, message = "소속은 50자를 초과할 수 없습니다")
    @Column(length = 50)
    private String department; // 소속 (예: 연기부, 음악부 등)

    @Size(max = 50, message = "직책은 50자를 초과할 수 없습니다")
    @Column(length = 50)
    private String position; // 직책 (예: 단장, 부단장, 총무 등)

    @Size(max = 100, message = "업무/담당은 100자를 초과할 수 없습니다")
    @Column(length = 100)
    private String responsibility; // 업무/담당

    @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다")
    @Column(columnDefinition = "TEXT")
    private String remarks; // 비고

    @Size(max = 20, message = "고유번호는 20자를 초과할 수 없습니다")
    @Column(unique = true, length = 20)
    private String uniqueCode; // 단원 고유번호

    @NotNull(message = "회원 상태는 필수입니다")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status = MemberStatus.ACTIVE; // 회원 상태

    @NotNull(message = "회원 역할은 필수입니다")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER; // 회원 역할 (일반 사용자 기본값)

    // 연관관계 매핑은 ID 참조 방식을 사용하므로 별도의 엔티티 참조는 하지 않습니다.
    private Long teamId; // 팀 ID 참조

    @Builder
    public Member(String name, String email, String password, String phoneNumber,
                  LocalDate birthDate, Gender gender, Integer joinYear, String major,
                  String department, String position, String responsibility,
                  String remarks, String uniqueCode, MemberStatus status, Role role, Long teamId) {
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
        this.uniqueCode = uniqueCode;
        this.status = status;
        this.role = role != null ? role : Role.USER;
        this.teamId = teamId;
    }

    /**
     * 회원 정보 수정
     * 비밀번호를 제외한 정보를 수정합니다.
     */
    public void updateInfo(String name, String phoneNumber, String department, 
                          String position, String responsibility, String remarks, Long teamId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.position = position;
        this.responsibility = responsibility;
        this.remarks = remarks;
        this.teamId = teamId;
    }

    /**
     * 비밀번호 변경
     * 별도의 메소드로 분리하여 보안성을 높입니다.
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * 회원 상태 변경
     */
    public void changeStatus(MemberStatus status) {
        this.status = status;
    }

    /**
     * 회원 탈퇴 처리
     * 탈퇴나 제명 시 사용합니다. 데이터는 보존하되 활동 불가 상태로 변경합니다.
     */
    public void deactivate() {
        this.status = MemberStatus.INACTIVE;
    }

    /**
     * 회원 활성화
     * 재가입이나 복귀 시 사용합니다.
     */
    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }

    /**
     * 휴단 처리
     * 일시적으로 활동을 중단하는 경우 사용합니다.
     */
    public void takeLeaveOfAbsence() {
        this.status = MemberStatus.LEAVE_OF_ABSENCE;
    }

    /**
     * 활동 중인 회원인지 확인
     * @return 활동 중이면 true
     */
    public boolean isActiveMember() {
        return this.status.isActive();
    }

    /**
     * 복귀 가능한 회원인지 확인 (휴단 상태)
     * @return 복귀 가능하면 true
     */
    public boolean canReturn() {
        return this.status.canReturn();
    }

    /**
     * 관리자 권한을 가지고 있는지 확인
     * @return 관리자 권한이 있으면 true
     */
    public boolean isAdmin() {
        return this.role.isAdmin();
    }

    /**
     * 일반 사용자 권한을 가지고 있는지 확인
     * @return 일반 사용자 권한이면 true
     */
    public boolean isUser() {
        return this.role.isUser();
    }

    /**
     * 회원 역할 변경
     * @param role 새로운 역할
     */
    public void changeRole(Role role) {
        this.role = role;
    }

    /**
     * 관리자 권한 부여
     */
    public void grantAdminRole() {
        this.role = Role.ADMIN;
    }

    /**
     * 일반 사용자 권한으로 변경
     */
    public void grantUserRole() {
        this.role = Role.USER;
    }
}
