package dev.zengcode.features.grpcdemo;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Spring gRPC Support",
            "module", "02-spring-grpc",
            "time", Instant.now().toString()
        );
    }
}
