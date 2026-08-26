```mermaid
sequenceDiagram
    autonumber
    actor Utente as Utente / Dashboard
    participant GC as GameController
    participant City as City
    participant CS as CityState
    participant Grid as Grid

    Utente->>GC: setCell(cell)
    activate GC
    GC->>City: setCell(cell)
    activate City
    City->>CS: getGrid()
    activate CS
    CS-->>City: grid
    deactivate CS
    City->>Grid: setCell / aggiorna cella
    activate Grid
    Grid-->>City: Conferma
    deactivate Grid
    City-->>GC: Completato
    deactivate City
    GC-->>Utente: Cella posizionata
    deactivate GC
