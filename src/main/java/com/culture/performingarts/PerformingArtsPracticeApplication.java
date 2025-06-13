package com.culture.performingarts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PerformingArtsPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformingArtsPracticeApplication.class, args);
    }

}
