package com.culture.performingarts.domain.member.repository;

import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 회원 데이터 접근을 위한 Repository
 * Spring Data JPA를 활용하여 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일로 회원 조회
     * 로그인 시 사용됩니다.
     * 
     * @param email 회원 이메일
     * @return 회원 정보
     */
    Optional<Member> findByEmail(String email);

    /**
     * 이메일 중복 확인
     * 회원가입 시 사용됩니다.
     * 
     * @param email 확인할 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 팀별 활성화된 회원 목록 조회
     * 
     * @param teamId 팀 ID
     * @return 해당 팀의 활성화된 회원 목록
     */
    List<Member> findByTeamIdAndStatus(Long teamId, MemberStatus status);

    /**
     * 부서별 회원 목록 조회
     * 
     * @param department 부서명
     * @param status 회원 상태
     * @return 해당 부서의 특정 상태 회원 목록
     */
    List<Member> findByDepartmentAndStatus(String department, MemberStatus status);

    /**
     * 입과년도별 회원 목록 조회
     * 
     * @param joinYear 입과년도
     * @param status 회원 상태
     * @return 해당 년도에 입과한 특정 상태 회원 목록
     */
    List<Member> findByJoinYearAndStatus(Integer joinYear, MemberStatus status);

    /**
     * 특정 상태의 전체 회원 목록 조회
     * 
     * @param status 회원 상태
     * @return 특정 상태의 회원 목록
     */
    List<Member> findByStatus(MemberStatus status);

    /**
     * 이름으로 회원 검색 (부분 일치)
     * 
     * @param name 검색할 이름
     * @param status 회원 상태
     * @return 이름이 일치하는 특정 상태 회원 목록
     */
    @Query("SELECT m FROM Member m WHERE m.name LIKE %:name% AND m.status = :status")
    List<Member> searchByNameAndStatus(@Param("name") String name, @Param("status") MemberStatus status);

    /**
     * 직책별 회원 목록 조회
     * 
     * @param position 직책
     * @param status 회원 상태
     * @return 해당 직책의 특정 상태 회원 목록
     */
    List<Member> findByPositionAndStatus(String position, MemberStatus status);

    /**
     * 고유코드로 회원 조회
     * 
     * @param uniqueCode 고유코드
     * @return 회원 정보
     */
    Optional<Member> findByUniqueCode(String uniqueCode);

    /**
     * 고유코드 중복 확인
     * 
     * @param uniqueCode 확인할 고유코드
     * @return 존재 여부
     */
    boolean existsByUniqueCode(String uniqueCode);
    
    // === 대시보드 통계용 메서드들 ===
    
    /**
     * 특정 상태의 회원 수 조회
     */
    Long countByStatus(MemberStatus status);
    
    /**
     * 특정 기간의 신규 가입자 수 조회
     */
    Long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 특정 시점 이전에 가입한 회원 수 조회
     */
    Long countByCreatedAtBefore(LocalDateTime dateTime);
    
    /**
     * 부서별 회원 수 조회
     */
    @Query("SELECT m.department, COUNT(m) " +
           "FROM Member m " +
           "WHERE m.status = :status " +
           "GROUP BY m.department " +
           "ORDER BY COUNT(m) DESC")
    List<Object[]> findMemberCountByDepartment(@Param("status") MemberStatus status);
    
    /**
     * 입과년도별 회원 수 조회
     */
    @Query("SELECT m.joinYear, COUNT(m) " +
           "FROM Member m " +
           "WHERE m.status = :status " +
           "GROUP BY m.joinYear " +
           "ORDER BY m.joinYear DESC")
    List<Object[]> findMemberCountByJoinYear(@Param("status") MemberStatus status);
    
    /**
     * 월별 신규 가입자 수 조회
     */
    @Query("SELECT CONCAT(YEAR(m.createdAt), '-', " +
           "CASE WHEN MONTH(m.createdAt) < 10 THEN CONCAT('0', MONTH(m.createdAt)) ELSE CAST(MONTH(m.createdAt) AS string) END) as month, " +
           "COUNT(m) " +
           "FROM Member m " +
           "WHERE m.createdAt >= :startDate " +
           "GROUP BY YEAR(m.createdAt), MONTH(m.createdAt) " +
           "ORDER BY YEAR(m.createdAt) DESC, MONTH(m.createdAt) DESC")
    List<Object[]> findMonthlyNewMemberCount(@Param("startDate") LocalDateTime startDate);
}
