package dev.zengcode.features.redislistener;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Redis Listener Auto Configuration",
            "module", "05-redis-listener",
            "time", Instant.now().toString()
        );
    }
}
