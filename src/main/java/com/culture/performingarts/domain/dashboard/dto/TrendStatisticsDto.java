package com.culture.performingarts.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 트렌드 분석 통계 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class TrendStatisticsDto {
    
    // 월별 트렌드 (최근 12개월)
    private final List<MonthlyTrendDto> monthlyTrends;
    
    // 주별 트렌드 (최근 8주)
    private final List<WeeklyTrendDto> weeklyTrends;
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class MonthlyTrendDto {
        private final String month; // YYYY-MM 형식
        private final Long practiceCount;
        private final Long memberCount;
        private final Double averageAttendanceRate;
        private final Long newMemberCount;
        private final Long completedPracticeCount;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class WeeklyTrendDto {
        private final String week; // YYYY-WW 형식
        private final Long practiceCount;
        private final Double averageAttendanceRate;
        private final Long totalParticipants;
    }
}