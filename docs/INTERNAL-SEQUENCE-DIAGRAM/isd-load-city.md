sequenceDiagram
    autonumber
    actor Mayor as City Mayor
    participant Controller as GameController
    participant Persistence as CityPersistenceManager

    Mayor->>Controller: loadGame(filePath)
    Controller->>Persistence: loadCity(filePath)
    
    alt Valid save file
        Persistence-->>Controller: return City (loaded successfully)
        Controller-->>Mayor: City loaded successfully
    else Invalid or unreadable save file
        Persistence-->>Controller: return null (load failed)
        Controller-->>Mayor: Load failed
    end
