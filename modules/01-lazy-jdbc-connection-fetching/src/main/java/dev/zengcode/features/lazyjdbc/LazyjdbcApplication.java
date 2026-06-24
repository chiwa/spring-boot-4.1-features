package dev.zengcode.features.lazyjdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LazyjdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(LazyjdbcApplication.class, args);
    }
}
