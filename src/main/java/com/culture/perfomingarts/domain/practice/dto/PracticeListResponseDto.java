package com.culture.perfomingarts.domain.practice.dto;

import com.culture.perfomingarts.domain.practice.entity.Practice;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeListResponseDto {

    private Long id;
    private String title;
    private String location;
    private LocalDate practiceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long teamId;
    private Boolean isCompleted;
    private Integer totalParticipants;
    private Double attendanceRate;

    @Builder
    public PracticeListResponseDto(Long id, String title, String location,
                                 LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                                 Long teamId, Boolean isCompleted, Integer totalParticipants,
                                 Double attendanceRate) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teamId = teamId;
        this.isCompleted = isCompleted;
        this.totalParticipants = totalParticipants;
        this.attendanceRate = attendanceRate;
    }

    public static PracticeListResponseDto fromEntity(Practice practice) {
        return PracticeListResponseDto.builder()
                .id(practice.getId())
                .title(practice.getTitle())
                .location(practice.getLocation())
                .practiceDate(practice.getPracticeDate())
                .startTime(practice.getStartTime())
                .endTime(practice.getEndTime())
                .teamId(practice.getTeamId())
                .isCompleted(practice.getIsCompleted())
                .totalParticipants(practice.getTotalParticipants())
                .attendanceRate(practice.getAttendanceRate())
                .build();
    }
}