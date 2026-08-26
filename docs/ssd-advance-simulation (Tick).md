```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: advanceTime()

    activate System
    System->>System: Advance simulation Tick
    System->>System: Recalculate city metrics
    System->>System: Apply active City Policy
    System->>System: Update city state and UI
    deactivate System

    System-->>Mayor: Simulation advanced
```
