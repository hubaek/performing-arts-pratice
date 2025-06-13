package com.culture.performingarts.domain.practice.service;

import com.culture.performingarts.domain.practice.dto.PracticeCreateRequestDto;
import com.culture.performingarts.domain.practice.dto.PracticeListResponseDto;
import com.culture.performingarts.domain.practice.dto.PracticeResponseDto;
import com.culture.performingarts.domain.practice.dto.PracticeUpdateRequestDto;
import com.culture.performingarts.domain.practice.entity.Practice;
import com.culture.performingarts.domain.practice.repository.PracticeRepository;
import com.culture.performingarts.domain.member.entity.Member;
import com.culture.performingarts.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PracticeService {

    private final PracticeRepository practiceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PracticeResponseDto createPractice(PracticeCreateRequestDto requestDto, String userEmail) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Practice practice = Practice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .location(requestDto.getLocation())
                .practiceDate(requestDto.getPracticeDate())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .comment(requestDto.getComment())
                .userId(member.getId())
                .teamId(requestDto.getTeamId())
                .build();

        Practice savedPractice = practiceRepository.save(practice);
        return PracticeResponseDto.fromEntity(savedPractice);
    }

    public PracticeResponseDto getPractice(Long practiceId) {
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new IllegalArgumentException("연습 정보를 찾을 수 없습니다."));
        
        return PracticeResponseDto.fromEntity(practice);
    }

    public List<PracticeListResponseDto> getAllPractices() {
        return practiceRepository.findAllByOrderByPracticeDateDescStartTimeDesc()
                .stream()
                .map(PracticeListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PracticeListResponseDto> getPracticesWithPaging(Pageable pageable) {
        return practiceRepository.findAllByOrderByPracticeDateDescStartTimeDesc(pageable)
                .map(PracticeListResponseDto::fromEntity);
    }

    public List<PracticeListResponseDto> getPracticesByTeam(Long teamId) {
        return practiceRepository.findByTeamIdOrderByPracticeDateDescStartTimeDesc(teamId)
                .stream()
                .map(PracticeListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PracticeListResponseDto> getPracticesByDateRange(LocalDate startDate, LocalDate endDate) {
        return practiceRepository.findByPracticeDateBetweenOrderByPracticeDateDescStartTimeDesc(startDate, endDate)
                .stream()
                .map(PracticeListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PracticeListResponseDto> getPracticesByUser(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return practiceRepository.findByUserIdOrderByPracticeDateDescStartTimeDesc(member.getId())
                .stream()
                .map(PracticeListResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PracticeResponseDto updatePractice(Long practiceId, PracticeUpdateRequestDto requestDto, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new IllegalArgumentException("연습 정보를 찾을 수 없습니다."));

        if (!practice.getUserId().equals(member.getId())) {
            throw new IllegalArgumentException("연습 정보를 수정할 권한이 없습니다.");
        }

        practice.updateInfo(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getLocation(),
                requestDto.getPracticeDate(),
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getComment()
        );

        return PracticeResponseDto.fromEntity(practice);
    }

    @Transactional
    public void deletePractice(Long practiceId, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new IllegalArgumentException("연습 정보를 찾을 수 없습니다."));

        if (!practice.getUserId().equals(member.getId())) {
            throw new IllegalArgumentException("연습 정보를 삭제할 권한이 없습니다.");
        }

        if (practice.getIsCompleted()) {
            throw new IllegalStateException("완료된 연습은 삭제할 수 없습니다.");
        }

        practiceRepository.delete(practice);
    }

    @Transactional
    public PracticeResponseDto completePractice(Long practiceId, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new IllegalArgumentException("연습 정보를 찾을 수 없습니다."));

        if (!practice.getUserId().equals(member.getId())) {
            throw new IllegalArgumentException("연습을 완료할 권한이 없습니다.");
        }

        practice.complete();
        return PracticeResponseDto.fromEntity(practice);
    }
}