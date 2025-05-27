package com.example.softengineerwebpr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // 추가

@SpringBootApplication
@EnableJpaAuditing // 추가: JPA Auditing 활성화
public class SoftEngineerWebprApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftEngineerWebprApplication.class, args);
    }
}
