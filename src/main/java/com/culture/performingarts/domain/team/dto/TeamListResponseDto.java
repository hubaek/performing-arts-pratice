package com.culture.performingarts.domain.team.dto;

import com.culture.performingarts.domain.team.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamListResponseDto {

    private Long id;
    private String name;
    private String status;
    private String leader;
    private Integer memberCount;

    @Builder
    public TeamListResponseDto(Long id, String name, String status, String leader, Integer memberCount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.leader = leader;
        this.memberCount = memberCount;
    }

    public static TeamListResponseDto fromEntity(Team team) {
        return TeamListResponseDto.builder()
                .id(team.getId())
                .name(team.getName())
                .status(team.getStatus())
                .leader(team.getLeader())
                .memberCount(team.getMemberCount())
                .build();
    }
}