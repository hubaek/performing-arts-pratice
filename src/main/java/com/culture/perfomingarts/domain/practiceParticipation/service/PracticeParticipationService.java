package com.culture.perfomingarts.domain.practiceParticipation.service;

import com.culture.perfomingarts.domain.practice.entity.Practice;
import com.culture.perfomingarts.domain.practice.repository.PracticeRepository;
import com.culture.perfomingarts.domain.practiceParticipation.dto.PracticeParticipationBulkCreateRequestDto;
import com.culture.perfomingarts.domain.practiceParticipation.dto.PracticeParticipationCreateRequestDto;
import com.culture.perfomingarts.domain.practiceParticipation.dto.PracticeParticipationResponseDto;
import com.culture.perfomingarts.domain.practiceParticipation.dto.PracticeParticipationUpdateRequestDto;
import com.culture.perfomingarts.domain.practiceParticipation.entity.PracticeParticipation;
import com.culture.perfomingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import com.culture.perfomingarts.domain.practiceParticipation.repository.PracticeParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PracticeParticipationService {

    private final PracticeParticipationRepository participationRepository;
    private final PracticeRepository practiceRepository;

    @Transactional
    public PracticeParticipationResponseDto createParticipation(PracticeParticipationCreateRequestDto requestDto) {
        if (participationRepository.existsByPracticeIdAndUserId(requestDto.getPracticeId(), requestDto.getUserId())) {
            throw new IllegalArgumentException("이미 참여 기록이 존재합니다.");
        }

        PracticeParticipation participation = PracticeParticipation.builder()
                .practiceId(requestDto.getPracticeId())
                .userId(requestDto.getUserId())
                .status(requestDto.getStatus())
                .reason(requestDto.getReason())
                .comment(requestDto.getComment())
                .isExcused(requestDto.getIsExcused())
                .build();

        PracticeParticipation savedParticipation = participationRepository.save(participation);
        updatePracticeAttendanceStats(requestDto.getPracticeId());

        return PracticeParticipationResponseDto.fromEntity(savedParticipation);
    }

    @Transactional
    public List<PracticeParticipationResponseDto> createBulkParticipations(PracticeParticipationBulkCreateRequestDto requestDto) {
        List<PracticeParticipation> participations = requestDto.getParticipations().stream()
                .map(dto -> {
                    if (participationRepository.existsByPracticeIdAndUserId(dto.getPracticeId(), dto.getUserId())) {
                        throw new IllegalArgumentException("이미 참여 기록이 존재합니다: User ID " + dto.getUserId());
                    }
                    return PracticeParticipation.builder()
                            .practiceId(dto.getPracticeId())
                            .userId(dto.getUserId())
                            .status(dto.getStatus())
                            .reason(dto.getReason())
                            .comment(dto.getComment())
                            .isExcused(dto.getIsExcused())
                            .build();
                })
                .collect(Collectors.toList());

        List<PracticeParticipation> savedParticipations = participationRepository.saveAll(participations);
        updatePracticeAttendanceStats(requestDto.getPracticeId());

        return savedParticipations.stream()
                .map(PracticeParticipationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PracticeParticipationResponseDto getParticipation(Long participationId) {
        PracticeParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 기록을 찾을 수 없습니다."));
        
        return PracticeParticipationResponseDto.fromEntity(participation);
    }

    public List<PracticeParticipationResponseDto> getParticipationsByPractice(Long practiceId) {
        return participationRepository.findByPracticeId(practiceId)
                .stream()
                .map(PracticeParticipationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PracticeParticipationResponseDto> getParticipationsByUser(Long userId) {
        return participationRepository.findByUserId(userId)
                .stream()
                .map(PracticeParticipationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PracticeParticipationResponseDto> getParticipationsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return participationRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(PracticeParticipationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PracticeParticipationResponseDto> getParticipationsByStatus(Long practiceId, PracticeParticipationStatus status) {
        return participationRepository.findByPracticeIdAndStatus(practiceId, status)
                .stream()
                .map(PracticeParticipationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PracticeParticipationResponseDto updateParticipation(Long participationId, PracticeParticipationUpdateRequestDto requestDto) {
        PracticeParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 기록을 찾을 수 없습니다."));

        participation.updateStatus(
                requestDto.getStatus(),
                requestDto.getReason(),
                requestDto.getIsExcused()
        );

        if (requestDto.getComment() != null) {
            participation.updateComment(requestDto.getComment());
        }

        updatePracticeAttendanceStats(participation.getPracticeId());

        return PracticeParticipationResponseDto.fromEntity(participation);
    }

    @Transactional
    public void deleteParticipation(Long participationId) {
        PracticeParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 기록을 찾을 수 없습니다."));

        Long practiceId = participation.getPracticeId();
        participationRepository.delete(participation);
        updatePracticeAttendanceStats(practiceId);
    }

    @Transactional
    public PracticeParticipationResponseDto excuseParticipation(Long participationId) {
        PracticeParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 기록을 찾을 수 없습니다."));

        participation.excuse();
        return PracticeParticipationResponseDto.fromEntity(participation);
    }

    @Transactional
    public PracticeParticipationResponseDto unexcuseParticipation(Long participationId) {
        PracticeParticipation participation = participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("참여 기록을 찾을 수 없습니다."));

        participation.unexcuse();
        return PracticeParticipationResponseDto.fromEntity(participation);
    }

    public AttendanceStatsDto getUserAttendanceStats(Long userId) {
        List<Object[]> stats = participationRepository.getAttendanceStatsByUserId(userId);
        
        if (stats.isEmpty() || stats.get(0)[0] == null) {
            return AttendanceStatsDto.builder()
                    .totalPractices(0L)
                    .attendanceCount(0L)
                    .lateCount(0L)
                    .absentCount(0L)
                    .attendanceRate(0.0)
                    .build();
        }

        Object[] result = stats.get(0);
        Long totalPractices = ((Number) result[0]).longValue();
        Long attendanceCount = ((Number) result[1]).longValue();
        Long lateCount = ((Number) result[2]).longValue();
        Long absentCount = ((Number) result[3]).longValue();

        double attendanceRate = totalPractices > 0 
            ? ((double) (attendanceCount + lateCount) / totalPractices) * 100 
            : 0.0;

        return AttendanceStatsDto.builder()
                .totalPractices(totalPractices)
                .attendanceCount(attendanceCount)
                .lateCount(lateCount)
                .absentCount(absentCount)
                .attendanceRate(attendanceRate)
                .build();
    }

    private void updatePracticeAttendanceStats(Long practiceId) {
        Practice practice = practiceRepository.findById(practiceId)
                .orElseThrow(() -> new IllegalArgumentException("연습 정보를 찾을 수 없습니다."));

        Long totalParticipants = participationRepository.countByPracticeId(practiceId);
        Long presentCount = participationRepository.countByPracticeIdAndStatus(practiceId, PracticeParticipationStatus.ATTENDANCE);
        Long lateCount = participationRepository.countByPracticeIdAndStatus(practiceId, PracticeParticipationStatus.LATE);
        Long absentCount = participationRepository.countByPracticeIdAndStatus(practiceId, PracticeParticipationStatus.ABSENT);

        practice.updateAttendanceStats(
                totalParticipants.intValue(),
                presentCount.intValue(),
                lateCount.intValue(),
                absentCount.intValue()
        );
    }

    @lombok.Builder
    @lombok.Getter
    public static class AttendanceStatsDto {
        private final Long totalPractices;
        private final Long attendanceCount;
        private final Long lateCount;
        private final Long absentCount;
        private final Double attendanceRate;
    }
}