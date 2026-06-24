package dev.zengcode.features.grpcdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GrpcdemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrpcdemoApplication.class, args);
    }
}
