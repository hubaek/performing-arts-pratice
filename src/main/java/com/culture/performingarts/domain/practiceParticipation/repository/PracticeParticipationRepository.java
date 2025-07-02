package com.culture.performingarts.domain.practiceParticipation.repository;

import com.culture.performingarts.domain.practiceParticipation.entity.PracticeParticipation;
import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 연습 참여 기록 데이터 접근을 위한 Repository
 * 출석/결석 관리 및 통계 기능을 제공합니다.
 */
@Repository
public interface PracticeParticipationRepository extends JpaRepository<PracticeParticipation, Long> {

    /**
     * 특정 연습의 모든 참여 기록 조회
     * 
     * @param practiceId 연습 ID
     * @return 해당 연습의 참여 기록 목록
     */
    List<PracticeParticipation> findByPracticeId(Long practiceId);

    /**
     * 특정 회원의 모든 참여 기록 조회
     * 
     * @param userId 회원 ID
     * @return 해당 회원의 참여 기록 목록
     */
    List<PracticeParticipation> findByUserId(Long userId);

    /**
     * 특정 연습에서 특정 회원의 참여 기록 조회
     * 
     * @param practiceId 연습 ID
     * @param userId 회원 ID
     * @return 참여 기록
     */
    Optional<PracticeParticipation> findByPracticeIdAndUserId(Long practiceId, Long userId);

    /**
     * 중복 참여 기록 확인
     * 
     * @param practiceId 연습 ID
     * @param userId 회원 ID
     * @return 존재 여부
     */
    boolean existsByPracticeIdAndUserId(Long practiceId, Long userId);

    /**
     * 특정 연습의 상태별 참여 기록 조회
     * 
     * @param practiceId 연습 ID
     * @param status 참석 상태
     * @return 해당 상태의 참여 기록 목록
     */
    List<PracticeParticipation> findByPracticeIdAndStatus(Long practiceId, PracticeParticipationStatus status);

    /**
     * 특정 회원의 상태별 참여 기록 조회
     * 
     * @param userId 회원 ID
     * @param status 참석 상태
     * @return 해당 상태의 참여 기록 목록
     */
    List<PracticeParticipation> findByUserIdAndStatus(Long userId, PracticeParticipationStatus status);

    /**
     * 특정 연습의 참석자 수 조회
     * 
     * @param practiceId 연습 ID
     * @param status 참석 상태
     * @return 해당 상태의 참석자 수
     */
    Long countByPracticeIdAndStatus(Long practiceId, PracticeParticipationStatus status);

    /**
     * 특정 연습의 전체 참여자 수 조회
     * 
     * @param practiceId 연습 ID
     * @return 전체 참여자 수
     */
    Long countByPracticeId(Long practiceId);

    /**
     * 특정 회원의 기간별 참여 통계 조회
     * 
     * @param userId 회원 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 참여 기록 목록
     */
    @Query("SELECT pp FROM PracticeParticipation pp " +
           "JOIN Practice p ON pp.practiceId = p.id " +
           "WHERE pp.userId = :userId AND p.practiceDate BETWEEN :startDate AND :endDate")
    List<PracticeParticipation> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /**
     * 특정 회원의 상태별 참여 횟수 조회
     * 
     * @param userId 회원 ID
     * @param status 참석 상태
     * @return 참여 횟수
     */
    Long countByUserIdAndStatus(Long userId, PracticeParticipationStatus status);

    /**
     * 미인정 결석 기록 조회
     * 
     * @param userId 회원 ID
     * @return 미인정 결석 기록 목록
     */
    @Query("SELECT pp FROM PracticeParticipation pp " +
           "WHERE pp.userId = :userId AND pp.status = 'ABSENT' AND pp.isExcused = false")
    List<PracticeParticipation> findUnexcusedAbsences(@Param("userId") Long userId);

    /**
     * 팀별 특정 기간의 참여 기록 조회
     * 통계 생성에 사용됩니다.
     * 
     * @param teamId 팀 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 참여 기록 목록
     */
    @Query("SELECT pp FROM PracticeParticipation pp " +
           "JOIN Practice p ON pp.practiceId = p.id " +
           "WHERE p.teamId = :teamId AND p.practiceDate BETWEEN :startDate AND :endDate")
    List<PracticeParticipation> findByTeamIdAndDateRange(@Param("teamId") Long teamId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /**
     * 사유가 있는 결석/지각 기록 조회
     * 
     * @return 사유가 기록된 참여 기록 목록
     */
    @Query("SELECT pp FROM PracticeParticipation pp WHERE pp.reason IS NOT NULL AND pp.reason != ''")
    List<PracticeParticipation> findParticipationsWithReason();

    /**
     * 특정 회원의 출석률 계산을 위한 통계 조회
     * 
     * @param userId 회원 ID
     * @return [총 연습 수, 출석 수, 지각 수, 결석 수]
     */
    @Query("SELECT COUNT(pp), " +
           "SUM(CASE WHEN pp.status = 'ATTENDANCE' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN pp.status = 'LATE' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN pp.status = 'ABSENT' THEN 1 ELSE 0 END) " +
           "FROM PracticeParticipation pp WHERE pp.userId = :userId")
    List<Object[]> getAttendanceStatsByUserId(@Param("userId") Long userId);
    
    // === 대시보드 통계용 메서드들 ===
    
    /**
     * 전체 출석 통계 조회
     */
    @Query("SELECT COUNT(pp), " +
           "SUM(CASE WHEN pp.status = 'ATTENDANCE' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN pp.status = 'LATE' THEN 1 ELSE 0 END) " +
           "FROM PracticeParticipation pp")
    List<Object[]> getOverallAttendanceStats();
    
    /**
     * 출석률 상위 회원 조회
     */
    @Query("SELECT pp.userId, m.name, m.department, " +
           "COUNT(pp) as totalPractices, " +
           "(SUM(CASE WHEN pp.status = 'ATTENDANCE' THEN 1 ELSE 0 END) + " +
           " SUM(CASE WHEN pp.status = 'LATE' THEN 1 ELSE 0 END)) * 100.0 / COUNT(pp) as attendanceRate " +
           "FROM PracticeParticipation pp " +
           "JOIN Member m ON pp.userId = m.id " +
           "GROUP BY pp.userId, m.name, m.department " +
           "HAVING COUNT(pp) >= 3 " +
           "ORDER BY attendanceRate DESC " +
           "LIMIT :limit")
    List<Object[]> findTopAttendanceMembers(@Param("limit") int limit);
    
    /**
     * 부서별 평균 출석률 조회
     */
    @Query("SELECT m.department, " +
           "(SUM(CASE WHEN pp.status = 'ATTENDANCE' THEN 1 ELSE 0 END) + " +
           " SUM(CASE WHEN pp.status = 'LATE' THEN 1 ELSE 0 END)) * 100.0 / COUNT(pp) as attendanceRate " +
           "FROM PracticeParticipation pp " +
           "JOIN Member m ON pp.userId = m.id " +
           "WHERE m.department IS NOT NULL " +
           "GROUP BY m.department " +
           "ORDER BY attendanceRate DESC")
    List<Object[]> findAttendanceRateByDepartment();
}
