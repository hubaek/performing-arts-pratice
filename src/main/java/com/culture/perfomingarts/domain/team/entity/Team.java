package com.culture.perfomingarts.domain.team.entity;

import com.culture.perfomingarts.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팀 엔티티
 * 공연예술단체 내의 팀이나 그룹을 관리합니다.
 * 예: 연기팀, 음악팀, 무용팀 등
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Team extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 팀명

    @Column(columnDefinition = "TEXT")
    private String description; // 팀 설명

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE"; // 팀 상태 (ACTIVE, INACTIVE, PREPARING 등)

    @Column(length = 50)
    private String leader; // 팀장명 (추후 Member와 연결 가능)

    private Integer memberCount = 0; // 팀원 수 (캐시용)

    @Builder
    public Team(String name, String description, String status, String leader) {
        this.name = name;
        this.description = description;
        this.status = status != null ? status : "ACTIVE";
        this.leader = leader;
    }

    /**
     * 팀 정보 수정
     * 
     * @param name 팀명
     * @param description 팀 설명
     * @param leader 팀장명
     */
    public void updateInfo(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }

    /**
     * 팀 상태 변경
     * 
     * @param status 새로운 상태
     */
    public void changeStatus(String status) {
        this.status = status;
    }

    /**
     * 팀원 수 업데이트
     * 실제 팀원 수와 동기화할 때 사용합니다.
     * 
     * @param count 팀원 수
     */
    public void updateMemberCount(Integer count) {
        this.memberCount = count;
    }

    /**
     * 팀 활성화
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * 팀 비활성화
     */
    public void deactivate() {
        this.status = "INACTIVE";
    }
}
