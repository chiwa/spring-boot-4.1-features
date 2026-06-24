package dev.zengcode.features.redislistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedislistenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedislistenerApplication.class, args);
    }
}
