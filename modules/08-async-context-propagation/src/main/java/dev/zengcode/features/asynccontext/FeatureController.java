package dev.zengcode.features.asynccontext;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FeatureController {
    @GetMapping("/feature")
    Map<String, Object> feature() {
        return Map.of(
            "feature", "Async Context Propagation",
            "module", "08-async-context-propagation",
            "time", Instant.now().toString()
        );
    }
}
