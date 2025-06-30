package com.culture.performingarts.domain.practice.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PracticeCreateRequestDto {

    @NotBlank(message = "연습 제목은 필수입니다")
    @Size(max = 100, message = "연습 제목은 100자를 초과할 수 없습니다")
    private String title;
    
    @Size(max = 1000, message = "연습 내용은 1000자를 초과할 수 없습니다")
    private String content;
    
    @Size(max = 100, message = "연습 장소는 100자를 초과할 수 없습니다")
    private String location;
    
    @NotNull(message = "연습 날짜는 필수입니다")
    @FutureOrPresent(message = "연습 날짜는 현재 또는 미래 날짜여야 합니다")
    private LocalDate practiceDate;
    
    @NotNull(message = "시작 시간은 필수입니다")
    private LocalTime startTime;
    
    @NotNull(message = "종료 시간은 필수입니다")
    private LocalTime endTime;
    
    @Size(max = 500, message = "코멘트는 500자를 초과할 수 없습니다")
    private String comment;
    
    private Long teamId;

    @Builder
    public PracticeCreateRequestDto(String title, String content, String location,
                                  LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                                  String comment, Long teamId) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.teamId = teamId;
    }
}