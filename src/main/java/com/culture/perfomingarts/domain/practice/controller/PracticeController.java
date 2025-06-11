package com.culture.perfomingarts.domain.practice.controller;

import com.culture.perfomingarts.domain.practice.dto.PracticeCreateRequestDto;
import com.culture.perfomingarts.domain.practice.dto.PracticeListResponseDto;
import com.culture.perfomingarts.domain.practice.dto.PracticeResponseDto;
import com.culture.perfomingarts.domain.practice.dto.PracticeUpdateRequestDto;
import com.culture.perfomingarts.domain.practice.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/practices")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    @PostMapping
    public ResponseEntity<PracticeResponseDto> createPractice(
            @RequestBody PracticeCreateRequestDto requestDto,
            @RequestHeader("User-Id") Long userId) {
        
        PracticeResponseDto response = practiceService.createPractice(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{practiceId}")
    public ResponseEntity<PracticeResponseDto> getPractice(@PathVariable Long practiceId) {
        PracticeResponseDto response = practiceService.getPractice(practiceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PracticeListResponseDto>> getAllPractices() {
        List<PracticeListResponseDto> practices = practiceService.getAllPractices();
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PracticeListResponseDto>> getPracticesWithPaging(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PracticeListResponseDto> practices = practiceService.getPracticesWithPaging(pageable);
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PracticeListResponseDto>> getPracticesByTeam(@PathVariable Long teamId) {
        List<PracticeListResponseDto> practices = practiceService.getPracticesByTeam(teamId);
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PracticeListResponseDto>> getPracticesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PracticeListResponseDto> practices = practiceService.getPracticesByDateRange(startDate, endDate);
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PracticeListResponseDto>> getMyPractices(
            @RequestHeader("User-Id") Long userId) {
        List<PracticeListResponseDto> practices = practiceService.getPracticesByUser(userId);
        return ResponseEntity.ok(practices);
    }

    @PutMapping("/{practiceId}")
    public ResponseEntity<PracticeResponseDto> updatePractice(
            @PathVariable Long practiceId,
            @RequestBody PracticeUpdateRequestDto requestDto,
            @RequestHeader("User-Id") Long userId) {
        
        PracticeResponseDto response = practiceService.updatePractice(practiceId, requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{practiceId}")
    public ResponseEntity<Void> deletePractice(
            @PathVariable Long practiceId,
            @RequestHeader("User-Id") Long userId) {
        
        practiceService.deletePractice(practiceId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{practiceId}/complete")
    public ResponseEntity<PracticeResponseDto> completePractice(
            @PathVariable Long practiceId,
            @RequestHeader("User-Id") Long userId) {
        
        PracticeResponseDto response = practiceService.completePractice(practiceId, userId);
        return ResponseEntity.ok(response);
    }
}