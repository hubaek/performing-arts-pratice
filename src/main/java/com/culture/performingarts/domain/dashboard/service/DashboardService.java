package com.culture.performingarts.domain.dashboard.service;

import com.culture.performingarts.domain.dashboard.dto.*;
import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import com.culture.performingarts.domain.member.repository.MemberRepository;
import com.culture.performingarts.domain.practice.repository.PracticeRepository;
import com.culture.performingarts.domain.practiceParticipation.repository.PracticeParticipationRepository;
import com.culture.performingarts.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    
    private final MemberRepository memberRepository;
    private final PracticeRepository practiceRepository;
    private final PracticeParticipationRepository participationRepository;
    private final TeamRepository teamRepository;
    
    /**
     * 대시보드 전체 현황 조회
     */
    public DashboardOverviewDto getDashboardOverview() {
        // 회원 현황
        Long totalMembers = memberRepository.count();
        Long activeMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);
        Long leaveOfAbsenceMembers = memberRepository.countByStatus(MemberStatus.LEAVE_OF_ABSENCE);
        Long inactiveMembers = memberRepository.countByStatus(MemberStatus.INACTIVE);
        
        // 팀 현황
        Long totalTeams = teamRepository.count();
        // 팀 상태가 없다면 전체를 활성으로 간주
        Long activeTeams = totalTeams;
        
        // 연습 현황
        Long totalPractices = practiceRepository.count();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        Long thisMonthPractices = practiceRepository.countByPracticeDateBetween(startOfMonth, endOfMonth);
        Long completedPractices = practiceRepository.countByIsCompletedTrue();
        
        // 전체 출석률 계산
        Double overallAttendanceRate = calculateOverallAttendanceRate();
        
        // 이번 달 신규 가입자
        LocalDateTime startOfMonthDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endOfMonthDateTime = endOfMonth.atTime(23, 59, 59);
        Long newMembersThisMonth = memberRepository.countByCreatedAtBetween(startOfMonthDateTime, endOfMonthDateTime);
        
        return DashboardOverviewDto.builder()
                .totalMembers(totalMembers)
                .activeMembers(activeMembers)
                .leaveOfAbsenceMembers(leaveOfAbsenceMembers)
                .inactiveMembers(inactiveMembers)
                .totalTeams(totalTeams)
                .activeTeams(activeTeams)
                .totalPractices(totalPractices)
                .thisMonthPractices(thisMonthPractices)
                .completedPractices(completedPractices)
                .overallAttendanceRate(overallAttendanceRate)
                .newMembersThisMonth(newMembersThisMonth)
                .build();
    }
    
    /**
     * 연습 관련 통계 조회
     */
    public PracticeStatisticsDto getPracticeStatistics() {
        // 기본 통계
        Long totalPractices = practiceRepository.count();
        Long completedPractices = practiceRepository.countByIsCompletedTrue();
        Double completionRate = totalPractices > 0 ? (double) completedPractices / totalPractices * 100 : 0.0;
        Double averageAttendanceRate = calculateOverallAttendanceRate();
        
        // 월별 연습 수 (최근 6개월)
        List<PracticeStatisticsDto.MonthlyPracticeCountDto> monthlyPracticeCounts = 
                getMonthlyPracticeCounts(6);
        
        // 장소별 연습 분포
        List<PracticeStatisticsDto.LocationPracticeCountDto> practicesByLocation = 
                getPracticesByLocation();
        
        return PracticeStatisticsDto.builder()
                .totalPractices(totalPractices)
                .completedPractices(completedPractices)
                .completionRate(completionRate)
                .averageAttendanceRate(averageAttendanceRate)
                .monthlyPracticeCounts(monthlyPracticeCounts)
                .practicesByLocation(practicesByLocation)
                .build();
    }
    
    /**
     * 회원 관련 통계 조회
     */
    public MemberStatisticsDto getMemberStatistics() {
        // 회원 상태별 분포
        Long activeMemberCount = memberRepository.countByStatus(MemberStatus.ACTIVE);
        Long leaveOfAbsenceMemberCount = memberRepository.countByStatus(MemberStatus.LEAVE_OF_ABSENCE);
        Long inactiveMemberCount = memberRepository.countByStatus(MemberStatus.INACTIVE);
        
        // 부서별 분포
        List<MemberStatisticsDto.DepartmentMemberCountDto> membersByDepartment = 
                getMembersByDepartment();
        
        // 입과년도별 분포
        List<MemberStatisticsDto.JoinYearMemberCountDto> membersByJoinYear = 
                getMembersByJoinYear();
        
        // 월별 신규 가입자
        List<MemberStatisticsDto.MonthlyNewMemberDto> monthlyNewMembers = 
                getMonthlyNewMembers(6);
        
        // 출석률 상위 회원
        List<MemberStatisticsDto.TopAttendanceMemberDto> topAttendanceMembers = 
                getTopAttendanceMembers(10);
        
        return MemberStatisticsDto.builder()
                .activeMemberCount(activeMemberCount)
                .leaveOfAbsenceMemberCount(leaveOfAbsenceMemberCount)
                .inactiveMemberCount(inactiveMemberCount)
                .membersByDepartment(membersByDepartment)
                .membersByJoinYear(membersByJoinYear)
                .monthlyNewMembers(monthlyNewMembers)
                .topAttendanceMembers(topAttendanceMembers)
                .build();
    }
    
    /**
     * 팀 관련 통계 조회
     */
    public TeamStatisticsDto getTeamStatistics() {
        Long totalTeams = teamRepository.count();
        Long activeTeams = totalTeams; // 팀 상태가 없다면 전체를 활성으로 간주
        
        // 평균 팀 크기 계산
        Double averageTeamSize = calculateAverageTeamSize();
        
        // 팀별 상세 통계
        List<TeamStatisticsDto.TeamDetailStatDto> teamStats = getTeamDetailStats();
        
        return TeamStatisticsDto.builder()
                .totalTeams(totalTeams)
                .activeTeams(activeTeams)
                .averageTeamSize(averageTeamSize)
                .teamStats(teamStats)
                .build();
    }
    
    /**
     * 트렌드 분석 통계 조회
     */
    public TrendStatisticsDto getTrendStatistics() {
        // 월별 트렌드 (최근 12개월)
        List<TrendStatisticsDto.MonthlyTrendDto> monthlyTrends = getMonthlyTrends(12);
        
        // 주별 트렌드 (최근 8주)
        List<TrendStatisticsDto.WeeklyTrendDto> weeklyTrends = getWeeklyTrends(8);
        
        return TrendStatisticsDto.builder()
                .monthlyTrends(monthlyTrends)
                .weeklyTrends(weeklyTrends)
                .build();
    }
    
    // === private helper methods ===
    
    private Double calculateOverallAttendanceRate() {
        List<Object[]> stats = participationRepository.getOverallAttendanceStats();
        if (stats.isEmpty() || stats.get(0)[0] == null) {
            return 0.0;
        }
        
        Object[] result = stats.get(0);
        Long totalParticipations = ((Number) result[0]).longValue();
        Long attendanceCount = ((Number) result[1]).longValue();
        Long lateCount = ((Number) result[2]).longValue();
        
        return totalParticipations > 0 
            ? ((double) (attendanceCount + lateCount) / totalParticipations) * 100 
            : 0.0;
    }
    
    private List<PracticeStatisticsDto.MonthlyPracticeCountDto> getMonthlyPracticeCounts(int monthCount) {
        LocalDate startDate = LocalDate.now().minusMonths(monthCount);
        List<Object[]> results = practiceRepository.findMonthlyPracticeCount(startDate);
        
        return results.stream()
                .map(result -> PracticeStatisticsDto.MonthlyPracticeCountDto.builder()
                        .month((String) result[0])
                        .practiceCount(((Number) result[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<PracticeStatisticsDto.LocationPracticeCountDto> getPracticesByLocation() {
        List<Object[]> results = practiceRepository.findPracticeCountAndAttendanceRateByLocation();
        
        return results.stream()
                .map(result -> PracticeStatisticsDto.LocationPracticeCountDto.builder()
                        .location((String) result[0])
                        .practiceCount(((Number) result[1]).longValue())
                        .averageAttendanceRate(((Number) result[2]).doubleValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<MemberStatisticsDto.DepartmentMemberCountDto> getMembersByDepartment() {
        List<Object[]> results = memberRepository.findMemberCountByDepartment(MemberStatus.ACTIVE);
        List<Object[]> attendanceRates = participationRepository.findAttendanceRateByDepartment();
        
        return results.stream()
                .map(result -> {
                    String department = (String) result[0];
                    Double attendanceRate = attendanceRates.stream()
                            .filter(ar -> department.equals(ar[0]))
                            .map(ar -> ((Number) ar[1]).doubleValue())
                            .findFirst()
                            .orElse(0.0);
                    
                    return MemberStatisticsDto.DepartmentMemberCountDto.builder()
                            .department(department)
                            .memberCount(((Number) result[1]).longValue())
                            .averageAttendanceRate(attendanceRate)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private List<MemberStatisticsDto.JoinYearMemberCountDto> getMembersByJoinYear() {
        List<Object[]> results = memberRepository.findMemberCountByJoinYear(MemberStatus.ACTIVE);
        
        return results.stream()
                .map(result -> MemberStatisticsDto.JoinYearMemberCountDto.builder()
                        .joinYear(((Number) result[0]).intValue())
                        .memberCount(((Number) result[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<MemberStatisticsDto.MonthlyNewMemberDto> getMonthlyNewMembers(int monthCount) {
        LocalDateTime startDate = LocalDate.now().minusMonths(monthCount).atStartOfDay();
        List<Object[]> results = memberRepository.findMonthlyNewMemberCount(startDate);
        
        return results.stream()
                .map(result -> MemberStatisticsDto.MonthlyNewMemberDto.builder()
                        .month((String) result[0])
                        .newMemberCount(((Number) result[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<MemberStatisticsDto.TopAttendanceMemberDto> getTopAttendanceMembers(int limit) {
        List<Object[]> results = participationRepository.findTopAttendanceMembers(limit);
        
        return results.stream()
                .map(result -> MemberStatisticsDto.TopAttendanceMemberDto.builder()
                        .memberId(((Number) result[0]).longValue())
                        .memberName((String) result[1])
                        .department((String) result[2])
                        .totalPractices(((Number) result[3]).longValue())
                        .attendanceRate(((Number) result[4]).doubleValue())
                        .build())
                .collect(Collectors.toList());
    }
    
    private Double calculateAverageTeamSize() {
        Long totalMembers = memberRepository.countByStatus(MemberStatus.ACTIVE);
        Long totalTeams = teamRepository.count();
        return totalTeams > 0 ? (double) totalMembers / totalTeams : 0.0;
    }
    
    private List<TeamStatisticsDto.TeamDetailStatDto> getTeamDetailStats() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        List<Object[]> results = teamRepository.findTeamDetailStats(startOfMonth);
        
        return results.stream()
                .map(result -> {
                    // 팀별 평균 출석률은 별도 계산 필요
                    Double avgAttendanceRate = calculateTeamAttendanceRate(((Number) result[0]).longValue());
                    
                    return TeamStatisticsDto.TeamDetailStatDto.builder()
                            .teamId(((Number) result[0]).longValue())
                            .teamName((String) result[1])
                            .leader((String) result[2])
                            .memberCount(((Number) result[3]).longValue())
                            .activeMemberCount(((Number) result[4]).longValue())
                            .totalPractices(((Number) result[5]).longValue())
                            .thisMonthPractices(((Number) result[6]).longValue())
                            .averageAttendanceRate(avgAttendanceRate)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private Double calculateTeamAttendanceRate(Long teamId) {
        // 팀별 평균 출석률 계산 로직
        // 실제 구현에서는 팀 내 연습의 출석률을 계산해야 함
        return 85.0; // 임시 값
    }
    
    private List<TrendStatisticsDto.MonthlyTrendDto> getMonthlyTrends(int monthCount) {
        LocalDate startDate = LocalDate.now().minusMonths(monthCount);
        List<Object[]> practiceStats = practiceRepository.findMonthlyTrendStats(startDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        List<Object[]> memberStats = memberRepository.findMonthlyNewMemberCount(startDateTime);
        
        return practiceStats.stream()
                .map(result -> {
                    String month = (String) result[0];
                    Long newMemberCount = memberStats.stream()
                            .filter(ms -> month.equals(ms[0]))
                            .map(ms -> ((Number) ms[1]).longValue())
                            .findFirst()
                            .orElse(0L);
                    
                    return TrendStatisticsDto.MonthlyTrendDto.builder()
                            .month(month)
                            .practiceCount(((Number) result[1]).longValue())
                            .completedPracticeCount(((Number) result[2]).longValue())
                            .averageAttendanceRate(result[3] != null ? ((Number) result[3]).doubleValue() : 0.0)
                            .newMemberCount(newMemberCount)
                            .memberCount(0L) // 전체 멤버 수는 별도 쿼리 필요
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    private List<TrendStatisticsDto.WeeklyTrendDto> getWeeklyTrends(int weekCount) {
        LocalDate startDate = LocalDate.now().minusWeeks(weekCount);
        List<Object[]> results = practiceRepository.findWeeklyPracticeStats(startDate);
        
        return results.stream()
                .map(result -> {
                    // YEARWEEK 결과를 YYYY-WW 형식으로 변환
                    String yearWeek = String.valueOf(result[0]);
                    String formattedWeek = yearWeek.substring(0, 4) + "-" + 
                                          String.format("%02d", Integer.parseInt(yearWeek.substring(4)));
                    
                    return TrendStatisticsDto.WeeklyTrendDto.builder()
                            .week(formattedWeek)
                            .practiceCount(((Number) result[1]).longValue())
                            .averageAttendanceRate(result[2] != null ? ((Number) result[2]).doubleValue() : 0.0)
                            .totalParticipants(0L) // 주별 총 참여자는 별도 계산 필요
                            .build();
                })
                .collect(Collectors.toList());
    }
}