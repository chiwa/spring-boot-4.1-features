# YouTube Playlist Outline

1. Repository Overview and ZEPS Standard
2. Lazy JDBC Connection Fetching
   - Eager Fetching Problem (Connection Pool Exhaustion)
   - Expected Spring Boot 4.1 solution: `spring.datasource.connection-fetch: lazy`
   - *Deep Architectural Insight*: How JPA/Hibernate users may already behave lazily in common flows, and why this feature is a game-changer for Spring JDBC, MyBatis, and jOOQ users.
3. Spring gRPC Support
4. HTTP Client SSRF Mitigation
5. Observability and OpenTelemetry
6. Redis Listener Auto Configuration
7. Jackson Configuration Improvements
8. Actuator Info Improvements
9. Async Context Propagation
10. Migration Guide and Production Checklist
