# Lazy JDBC Connection Fetching

## Objective
Demonstrate the benefits of Lazy JDBC Connection Fetching in Spring Boot 4.1.

> [!WARNING]
> **Important Note:**
> This repository uses `LazyConnectionDataSourceProxy` to **simulate** the expected Lazy JDBC Connection Fetching behavior. The exact native Spring Boot 4.1 configuration should be verified against the official Spring Boot 4.1 documentation when upgrading.

## Purpose & Expectation
In standard Spring applications, when a method is annotated with `@Transactional`, the underlying transaction manager immediately fetches a JDBC connection from the connection pool (like HikariCP), even before any database queries are actually executed.

If a transaction contains a slow operation *before* the first database call (e.g., an external API call, a complex calculation), the JDBC connection sits idle in the transaction while the pool's capacity is tied up. This leads to connection pool exhaustion under high load.

**Lazy JDBC Connection Fetching** delays acquiring the JDBC connection from the pool until the exact moment a database query is executed. 

> [!IMPORTANT]
> **Deep Architectural Insight:** 
> If your project mainly uses **Spring Data JPA** with Hibernate, you may not see a big difference in many common scenarios when toggling lazy fetching. That's because Hibernate can delay physical connection acquisition in many common JPA flows natively!
> 
> However, the actual behavior depends on Hibernate connection handling mode, transaction manager, provider configuration, and how the application accesses the database.
> 
> The `spring.datasource.connection-fetch: lazy` feature expected in Spring Boot 4.1 aims to make this easier primarily for applications using **Spring JDBC (JdbcTemplate), MyBatis, or jOOQ**. These frameworks traditionally suffer from Eager Fetching when wrapping methods in `@Transactional`. This brings the benefits often naturally enjoyed by JPA flows down to the rest of the ecosystem!
## How to Run
1. Ensure the PostgreSQL database is running via `docker compose`:
   ```bash
   docker compose -f infra/docker-compose.yml up postgres -d
   ```
2. Start this module.
3. Use Postman or a browser to hit the endpoints. The API will return a **JSON Timeline** showing the exact number of active connections in the pool at each step of the transaction.

## Scenario Comparison

| Scenario | External Call Before DB | DB Access | Expected Active Connection During External Call |
|----------|-------------------------|-----------|-------------------------------------------------|
| `/lazy-jdbc/no-db` | Yes | No | **0** (Connection never acquired) |
| `/lazy-jdbc/external-then-db` | Yes (2s delay) | Yes (after delay) | **0** (Acquired *after* delay) |
| `/lazy-jdbc/db-first` | Yes (2s delay) | Yes (before delay) | **1** (Pool blocked during delay) |

## Example JSON Response (`/lazy-jdbc/external-then-db`)

```json
{
  "scenario": "external-then-db",
  "description": "Simulates a slow 2-second external API call BEFORE hitting the database.",
  "timeline": [
    { "step": "transaction-started", "activeConnections": 0, "idleConnections": 2, "totalConnections": 2, "threadsAwaitingConnection": 0 },
    { "step": "before-external-call", "activeConnections": 0, "idleConnections": 2, "totalConnections": 2, "threadsAwaitingConnection": 0 },
    { "step": "after-external-call", "activeConnections": 0, "idleConnections": 2, "totalConnections": 2, "threadsAwaitingConnection": 0 },
    { "step": "before-database-access", "activeConnections": 0, "idleConnections": 2, "totalConnections": 2, "threadsAwaitingConnection": 0 },
    { "step": "after-database-access", "activeConnections": 1, "idleConnections": 1, "totalConnections": 2, "threadsAwaitingConnection": 0 },
    { "step": "transaction-ended", "activeConnections": 0, "idleConnections": 2, "totalConnections": 2, "threadsAwaitingConnection": 0 }
  ],
  "takeaway": "Active connections remain 0 during the external call. The connection is ONLY acquired when the database is actually accessed, preventing pool exhaustion."
}
```

> [!NOTE]
> Since this demo uses Spring Data JPA, `activeConnections` will usually efficiently stay at `0` during the external call, regardless of the `connection-fetch` configuration, proving how Hibernate handles logical connections under the hood!
