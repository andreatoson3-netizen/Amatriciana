```mermaid
sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: advanceTime()
    activate System
    System-->>Mayor: Game Over - Bankruptcy
    deactivate System

    alt Start a new game
        Mayor->>System: startNewGame()
        activate System
        System-->>Mayor: New game started
        deactivate System
    else Load a saved game
        Mayor->>System: loadGame()
        activate System
        System-->>Mayor: Saved game loaded
        deactivate System
    end
