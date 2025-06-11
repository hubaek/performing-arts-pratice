package com.culture.performingarts.domain.team.controller;

import com.culture.performingarts.domain.team.dto.TeamCreateRequestDto;
import com.culture.performingarts.domain.team.dto.TeamListResponseDto;
import com.culture.performingarts.domain.team.dto.TeamResponseDto;
import com.culture.performingarts.domain.team.dto.TeamUpdateRequestDto;
import com.culture.performingarts.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponseDto> createTeam(@RequestBody TeamCreateRequestDto requestDto) {
        TeamResponseDto response = teamService.createTeam(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable Long teamId) {
        TeamResponseDto response = teamService.getTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamListResponseDto>> getAllTeams() {
        List<TeamListResponseDto> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TeamListResponseDto>> getActiveTeams() {
        List<TeamListResponseDto> teams = teamService.getActiveTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TeamListResponseDto>> getTeamsByStatus(@PathVariable String status) {
        List<TeamListResponseDto> teams = teamService.getTeamsByStatus(status);
        return ResponseEntity.ok(teams);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamUpdateRequestDto requestDto) {
        
        TeamResponseDto response = teamService.updateTeam(teamId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{teamId}/activate")
    public ResponseEntity<TeamResponseDto> activateTeam(@PathVariable Long teamId) {
        TeamResponseDto response = teamService.activateTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{teamId}/deactivate")
    public ResponseEntity<TeamResponseDto> deactivateTeam(@PathVariable Long teamId) {
        TeamResponseDto response = teamService.deactivateTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{teamId}/member-count")
    public ResponseEntity<TeamResponseDto> updateMemberCount(
            @PathVariable Long teamId,
            @RequestParam Integer memberCount) {
        
        TeamResponseDto response = teamService.updateMemberCount(teamId, memberCount);
        return ResponseEntity.ok(response);
    }
}