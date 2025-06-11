package com.culture.performingarts.domain.practice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeUpdateRequestDto {

    private String title;
    private String content;
    private String location;
    private LocalDate practiceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;

    @Builder
    public PracticeUpdateRequestDto(String title, String content, String location,
                                  LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                                  String comment) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
    }
}