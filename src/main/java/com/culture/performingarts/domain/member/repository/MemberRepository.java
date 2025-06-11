package com.culture.performingarts.domain.member.repository;

import com.culture.performingarts.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    List<Member> findByTeamIdAndIsActiveTrue(Long teamId);

    /**
     * 부서별 회원 목록 조회
     * 
     * @param department 부서명
     * @return 해당 부서의 활성화된 회원 목록
     */
    List<Member> findByDepartmentAndIsActiveTrue(String department);

    /**
     * 입과년도별 회원 목록 조회
     * 
     * @param joinYear 입과년도
     * @return 해당 년도에 입과한 활성화된 회원 목록
     */
    List<Member> findByJoinYearAndIsActiveTrue(Integer joinYear);

    /**
     * 활성화된 전체 회원 목록 조회
     * 
     * @return 활성화된 회원 목록
     */
    List<Member> findByIsActiveTrue();

    /**
     * 이름으로 회원 검색 (부분 일치)
     * 
     * @param name 검색할 이름
     * @return 이름이 일치하는 활성화된 회원 목록
     */
    @Query("SELECT m FROM Member m WHERE m.name LIKE %:name% AND m.isActive = true")
    List<Member> searchByName(@Param("name") String name);

    /**
     * 직책별 회원 목록 조회
     * 
     * @param position 직책
     * @return 해당 직책의 활성화된 회원 목록
     */
    List<Member> findByPositionAndIsActiveTrue(String position);
}
