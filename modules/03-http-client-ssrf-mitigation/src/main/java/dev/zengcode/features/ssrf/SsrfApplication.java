package dev.zengcode.features.ssrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SsrfApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsrfApplication.class, args);
    }
}
