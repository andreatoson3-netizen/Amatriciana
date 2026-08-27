```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: activatePolicy(policy)
    activate System

    alt No policy currently active
        System-->>Mayor: Policy activated
    else Another policy is already active
        System-->>Mayor: Policy changed
    end

    deactivate System
```
