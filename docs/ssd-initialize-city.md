```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: startNewGame()
    activate System

    System-->>Mayor: New city initialized
    System-->>Mayor: Display city grid and stats

    deactivate System
```
