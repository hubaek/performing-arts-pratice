package com.culture.performingarts.domain.team.repository;

import com.culture.performingarts.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 팀 데이터 접근을 위한 Repository
 * 팀 관리에 필요한 다양한 조회 기능을 제공합니다.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 팀명으로 팀 조회
     * 팀명은 고유해야 합니다.
     * 
     * @param name 팀명
     * @return 팀 정보
     */
    Optional<Team> findByName(String name);

    /**
     * 팀명 중복 확인
     * 새 팀 생성 시 사용됩니다.
     * 
     * @param name 확인할 팀명
     * @return 존재 여부
     */
    boolean existsByName(String name);

    /**
     * 상태별 팀 목록 조회
     * 
     * @param status 팀 상태 (ACTIVE, INACTIVE 등)
     * @return 해당 상태의 팀 목록
     */
    List<Team> findByStatus(String status);

    /**
     * 활성화된 팀 목록 조회
     * 
     * @return 활성 상태의 팀 목록
     */
    List<Team> findByStatusOrderByName(String status);

    /**
     * 팀장별 팀 목록 조회
     * 
     * @param leader 팀장명
     * @return 해당 팀장이 관리하는 팀 목록
     */
    List<Team> findByLeader(String leader);

    /**
     * 팀명으로 검색 (부분 일치)
     * 
     * @param keyword 검색 키워드
     * @return 검색된 팀 목록
     */
    @Query("SELECT t FROM Team t WHERE t.name LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<Team> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 멤버가 있는 팀 목록 조회
     * 
     * @return 멤버 수가 0보다 큰 팀 목록
     */
    @Query("SELECT t FROM Team t WHERE t.memberCount > 0 AND t.status = 'ACTIVE' ORDER BY t.memberCount DESC")
    List<Team> findActiveTeamsWithMembers();

    /**
     * 팀 상태별 카운트 조회
     * 통계 기능에 사용됩니다.
     * 
     * @param status 팀 상태
     * @return 해당 상태의 팀 수
     */
    Long countByStatus(String status);
    
    // === 대시보드 통계용 메서드들 ===
    
    /**
     * 팀별 상세 통계 조회
     */
    @Query("SELECT t.id, t.name, t.leader, t.memberCount, " +
           "COUNT(DISTINCT CASE WHEN m.status = 'ACTIVE' THEN m.id END) as activeMemberCount, " +
           "COUNT(DISTINCT p.id) as totalPractices, " +
           "COUNT(DISTINCT CASE WHEN p.practiceDate >= :startDate THEN p.id END) as thisMonthPractices " +
           "FROM Team t " +
           "LEFT JOIN Member m ON m.teamId = t.id " +
           "LEFT JOIN Practice p ON p.teamId = t.id " +
           "GROUP BY t.id, t.name, t.leader, t.memberCount " +
           "ORDER BY t.memberCount DESC")
    List<Object[]> findTeamDetailStats(@Param("startDate") java.time.LocalDate startDate);
}
