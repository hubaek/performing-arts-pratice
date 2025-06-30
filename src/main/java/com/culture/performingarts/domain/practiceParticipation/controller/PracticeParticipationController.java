package com.culture.performingarts.domain.practiceParticipation.controller;

import com.culture.performingarts.domain.practiceParticipation.dto.PracticeParticipationBulkCreateRequestDto;
import com.culture.performingarts.domain.practiceParticipation.dto.PracticeParticipationCreateRequestDto;
import com.culture.performingarts.domain.practiceParticipation.dto.PracticeParticipationResponseDto;
import com.culture.performingarts.domain.practiceParticipation.dto.PracticeParticipationUpdateRequestDto;
import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import com.culture.performingarts.domain.practiceParticipation.service.PracticeParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/practice-participations")
@RequiredArgsConstructor
public class PracticeParticipationController {

    private final PracticeParticipationService participationService;

    @PostMapping
    public ResponseEntity<PracticeParticipationResponseDto> createParticipation(
            @Valid @RequestBody PracticeParticipationCreateRequestDto requestDto) {
        
        PracticeParticipationResponseDto response = participationService.createParticipation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<PracticeParticipationResponseDto>> createBulkParticipations(
            @Valid @RequestBody PracticeParticipationBulkCreateRequestDto requestDto) {
        
        List<PracticeParticipationResponseDto> responses = participationService.createBulkParticipations(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{participationId}")
    public ResponseEntity<PracticeParticipationResponseDto> getParticipation(@PathVariable Long participationId) {
        PracticeParticipationResponseDto response = participationService.getParticipation(participationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/practice/{practiceId}")
    public ResponseEntity<List<PracticeParticipationResponseDto>> getParticipationsByPractice(@PathVariable Long practiceId) {
        List<PracticeParticipationResponseDto> participations = participationService.getParticipationsByPractice(practiceId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PracticeParticipationResponseDto>> getParticipationsByUser(@PathVariable Long userId) {
        List<PracticeParticipationResponseDto> participations = participationService.getParticipationsByUser(userId);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<PracticeParticipationResponseDto>> getParticipationsByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<PracticeParticipationResponseDto> participations = 
            participationService.getParticipationsByUserAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/practice/{practiceId}/status/{status}")
    public ResponseEntity<List<PracticeParticipationResponseDto>> getParticipationsByStatus(
            @PathVariable Long practiceId,
            @PathVariable PracticeParticipationStatus status) {
        
        List<PracticeParticipationResponseDto> participations = 
            participationService.getParticipationsByStatus(practiceId, status);
        return ResponseEntity.ok(participations);
    }

    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<PracticeParticipationService.AttendanceStatsDto> getUserAttendanceStats(@PathVariable Long userId) {
        PracticeParticipationService.AttendanceStatsDto stats = participationService.getUserAttendanceStats(userId);
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{participationId}")
    public ResponseEntity<PracticeParticipationResponseDto> updateParticipation(
            @PathVariable Long participationId,
            @Valid @RequestBody PracticeParticipationUpdateRequestDto requestDto) {
        
        PracticeParticipationResponseDto response = participationService.updateParticipation(participationId, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{participationId}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long participationId) {
        participationService.deleteParticipation(participationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{participationId}/excuse")
    public ResponseEntity<PracticeParticipationResponseDto> excuseParticipation(@PathVariable Long participationId) {
        PracticeParticipationResponseDto response = participationService.excuseParticipation(participationId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{participationId}/unexcuse")
    public ResponseEntity<PracticeParticipationResponseDto> unexcuseParticipation(@PathVariable Long participationId) {
        PracticeParticipationResponseDto response = participationService.unexcuseParticipation(participationId);
        return ResponseEntity.ok(response);
    }
}