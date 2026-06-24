package dev.zengcode.features.actuatorinfo;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Actuator Info Improvements",
            "module", "07-actuator-info",
            "time", Instant.now().toString()
        );
    }
}
