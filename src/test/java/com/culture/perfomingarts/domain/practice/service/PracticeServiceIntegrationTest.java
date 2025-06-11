package com.culture.perfomingarts.domain.practice.service;

import com.culture.perfomingarts.domain.practice.dto.PracticeCreateRequestDto;
import com.culture.perfomingarts.domain.practice.dto.PracticeResponseDto;
import com.culture.perfomingarts.domain.practice.entity.Practice;
import com.culture.perfomingarts.domain.practice.repository.PracticeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PracticeServiceIntegrationTest {

    @Autowired
    private PracticeService practiceService;

    @Autowired
    private PracticeRepository practiceRepository;

    @Test
    void 연습_생성_테스트() {
        // given
        PracticeCreateRequestDto requestDto = PracticeCreateRequestDto.builder()
                .title("연기 연습")
                .content("햄릿 1막 연습")
                .location("연습실 A")
                .practiceDate(LocalDate.now())
                .startTime(LocalTime.of(19, 0))
                .endTime(LocalTime.of(21, 0))
                .comment("집중 연습")
                .teamId(1L)
                .build();

        // when
        PracticeResponseDto response = practiceService.createPractice(requestDto, 1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("연기 연습");
        assertThat(response.getContent()).isEqualTo("햄릿 1막 연습");
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getIsCompleted()).isFalse();
        
        // 데이터베이스 확인
        Practice savedPractice = practiceRepository.findById(response.getId()).orElse(null);
        assertThat(savedPractice).isNotNull();
        assertThat(savedPractice.getTitle()).isEqualTo("연기 연습");
    }

    @Test
    void 연습_조회_테스트() {
        // given
        Practice practice = Practice.builder()
                .title("무용 연습")
                .content("발레 기초 연습")
                .location("무용실")
                .practiceDate(LocalDate.now())
                .startTime(LocalTime.of(18, 0))
                .endTime(LocalTime.of(20, 0))
                .userId(1L)
                .teamId(2L)
                .build();
        
        Practice savedPractice = practiceRepository.save(practice);

        // when
        PracticeResponseDto response = practiceService.getPractice(savedPractice.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(savedPractice.getId());
        assertThat(response.getTitle()).isEqualTo("무용 연습");
        assertThat(response.getContent()).isEqualTo("발레 기초 연습");
        assertThat(response.getPracticeDurationInMinutes()).isEqualTo(120);
    }
}