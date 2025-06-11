package com.culture.performingarts.domain.member.dto;

import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원 목록 조회용 응답 DTO
 * 목록 조회 시 필요한 최소한의 정보만 포함
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberListResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private Integer joinYear;
    private String department;
    private String position;
    private Boolean isActive;
    private Long teamId;

    @Builder
    public MemberListResponseDto(Long id, String name, String email, String phoneNumber,
                                LocalDate birthDate, Gender gender, Integer joinYear,
                                String department, String position, Boolean isActive, Long teamId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.joinYear = joinYear;
        this.department = department;
        this.position = position;
        this.isActive = isActive;
        this.teamId = teamId;
    }

    /**
     * Member 엔티티로부터 ListResponseDto 생성
     */
    public static MemberListResponseDto from(Member member) {
        return MemberListResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .joinYear(member.getJoinYear())
                .department(member.getDepartment())
                .position(member.getPosition())
                .isActive(member.getIsActive())
                .teamId(member.getTeamId())
                .build();
    }
}
