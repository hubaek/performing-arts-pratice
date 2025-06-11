package com.culture.perfomingarts.domain.team.service;

import com.culture.perfomingarts.domain.team.dto.TeamCreateRequestDto;
import com.culture.perfomingarts.domain.team.dto.TeamListResponseDto;
import com.culture.perfomingarts.domain.team.dto.TeamResponseDto;
import com.culture.perfomingarts.domain.team.dto.TeamUpdateRequestDto;
import com.culture.perfomingarts.domain.team.entity.Team;
import com.culture.perfomingarts.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamResponseDto createTeam(TeamCreateRequestDto requestDto) {
        Team team = Team.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .leader(requestDto.getLeader())
                .build();

        Team savedTeam = teamRepository.save(team);
        return TeamResponseDto.fromEntity(savedTeam);
    }

    public TeamResponseDto getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));
        
        return TeamResponseDto.fromEntity(team);
    }

    public List<TeamListResponseDto> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(TeamListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TeamListResponseDto> getActiveTeams() {
        return teamRepository.findByStatus("ACTIVE")
                .stream()
                .map(TeamListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TeamListResponseDto> getTeamsByStatus(String status) {
        return teamRepository.findByStatus(status)
                .stream()
                .map(TeamListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TeamResponseDto updateTeam(Long teamId, TeamUpdateRequestDto requestDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));

        team.updateInfo(
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getLeader()
        );

        return TeamResponseDto.fromEntity(team);
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));

        teamRepository.delete(team);
    }

    @Transactional
    public TeamResponseDto activateTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));

        team.activate();
        return TeamResponseDto.fromEntity(team);
    }

    @Transactional
    public TeamResponseDto deactivateTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));

        team.deactivate();
        return TeamResponseDto.fromEntity(team);
    }

    @Transactional
    public TeamResponseDto updateMemberCount(Long teamId, Integer memberCount) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀 정보를 찾을 수 없습니다."));

        team.updateMemberCount(memberCount);
        return TeamResponseDto.fromEntity(team);
    }
}