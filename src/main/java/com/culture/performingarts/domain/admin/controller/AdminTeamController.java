package com.culture.performingarts.domain.admin.controller;

import com.culture.performingarts.config.security.annotation.AdminOnly;
import com.culture.performingarts.domain.team.dto.TeamCreateRequestDto;
import com.culture.performingarts.domain.team.dto.TeamListResponseDto;
import com.culture.performingarts.domain.team.dto.TeamResponseDto;
import com.culture.performingarts.domain.team.dto.TeamUpdateRequestDto;
import com.culture.performingarts.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자용 팀 관리 컨트롤러
 * 관리자만 팀 생성, 수정, 삭제 등의 작업을 수행할 수 있습니다.
 */
@RestController
@RequestMapping("/api/admin/teams")
@RequiredArgsConstructor
@Slf4j
@AdminOnly
public class AdminTeamController {

    private final TeamService teamService;

    /**
     * 팀 생성 (관리자 전용)
     */
    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@Valid @RequestBody TeamCreateRequestDto requestDto) {
        log.info("Admin creating new team: {}", requestDto.getName());
        TeamResponseDto response = teamService.createTeam(requestDto);
        log.info("Team created successfully with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 모든 팀 조회 (관리자 전용)
     */
    @GetMapping
    public ResponseEntity<List<TeamListResponseDto>> getAllTeams() {
        log.info("Admin requesting all teams list");
        List<TeamListResponseDto> teams = teamService.getAllTeams();
        log.info("Retrieved {} teams", teams.size());
        return ResponseEntity.ok(teams);
    }

    /**
     * 특정 팀 상세 조회 (관리자 전용)
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable Long teamId) {
        log.info("Admin requesting team details for ID: {}", teamId);
        TeamResponseDto response = teamService.getTeamById(teamId);
        return ResponseEntity.ok(response);
    }

    /**
     * 팀 정보 수정 (관리자 전용)
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody TeamUpdateRequestDto requestDto) {
        log.info("Admin updating team ID: {} with data: {}", teamId, requestDto.getName());
        TeamResponseDto response = teamService.updateTeam(teamId, requestDto);
        log.info("Team updated successfully: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * 팀 삭제 (관리자 전용)
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        log.info("Admin deleting team ID: {}", teamId);
        teamService.deleteTeam(teamId);
        log.info("Team deleted successfully: {}", teamId);
        return ResponseEntity.noContent().build();
    }
}