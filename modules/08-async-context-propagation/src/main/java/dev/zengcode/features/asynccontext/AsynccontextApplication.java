package dev.zengcode.features.asynccontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AsynccontextApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsynccontextApplication.class, args);
    }
}
