package com.culture.performingarts.domain.practiceParticipation.entity;

import com.culture.performingarts.common.entity.Timestamped;
import com.culture.performingarts.domain.practiceParticipation.enums.PracticeParticipationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 연습 참여 기록 엔티티
 * 각 회원의 연습 참석/결석 정보를 관리합니다.
 * Practice와 Member 간의 다대다 관계를 풀어내는 중간 엔티티 역할을 합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "practice_participations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"practice_id", "user_id"}))
public class PracticeParticipation extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "practice_id", nullable = false)
    private Long practiceId; // 연습 ID (Practice 참조)

    @Column(name = "user_id", nullable = false)
    private Long userId; // 회원 ID (Member 참조)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PracticeParticipationStatus status; // 참석 상태 (출석, 지각, 결석)

    @Column(length = 200)
    private String reason; // 결석/지각 사유

    @Column(columnDefinition = "TEXT")
    private String comment; // 비고 (특이사항, 개인 피드백 등)

    @Column(nullable = false)
    private Boolean isExcused = false; // 사유 인정 여부 (결석/지각 시)

    @Builder
    public PracticeParticipation(Long practiceId, Long userId, 
                                PracticeParticipationStatus status, 
                                String reason, String comment, Boolean isExcused) {
        this.practiceId = practiceId;
        this.userId = userId;
        this.status = status;
        this.reason = reason;
        this.comment = comment;
        this.isExcused = isExcused != null ? isExcused : false;
    }

    /**
     * 참석 상태 업데이트
     * 연습 완료 전까지 수정 가능합니다.
     * 
     * @param status 새로운 참석 상태
     * @param reason 사유
     * @param isExcused 사유 인정 여부
     */
    public void updateStatus(PracticeParticipationStatus status, String reason, Boolean isExcused) {
        this.status = status;
        this.reason = reason;
        this.isExcused = isExcused;
    }

    /**
     * 코멘트 업데이트
     * 관리자나 작성자가 추가 코멘트를 남길 때 사용합니다.
     * 
     * @param comment 코멘트 내용
     */
    public void updateComment(String comment) {
        this.comment = comment;
    }

    /**
     * 사유 인정 처리
     * 관리자가 결석/지각 사유를 인정할 때 사용합니다.
     */
    public void excuse() {
        this.isExcused = true;
    }

    /**
     * 사유 불인정 처리
     */
    public void unexcuse() {
        this.isExcused = false;
    }

    /**
     * 출석 여부 확인
     * 출석과 지각을 모두 출석으로 간주합니다.
     * 
     * @return 출석 여부
     */
    public boolean isPresent() {
        return status == PracticeParticipationStatus.ATTENDANCE || 
               status == PracticeParticipationStatus.LATE;
    }

    /**
     * 결석 여부 확인
     * 
     * @return 결석 여부
     */
    public boolean isAbsent() {
        return status == PracticeParticipationStatus.ABSENT;
    }
}
