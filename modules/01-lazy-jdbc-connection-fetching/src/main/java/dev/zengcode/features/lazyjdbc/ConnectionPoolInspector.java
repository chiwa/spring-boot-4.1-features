package dev.zengcode.features.lazyjdbc;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ConnectionPoolInspector {

    private final DataSource dataSource;

    public ConnectionPoolInspector(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TimelineStep captureStep(String stepName) {
        try {
            if (dataSource.isWrapperFor(HikariDataSource.class)) {
                HikariDataSource hikari = dataSource.unwrap(HikariDataSource.class);
                HikariPoolMXBean mxBean = hikari.getHikariPoolMXBean();
                if (mxBean != null) {
                    return new TimelineStep(
                            stepName,
                            mxBean.getActiveConnections(),
                            mxBean.getIdleConnections(),
                            mxBean.getTotalConnections(),
                            mxBean.getThreadsAwaitingConnection()
                    );
                }
            }
        } catch (Exception e) {
            // Fallthrough
        }
        return new TimelineStep(stepName, -1, -1, -1, -1);
    }
}
