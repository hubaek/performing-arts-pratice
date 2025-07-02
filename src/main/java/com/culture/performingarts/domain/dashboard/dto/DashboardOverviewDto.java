package com.culture.performingarts.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 대시보드 전체 현황 통계 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class DashboardOverviewDto {
    
    // 전체 현황
    private final Long totalMembers;
    private final Long activeMembers;
    private final Long leaveOfAbsenceMembers;
    private final Long inactiveMembers;
    
    // 팀 현황
    private final Long totalTeams;
    private final Long activeTeams;
    
    // 연습 현황
    private final Long totalPractices;
    private final Long thisMonthPractices;
    private final Long completedPractices;
    private final Double overallAttendanceRate;
    
    // 이번 달 신규 가입자
    private final Long newMembersThisMonth;
}