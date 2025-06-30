package com.culture.performingarts.domain.team.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCreateRequestDto {

    @NotBlank(message = "팀 이름은 필수입니다")
    @Size(max = 50, message = "팀 이름은 50자를 초과할 수 없습니다")
    private String name;
    
    @Size(max = 500, message = "팀 설명은 500자를 초과할 수 없습니다")
    private String description;
    
    @Size(max = 50, message = "팀장 이름은 50자를 초과할 수 없습니다")
    private String leader;

    @Builder
    public TeamCreateRequestDto(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }
}