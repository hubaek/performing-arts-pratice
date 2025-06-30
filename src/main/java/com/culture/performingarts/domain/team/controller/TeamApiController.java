package com.culture.performingarts.domain.team.controller;

import com.culture.performingarts.domain.team.dto.TeamListResponseDto;
import com.culture.performingarts.domain.team.dto.TeamResponseDto;
import com.culture.performingarts.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일반 사용자용 팀 조회 컨트롤러
 * 팀 목록 조회 등 읽기 전용 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Slf4j
public class TeamApiController {

    private final TeamService teamService;

    /**
     * 특정 팀 상세 조회
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable Long teamId) {
        log.info("Requesting team details for ID: {}", teamId);
        TeamResponseDto response = teamService.getTeam(teamId);
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 팀 조회
     */
    @GetMapping
    public ResponseEntity<List<TeamListResponseDto>> getAllTeams() {
        log.info("Requesting all teams list");
        List<TeamListResponseDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    /**
     * 활성화된 팀만 조회
     * 회원가입 시 팀 선택용으로 활용
     */
    @GetMapping("/active")
    public ResponseEntity<List<TeamListResponseDto>> getActiveTeams() {
        log.info("Requesting active teams list");
        List<TeamListResponseDto> teams = teamService.getActiveTeams();
        return ResponseEntity.ok(teams);
    }

    /**
     * 상태별 팀 조회
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TeamListResponseDto>> getTeamsByStatus(@PathVariable String status) {
        log.info("Requesting teams by status: {}", status);
        List<TeamListResponseDto> teams = teamService.getTeamsByStatus(status);
        return ResponseEntity.ok(teams);
    }
}