package com.culture.perfomingarts.domain.practice.entity;

import com.culture.perfomingarts.common.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Practice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String location;

    private LocalDate practiceDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String comment;

    private Long userId;

}
