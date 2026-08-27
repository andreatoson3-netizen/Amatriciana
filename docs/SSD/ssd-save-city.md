```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: saveGame(filePath)
    activate System

    alt Save successful
        System-->>Mayor: City saved successfully
    else Save failed
        System-->>Mayor: Save failed
    end

    deactivate System
```
