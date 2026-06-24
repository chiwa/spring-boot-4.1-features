package dev.zengcode.features.ssrf;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "HTTP Client SSRF Mitigation",
            "module", "03-http-client-ssrf-mitigation",
            "time", Instant.now().toString()
        );
    }
}
