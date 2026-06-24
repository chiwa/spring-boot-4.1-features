package dev.zengcode.features.lazyjdbc;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Service
public class LazyJdbcService {

    private final ProductRepository productRepository;
    private final ConnectionPoolInspector poolInspector;
    private final DataSource dataSource;

    public LazyJdbcService(ProductRepository productRepository, ConnectionPoolInspector poolInspector, DataSource dataSource) {
        this.productRepository = productRepository;
        this.poolInspector = poolInspector;
        this.dataSource = dataSource;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TimelineStep> noDb() {
        List<TimelineStep> timeline = new ArrayList<>();
        timeline.add(poolInspector.captureStep("transaction-started"));
        
        timeline.add(poolInspector.captureStep("before-external-call"));
        simulateExternalCall();
        timeline.add(poolInspector.captureStep("after-external-call"));
        
        return timeline;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TimelineStep> externalThenDb() {
        List<TimelineStep> timeline = new ArrayList<>();
        timeline.add(poolInspector.captureStep("transaction-started"));
        
        timeline.add(poolInspector.captureStep("before-external-call"));
        simulateExternalCall();
        timeline.add(poolInspector.captureStep("after-external-call"));
        
        timeline.add(poolInspector.captureStep("before-database-access"));
        productRepository.count();
        timeline.add(poolInspector.captureStep("after-database-access"));
        
        return timeline;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TimelineStep> dbFirst() {
        List<TimelineStep> timeline = new ArrayList<>();
        timeline.add(poolInspector.captureStep("transaction-started"));
        
        timeline.add(poolInspector.captureStep("before-database-access"));
        productRepository.count();
        timeline.add(poolInspector.captureStep("after-database-access"));
        
        timeline.add(poolInspector.captureStep("before-external-call"));
        simulateExternalCall();
        timeline.add(poolInspector.captureStep("after-external-call"));
        
        return timeline;
    }

    private void simulateExternalCall() {
        try {
            // Simulate a slow external API call (2 seconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
