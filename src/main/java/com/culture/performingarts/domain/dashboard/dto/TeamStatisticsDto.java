package com.culture.performingarts.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 팀 관련 통계 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class TeamStatisticsDto {
    
    // 전체 팀 통계
    private final Long totalTeams;
    private final Long activeTeams;
    private final Double averageTeamSize;
    
    // 팀별 상세 통계
    private final List<TeamDetailStatDto> teamStats;
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class TeamDetailStatDto {
        private final Long teamId;
        private final String teamName;
        private final String leader;
        private final Long memberCount;
        private final Long activeMemberCount;
        private final Long totalPractices;
        private final Double averageAttendanceRate;
        private final Long thisMonthPractices;
    }
}