```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: viewCityState()
    activate System

    System-->>Mayor: Display city grid and city metrics

    deactivate System

    Mayor->>System: selectBlock(position)
    activate System

    System-->>Mayor: Display selected block state

    deactivate System
```
