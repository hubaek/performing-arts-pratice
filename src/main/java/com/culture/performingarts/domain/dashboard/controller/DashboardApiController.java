package com.culture.performingarts.domain.dashboard.controller;

import com.culture.performingarts.domain.dashboard.dto.*;
import com.culture.performingarts.domain.dashboard.service.DashboardService;
import com.culture.performingarts.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dashboard", description = "대시보드 API")
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
    @Operation(summary = "대시보드 전체 현황 조회", description = "회원, 팀, 연습 등의 전체 현황을 조회합니다.")
    @GetMapping("/overview")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<DashboardOverviewDto>> getDashboardOverview() {
        DashboardOverviewDto overview = dashboardService.getDashboardOverview();
        return ResponseEntity.ok(ApiResponse.success(overview));
    }

    /**
     * 연습 관련 통계 조회
     * 
     * @return 연습 통계 정보
     */
    @Operation(summary = "연습 통계 조회", description = "연습 관련 상세 통계를 조회합니다.")
    @GetMapping("/practices")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<PracticeStatisticsDto>> getPracticeStatistics() {
        PracticeStatisticsDto statistics = dashboardService.getPracticeStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    /**
     * 회원 관련 통계 조회
     * 
     * @return 회원 통계 정보
     */
    @Operation(summary = "회원 통계 조회", description = "회원 관련 상세 통계를 조회합니다.")
    @GetMapping("/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MemberStatisticsDto>> getMemberStatistics() {
        MemberStatisticsDto statistics = dashboardService.getMemberStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    /**
     * 팀 관련 통계 조회
     * 
     * @return 팀 통계 정보
     */
    @Operation(summary = "팀 통계 조회", description = "팀 관련 상세 통계를 조회합니다.")
    @GetMapping("/teams")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ApiResponse<TeamStatisticsDto>> getTeamStatistics() {
        TeamStatisticsDto statistics = dashboardService.getTeamStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    /**
     * 트렌드 분석 통계 조회
     * 
     * @return 트렌드 통계 정보
     */
    @Operation(summary = "트렌드 분석 조회", description = "월별/주별 트렌드 분석 정보를 조회합니다.")
    @GetMapping("/trends")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TrendStatisticsDto>> getTrendStatistics() {
        TrendStatisticsDto statistics = dashboardService.getTrendStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}