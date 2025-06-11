package com.culture.perfomingarts.domain.practice.dto;

import com.culture.perfomingarts.domain.practice.entity.Practice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private String location;
    private LocalDate practiceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;
    private Long userId;
    private Long teamId;
    private Boolean isCompleted;
    private Integer totalParticipants;
    private Integer presentCount;
    private Integer lateCount;
    private Integer absentCount;
    private Double attendanceRate;
    private Long practiceDurationInMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PracticeResponseDto(Long id, String title, String content, String location,
                             LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                             String comment, Long userId, Long teamId, Boolean isCompleted,
                             Integer totalParticipants, Integer presentCount, Integer lateCount,
                             Integer absentCount, Double attendanceRate, Long practiceDurationInMinutes,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.userId = userId;
        this.teamId = teamId;
        this.isCompleted = isCompleted;
        this.totalParticipants = totalParticipants;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
        this.absentCount = absentCount;
        this.attendanceRate = attendanceRate;
        this.practiceDurationInMinutes = practiceDurationInMinutes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PracticeResponseDto fromEntity(Practice practice) {
        return PracticeResponseDto.builder()
                .id(practice.getId())
                .title(practice.getTitle())
                .content(practice.getContent())
                .location(practice.getLocation())
                .practiceDate(practice.getPracticeDate())
                .startTime(practice.getStartTime())
                .endTime(practice.getEndTime())
                .comment(practice.getComment())
                .userId(practice.getUserId())
                .teamId(practice.getTeamId())
                .isCompleted(practice.getIsCompleted())
                .totalParticipants(practice.getTotalParticipants())
                .presentCount(practice.getPresentCount())
                .lateCount(practice.getLateCount())
                .absentCount(practice.getAbsentCount())
                .attendanceRate(practice.getAttendanceRate())
                .practiceDurationInMinutes(practice.getPracticeDurationInMinutes())
                .createdAt(practice.getCreatedAt())
                .updatedAt(practice.getUpdatedAt())
                .build();
    }
}