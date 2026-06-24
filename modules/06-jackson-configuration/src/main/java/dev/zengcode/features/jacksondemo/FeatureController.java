package dev.zengcode.features.jacksondemo;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Jackson Configuration Improvements",
            "module", "06-jackson-configuration",
            "time", Instant.now().toString()
        );
    }
}
