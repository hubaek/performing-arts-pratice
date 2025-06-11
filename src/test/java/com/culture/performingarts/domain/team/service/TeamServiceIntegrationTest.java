package com.culture.performingarts.domain.team.service;

import com.culture.performingarts.domain.team.dto.TeamCreateRequestDto;
import com.culture.performingarts.domain.team.dto.TeamResponseDto;
import com.culture.performingarts.domain.team.entity.Team;
import com.culture.performingarts.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeamServiceIntegrationTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void 팀_생성_테스트() {
        // given
        TeamCreateRequestDto requestDto = TeamCreateRequestDto.builder()
                .name("연기팀")
                .description("연기 전문 팀")
                .leader("김연기")
                .build();

        // when
        TeamResponseDto response = teamService.createTeam(requestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("연기팀");
        assertThat(response.getDescription()).isEqualTo("연기 전문 팀");
        assertThat(response.getLeader()).isEqualTo("김연기");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        assertThat(response.getMemberCount()).isEqualTo(0);
        
        // 데이터베이스 확인
        Team savedTeam = teamRepository.findById(response.getId()).orElse(null);
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo("연기팀");
    }

    @Test
    void 팀_상태_변경_테스트() {
        // given
        Team team = Team.builder()
                .name("음악팀")
                .description("음악 전문 팀")
                .leader("박음악")
                .build();
        
        Team savedTeam = teamRepository.save(team);

        // when
        TeamResponseDto deactivatedResponse = teamService.deactivateTeam(savedTeam.getId());
        TeamResponseDto activatedResponse = teamService.activateTeam(savedTeam.getId());

        // then
        assertThat(deactivatedResponse.getStatus()).isEqualTo("INACTIVE");
        assertThat(activatedResponse.getStatus()).isEqualTo("ACTIVE");
    }
}