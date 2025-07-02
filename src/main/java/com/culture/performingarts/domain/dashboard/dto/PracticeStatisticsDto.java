package com.culture.performingarts.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * 연습 관련 통계 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class PracticeStatisticsDto {
    
    // 기본 통계
    private final Long totalPractices;
    private final Long completedPractices;
    private final Double completionRate;
    private final Double averageAttendanceRate;
    
    // 월별 연습 수 (최근 6개월)
    private final List<MonthlyPracticeCountDto> monthlyPracticeCounts;
    
    // 장소별 연습 분포
    private final List<LocationPracticeCountDto> practicesByLocation;
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class MonthlyPracticeCountDto {
        private final String month; // YYYY-MM 형식
        private final Long practiceCount;
        private final Long completedCount;
        private final Double averageAttendanceRate;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    public static class LocationPracticeCountDto {
        private final String location;
        private final Long practiceCount;
        private final Double averageAttendanceRate;
    }
}