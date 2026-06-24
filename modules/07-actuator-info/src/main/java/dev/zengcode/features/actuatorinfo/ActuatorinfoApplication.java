package dev.zengcode.features.actuatorinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ActuatorinfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActuatorinfoApplication.class, args);
    }
}
