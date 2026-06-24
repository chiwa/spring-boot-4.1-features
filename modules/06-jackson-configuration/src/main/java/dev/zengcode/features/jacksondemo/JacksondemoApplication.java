package dev.zengcode.features.jacksondemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JacksondemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(JacksondemoApplication.class, args);
    }
}
