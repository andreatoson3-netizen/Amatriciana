```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: activatePolicy(policy)
    activate System

    System-->>Mayor: Policy activated

    deactivate System
```
