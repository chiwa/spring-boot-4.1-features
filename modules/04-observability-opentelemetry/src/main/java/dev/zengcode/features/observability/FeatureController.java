package dev.zengcode.features.observability;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "OpenTelemetry & Observability",
            "module", "04-observability-opentelemetry",
            "time", Instant.now().toString()
        );
    }
}
