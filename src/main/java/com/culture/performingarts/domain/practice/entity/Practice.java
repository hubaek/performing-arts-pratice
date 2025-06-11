package com.culture.performingarts.domain.practice.entity;

import com.culture.performingarts.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 연습 일지 엔티티
 * 공연예술단체의 연습 세션 정보를 기록합니다.
 * 각 연습 세션의 시간, 장소, 내용 등을 상세히 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "practices")
public class Practice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title; // 연습 제목 (예: "햄릿 1막 리허설")

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 연습 내용 상세

    @Column(nullable = false, length = 100)
    private String location; // 연습 장소

    @Column(nullable = false)
    private LocalDate practiceDate; // 연습 날짜

    @Column(nullable = false)
    private LocalTime startTime; // 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(columnDefinition = "TEXT")
    private String comment; // 추가 코멘트나 특이사항

    @Column(nullable = false)
    private Long userId; // 작성자 ID (Member 참조)

    private Long teamId; // 팀 ID (Team 참조)

    @Column(nullable = false)
    private Boolean isCompleted = false; // 연습 완료 여부

    private Integer totalParticipants = 0; // 총 참석자 수 (캐시용)
    private Integer presentCount = 0; // 출석자 수 (캐시용)
    private Integer lateCount = 0; // 지각자 수 (캐시용)
    private Integer absentCount = 0; // 결석자 수 (캐시용)

    @Builder
    public Practice(String title, String content, String location, 
                   LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                   String comment, Long userId, Long teamId) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
        this.userId = userId;
        this.teamId = teamId;
    }

    /**
     * 연습 정보 수정
     * 연습 완료 전에만 수정 가능합니다.
     */
    public void updateInfo(String title, String content, String location,
                          LocalDate practiceDate, LocalTime startTime, LocalTime endTime,
                          String comment) {
        if (this.isCompleted) {
            throw new IllegalStateException("완료된 연습은 수정할 수 없습니다.");
        }
        this.title = title;
        this.content = content;
        this.location = location;
        this.practiceDate = practiceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comment = comment;
    }

    /**
     * 연습 완료 처리
     * 모든 참석 정보가 입력된 후 호출됩니다.
     */
    public void complete() {
        this.isCompleted = true;
    }

    /**
     * 참석 통계 업데이트
     * PracticeParticipation 정보 집계 후 호출됩니다.
     */
    public void updateAttendanceStats(int totalParticipants, int presentCount, 
                                     int lateCount, int absentCount) {
        this.totalParticipants = totalParticipants;
        this.presentCount = presentCount;
        this.lateCount = lateCount;
        this.absentCount = absentCount;
    }

    /**
     * 출석률 계산
     * 
     * @return 출석률 (퍼센트)
     */
    public double getAttendanceRate() {
        if (totalParticipants == 0) return 0.0;
        return ((double) (presentCount + lateCount) / totalParticipants) * 100;
    }

    /**
     * 연습 시간 계산 (분 단위)
     * 
     * @return 연습 시간 (분)
     */
    public long getPracticeDurationInMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
}
