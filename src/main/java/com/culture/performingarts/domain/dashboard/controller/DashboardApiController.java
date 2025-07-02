package com.culture.performingarts.domain.dashboard.controller;

import com.culture.performingarts.domain.dashboard.dto.*;
import com.culture.performingarts.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 대시보드 관련 API 컨트롤러
 * 통계 및 현황 정보를 제공합니다.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;

    /**
     * 대시보드 전체 현황 조회
     * 
     * @return 대시보드 전체 현황 정보
     */
    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<DashboardOverviewDto> getDashboardOverview() {
        DashboardOverviewDto overview = dashboardService.getDashboardOverview();
        return ResponseEntity.ok(overview);
    }

    /**
     * 연습 관련 통계 조회
     * 
     * @return 연습 통계 정보
     */
    @GetMapping("/practices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PracticeStatisticsDto> getPracticeStatistics() {
        PracticeStatisticsDto statistics = dashboardService.getPracticeStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 회원 관련 통계 조회
     * 
     * @return 회원 통계 정보
     */
    @GetMapping("/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberStatisticsDto> getMemberStatistics() {
        MemberStatisticsDto statistics = dashboardService.getMemberStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 팀 관련 통계 조회
     * 
     * @return 팀 통계 정보
     */
    @GetMapping("/teams")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TeamStatisticsDto> getTeamStatistics() {
        TeamStatisticsDto statistics = dashboardService.getTeamStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 트렌드 분석 통계 조회
     * 
     * @return 트렌드 통계 정보
     */
    @GetMapping("/trends")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TrendStatisticsDto> getTrendStatistics() {
        TrendStatisticsDto statistics = dashboardService.getTrendStatistics();
        return ResponseEntity.ok(statistics);
    }
}