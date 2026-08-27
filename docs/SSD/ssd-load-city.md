```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: loadGame(filePath)
    activate System

    alt Valid save file
        System-->>Mayor: City loaded successfully
    else Invalid or unreadable save file
        System-->>Mayor: Load failed
    end

    deactivate System
```
