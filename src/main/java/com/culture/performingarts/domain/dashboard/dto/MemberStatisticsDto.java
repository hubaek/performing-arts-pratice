package com.culture.performingarts.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 회원 관련 통계 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class MemberStatisticsDto {
    
    // 회원 상태별 분포
    private final Long activeMemberCount;
    private final Long leaveOfAbsenceMemberCount;
    private final Long inactiveMemberCount;
    
    // 부서별 분포
    private final List<DepartmentMemberCountDto> membersByDepartment;
    
    // 입과년도별 분포
    private final List<JoinYearMemberCountDto> membersByJoinYear;
    
    // 월별 신규 가입자 (최근 6개월)
    private final List<MonthlyNewMemberDto> monthlyNewMembers;
    
    // 출석률 상위 회원 (상위 10명)
    private final List<TopAttendanceMemberDto> topAttendanceMembers;
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class DepartmentMemberCountDto {
        private final String department;
        private final Long memberCount;
        private final Double averageAttendanceRate;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class JoinYearMemberCountDto {
        private final Integer joinYear;
        private final Long memberCount;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class MonthlyNewMemberDto {
        private final String month; // YYYY-MM 형식
        private final Long newMemberCount;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class TopAttendanceMemberDto {
        private final Long memberId;
        private final String memberName;
        private final String department;
        private final Double attendanceRate;
        private final Long totalPractices;
    }
}