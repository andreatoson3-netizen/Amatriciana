```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: advanceTime()

    activate System
    System-->>Mayor: Simulation advanced
    deactivate System
```
