```mermaid
sequenceDiagram
    autonumber
    actor Mayor as City Mayor
    participant Controller as GameController
    participant Persistence as CityPersistenceManager

    Mayor->>Controller: saveGame(filePath)
    
    alt city != null
        Controller->>Persistence: saveCity(city, filePath)
        
        alt Scrittura riuscita (Try block)
            Persistence->>Persistence: objectMapper.writeValue(new File(filePath), city)
            Persistence-->>Controller: Success
            Controller-->>Mayor: City saved successfully
        else Errore di I/O (Catch IOException)
            Persistence->>Persistence: e.printStackTrace()
            Persistence-->>Controller: Exception / Fail
            Controller-->>Mayor: Save failed
        end
        
    else city == null
        Controller-->>Mayor: Save failed (No active city)
    end
