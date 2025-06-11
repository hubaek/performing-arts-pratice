package com.culture.perfomingarts.domain.practice.repository;

import com.culture.perfomingarts.domain.practice.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 연습 일지 데이터 접근을 위한 Repository
 * 연습 기록 조회 및 통계 기능을 제공합니다.
 */
@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    /**
     * 특정 기간의 연습 목록 조회
     * 
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 해당 기간의 연습 목록
     */
    @Query("SELECT p FROM Practice p WHERE p.practiceDate BETWEEN :startDate AND :endDate ORDER BY p.practiceDate DESC, p.startTime DESC")
    List<Practice> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 팀별 연습 목록 조회
     * 
     * @param teamId 팀 ID
     * @return 해당 팀의 연습 목록
     */
    List<Practice> findByTeamIdOrderByPracticeDateDesc(Long teamId);

    /**
     * 작성자별 연습 목록 조회
     * 
     * @param userId 작성자 ID
     * @return 해당 작성자가 작성한 연습 목록
     */
    List<Practice> findByUserIdOrderByPracticeDateDesc(Long userId);

    /**
     * 특정 날짜의 연습 목록 조회
     * 
     * @param date 조회할 날짜
     * @return 해당 날짜의 연습 목록
     */
    List<Practice> findByPracticeDateOrderByStartTime(LocalDate date);

    /**
     * 완료된 연습 목록 조회
     * 
     * @param isCompleted 완료 여부
     * @return 완료/미완료 연습 목록
     */
    List<Practice> findByIsCompletedOrderByPracticeDateDesc(Boolean isCompleted);

    /**
     * 팀별 특정 기간의 연습 목록 조회
     * 
     * @param teamId 팀 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 조건에 맞는 연습 목록
     */
    @Query("SELECT p FROM Practice p WHERE p.teamId = :teamId AND p.practiceDate BETWEEN :startDate AND :endDate ORDER BY p.practiceDate DESC")
    List<Practice> findByTeamAndDateRange(@Param("teamId") Long teamId, 
                                         @Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);

    /**
     * 장소별 연습 목록 조회
     * 
     * @param location 장소
     * @return 해당 장소의 연습 목록
     */
    List<Practice> findByLocationOrderByPracticeDateDesc(String location);

    /**
     * 제목 또는 내용으로 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색된 연습 목록
     */
    @Query("SELECT p FROM Practice p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% ORDER BY p.practiceDate DESC")
    List<Practice> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 팀별 연습 횟수 조회 (특정 기간)
     * 
     * @param teamId 팀 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 연습 횟수
     */
    @Query("SELECT COUNT(p) FROM Practice p WHERE p.teamId = :teamId AND p.practiceDate BETWEEN :startDate AND :endDate")
    Long countByTeamAndDateRange(@Param("teamId") Long teamId, 
                                @Param("startDate") LocalDate startDate, 
                                @Param("endDate") LocalDate endDate);

    /**
     * 월별 연습 통계 조회
     * 
     * @param year 년도
     * @param month 월
     * @return 해당 월의 연습 목록
     */
    @Query("SELECT p FROM Practice p WHERE YEAR(p.practiceDate) = :year AND MONTH(p.practiceDate) = :month ORDER BY p.practiceDate")
    List<Practice> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    /**
     * 출석률이 특정 값 이하인 연습 조회
     * 낮은 출석률의 연습을 파악하는데 사용됩니다.
     * 
     * @param rate 출석률 기준
     * @return 기준 이하의 출석률을 가진 연습 목록
     */
    @Query("SELECT p FROM Practice p WHERE p.isCompleted = true AND ((p.presentCount + p.lateCount) * 100.0 / p.totalParticipants) <= :rate")
    List<Practice> findLowAttendancePractices(@Param("rate") double rate);
}
