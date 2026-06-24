# Architecture Overview

```mermaid
flowchart TB
    GitHub[GitHub Repository] --> Docs[Documentation]
    GitHub --> Modules[Runnable Modules]
    Modules --> Infra[Docker Compose Infra]
    Docs --> Medium[Medium Summary]
    Docs --> YouTube[YouTube Walkthrough]
```
