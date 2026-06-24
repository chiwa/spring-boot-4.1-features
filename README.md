# Spring Boot 4.1 Features Deep Dive

> This repository follows **ZengCode Engineering Playbook Standard (ZEPS) v1.0**.

This repository is a production-oriented engineering knowledge base for Spring Boot 4.1 features. It is not only a demo project. It explains why each feature exists, what engineering problem it solves, how it works, and when it should or should not be used in production.

🇹🇭 Thai documentation: [README.th.md](README.th.md)

## Objectives

- Explain Spring Boot 4.1 features from an engineering perspective.
- Provide runnable code examples for each feature.
- Build a GitHub-first learning asset that can later become Medium articles, LinkedIn posts, and YouTube episodes.

## Feature Modules

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

# Run one module
mvn -pl modules/01-lazy-jdbc-connection-fetching spring-boot:run

# Start infrastructure
cd infra
docker compose up -d
```

## Repository Structure

```text
spring-boot-4.1-features/
├── README.md
├── README.th.md
├── docs/
│   ├── en/
│   └── th/
├── modules/
├── diagrams/
├── infra/
├── postman/
├── benchmark/
├── medium/
├── linkedin/
└── youtube/
```

## References

- Spring Boot 4.1 Release Notes
- Spring Boot 4.1 announcement blog
- Spring Framework reference documentation
