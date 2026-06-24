package dev.zengcode.features.lazyjdbc;

public record TimelineStep(
        String step,
        int activeConnections,
        int idleConnections,
        int totalConnections,
        int threadsAwaitingConnection
) {
}
