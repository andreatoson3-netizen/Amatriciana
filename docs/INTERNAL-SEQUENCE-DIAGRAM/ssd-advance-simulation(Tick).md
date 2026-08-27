
```mermaid
sequenceDiagram
    autonumber
    actor Utente as City Major 
    participant GC as GameController
    participant City as City
    participant CS as CityState
    participant Grid as Grid
    participant Obs as CityObserver

    Utente->>GC: advanceTime()
    activate GC
    GC->>City: processTick()
    activate City
    City->>CS: processTick()
    activate CS
    
    Note over CS: Incrementa currTick
    CS->>Grid: calculateRawStats()
    activate Grid
    Grid-->>CS: Stats grezze
    deactivate Grid
    
    Note over CS: Applica eventuale Policy (Strategy)
    CS->>CS: updateStats(rawStats)
    CS->>Obs: notifyObservers(cityStats)
    activate Obs
    Obs-->>CS: Aggiornato
    deactivate Obs
    
    CS-->>City: Tick elaborato
    deactivate CS
    City-->>GC: Successo
    deactivate City
    GC-->>Utente: Simulazione avanzata
    deactivate GC
