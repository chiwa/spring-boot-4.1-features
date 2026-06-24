package dev.zengcode.features.lazyjdbc;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Lazy JDBC Connection Fetching",
            "module", "01-lazy-jdbc-connection-fetching",
            "time", Instant.now().toString()
        );
    }
}
