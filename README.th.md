# Spring Boot 4.1 Features Deep Dive

> Repository นี้ใช้มาตรฐาน **ZengCode Engineering Playbook Standard (ZEPS) v1.0**

Repository นี้ไม่ได้ทำขึ้นเพื่อ Demo Feature เฉย ๆ แต่ตั้งใจให้เป็น Engineering Knowledge Base ที่อธิบาย Spring Boot 4.1 แบบใช้จริงในงาน Backend / Production

🇺🇸 English documentation: [README.md](README.md)

## Objective

- อธิบาย Feature ของ Spring Boot 4.1 จากมุม Software Engineering
- มี Code ตัวอย่างที่รันได้จริง
- ใช้ GitHub เป็น Source of Truth แล้วค่อยต่อยอดเป็น Medium, LinkedIn และ YouTube

## Modules

| Module | Feature | Documentation |
|---|---|---|
| `01-lazy-jdbc-connection-fetching` | Lazy JDBC Connection Fetching | [EN](docs/en/01-lazy-jdbc-connection-fetching.md) / [TH](docs/th/01-lazy-jdbc-connection-fetching.md) |
| `02-spring-grpc` | Spring gRPC Support | [EN](docs/en/02-spring-grpc.md) / [TH](docs/th/02-spring-grpc.md) |
| `03-http-client-ssrf-mitigation` | HTTP Client SSRF Mitigation | [EN](docs/en/03-http-client-ssrf-mitigation.md) / [TH](docs/th/03-http-client-ssrf-mitigation.md) |
| `04-observability-opentelemetry` | OpenTelemetry & Observability | [EN](docs/en/04-observability-opentelemetry.md) / [TH](docs/th/04-observability-opentelemetry.md) |
| `05-redis-listener` | Redis Listener Auto Configuration | [EN](docs/en/05-redis-listener.md) / [TH](docs/th/05-redis-listener.md) |
| `06-jackson-configuration` | Jackson Configuration Improvements | [EN](docs/en/06-jackson-configuration.md) / [TH](docs/th/06-jackson-configuration.md) |
| `07-actuator-info` | Actuator Info Improvements | [EN](docs/en/07-actuator-info.md) / [TH](docs/th/07-actuator-info.md) |
| `08-async-context-propagation` | Async Context Propagation | [EN](docs/en/08-async-context-propagation.md) / [TH](docs/th/08-async-context-propagation.md) |

## Quick Start

```bash
mvn clean verify
mvn -pl modules/01-lazy-jdbc-connection-fetching spring-boot:run
cd infra && docker compose up -d
```
