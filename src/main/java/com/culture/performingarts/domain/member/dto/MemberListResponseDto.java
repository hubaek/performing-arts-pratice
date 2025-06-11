package com.culture.performingarts.domain.member.dto;

import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.Gender;
import com.culture.performingarts.domain.member.enums.MemberStatus;
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
    private String uniqueCode;
    private MemberStatus status;
    private Long teamId;

    @Builder
    public MemberListResponseDto(Long id, String name, String email, String phoneNumber,
                                LocalDate birthDate, Gender gender, Integer joinYear,
                                String department, String position, String uniqueCode, MemberStatus status, Long teamId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.joinYear = joinYear;
        this.department = department;
        this.position = position;
        this.uniqueCode = uniqueCode;
        this.status = status;
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
                .uniqueCode(member.getUniqueCode())
                .status(member.getStatus())
                .teamId(member.getTeamId())
                .build();
    }
}
