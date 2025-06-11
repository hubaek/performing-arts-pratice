package com.culture.performingarts.domain.team.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateRequestDto {

    private String name;
    private String description;
    private String leader;

    @Builder
    public TeamCreateRequestDto(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }
}