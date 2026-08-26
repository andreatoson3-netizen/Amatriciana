```mermaid
sequenceDiagram
    autonumber
    actor Utente as Utente / Dashboard
    participant GC as GameController
    participant City as City
    participant CS as CityState

    Utente->>GC: setCell(x, y, cellType)
    activate GC
    GC->>City: setCell(x, y, cellType)
    activate City
    City->>CS: updateGridAndCell(x, y, cellType)
    activate CS
    CS-->>City: Conferma aggiornamento
    deactivate CS
    City-->>GC: Operazione completata
    deactivate City
    GC-->>Utente: Stato aggiornato / Cella creata
    deactivate GC
