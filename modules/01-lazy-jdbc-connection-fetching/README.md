# Lazy JDBC Connection Fetching

## Objective
Demonstrate the benefits of Lazy JDBC Connection Fetching in Spring Boot 4.1.

> **Note:** To simulate the upcoming native Spring Boot 4.1 behavior (`spring.datasource.connection-fetch: lazy`), this module employs a custom `LazyDataSourceConfig` that wraps the Hikari `DataSource` in a `LazyConnectionDataSourceProxy`. This perfectly mimics how the framework will delay acquiring physical connections until actual SQL execution.

## Purpose & Expectation
In standard Spring applications, when a method is annotated with `@Transactional`, the underlying transaction manager immediately fetches a JDBC connection from the connection pool (like HikariCP), even before any database queries are actually executed.

If a transaction contains a slow operation *before* the first database call (e.g., an external API call, a complex calculation), the JDBC connection sits idle in the transaction while the pool's capacity is tied up. This leads to connection pool exhaustion under high load.

**Lazy JDBC Connection Fetching** delays acquiring the JDBC connection from the pool until the exact moment a database query is executed. 

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
This timeline visually proves that the JDBC connection was safely resting in the pool (`activeConnections: 0`) while the slow 2-second external API call was executing, unlocking massive scalability improvements!
