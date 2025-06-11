
package com.culture.perfomingarts.domain.member.dto;

import com.culture.perfomingarts.domain.member.entity.Member;
import com.culture.perfomingarts.domain.member.enums.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 회원 응답 DTO
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private Gender gender;
    private Integer joinYear;
    private String major;
    private String department;
    private String position;
    private String responsibility;
    private String remarks;
    private Boolean isActive;
    private Long teamId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public MemberResponseDto(Long id, String name, String email, String phoneNumber,
                            LocalDate birthDate, Gender gender, Integer joinYear, String major,
                            String department, String position, String responsibility,
                            String remarks, Boolean isActive, Long teamId,
                            LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.gender = gender;
        this.joinYear = joinYear;
        this.major = major;
        this.department = department;
        this.position = position;
        this.responsibility = responsibility;
        this.remarks = remarks;
        this.isActive = isActive;
        this.teamId = teamId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    /**
     * Member 엔티티로부터 ResponseDto 생성
     */
    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .joinYear(member.getJoinYear())
                .major(member.getMajor())
                .department(member.getDepartment())
                .position(member.getPosition())
                .responsibility(member.getResponsibility())
                .remarks(member.getRemarks())
                .isActive(member.getIsActive())
                .teamId(member.getTeamId())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getUpdatedAt())
                .build();
    }
}
