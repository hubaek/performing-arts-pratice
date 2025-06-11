package com.culture.performingarts.domain.member.entity;

import com.culture.performingarts.common.entity.Timestamped;
import com.culture.performingarts.domain.member.enums.Gender;
import jakarta.persistence.*;
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

    @Column(nullable = false, length = 50)
    private String name; // 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일 (로그인 ID로 사용)

    @Column(nullable = false)
    private String password; // 비밀번호 (암호화 필요)

    @Column(length = 20)
    private String phoneNumber; // 연락처

    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender; // 성별

    @Column(nullable = false)
    private Integer joinYear; // 입과년도

    @Column(length = 50)
    private String major; // 전공

    @Column(length = 50)
    private String department; // 소속 (예: 연기부, 음악부 등)

    @Column(length = 50)
    private String position; // 직책 (예: 단장, 부단장, 총무 등)

    @Column(length = 100)
    private String responsibility; // 업무/담당

    @Column(columnDefinition = "TEXT")
    private String remarks; // 비고

    @Column(nullable = false)
    private Boolean isActive = true; // 활동 상태

    // 연관관계 매핑은 ID 참조 방식을 사용하므로 별도의 엔티티 참조는 하지 않습니다.
    private Long teamId; // 팀 ID 참조

    @Builder
    public Member(String name, String email, String password, String phoneNumber, 
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
     * 회원 비활성화
     * 탈퇴나 제명 시 사용합니다. 데이터는 보존하되 활동 불가 상태로 변경합니다.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * 회원 활성화
     * 재가입이나 복귀 시 사용합니다.
     */
    public void activate() {
        this.isActive = true;
    }
}
