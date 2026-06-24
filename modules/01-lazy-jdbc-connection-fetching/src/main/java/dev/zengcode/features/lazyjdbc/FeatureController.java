package dev.zengcode.features.lazyjdbc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lazy-jdbc")
public class FeatureController {

    private final LazyJdbcService lazyJdbcService;
    private final ConnectionPoolInspector poolInspector;

    public FeatureController(LazyJdbcService lazyJdbcService, ConnectionPoolInspector poolInspector) {
        this.lazyJdbcService = lazyJdbcService;
        this.poolInspector = poolInspector;
    }

    @GetMapping("/no-db")
    public DemoResponse noDb() {
        List<TimelineStep> timeline = lazyJdbcService.noDb();
        timeline.add(poolInspector.captureStep("transaction-ended"));
        
        return new DemoResponse(
                "no-db",
                "Transaction runs but never accesses the database.",
                timeline,
                "With Lazy Fetching (simulating Spring Boot 4.1), no Hikari connection is ever acquired because no SQL was executed. Active connections remain 0."
        );
    }

    @GetMapping("/external-then-db")
    public DemoResponse externalThenDb() {
        List<TimelineStep> timeline = lazyJdbcService.externalThenDb();
        timeline.add(poolInspector.captureStep("transaction-ended"));
        
        return new DemoResponse(
                "external-then-db",
                "Simulates a slow 2-second external API call BEFORE hitting the database.",
                timeline,
                "Active connections remain 0 during the external call. The connection is ONLY acquired when the database is actually accessed, preventing pool exhaustion."
        );
    }

    @GetMapping("/db-first")
    public DemoResponse dbFirst() {
        List<TimelineStep> timeline = lazyJdbcService.dbFirst();
        timeline.add(poolInspector.captureStep("transaction-ended"));
        
        return new DemoResponse(
                "db-first",
                "Queries the database FIRST, then simulates a slow 2-second external API call.",
                timeline,
                "Active connections immediately jump to 1 and stay there during the 2-second external call. This demonstrates the traditional problem: idle connections block the pool."
        );
    }
}
