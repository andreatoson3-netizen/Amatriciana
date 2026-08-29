
```mermaid
sequenceDiagram
    autonumber
    actor System as Simulation
    participant Controller as GameController
    participant CityObj as City
    participant State as CityState
    participant TheGrid as Grid
    participant Observer as CityObserver (Dashboard)

    System->>Controller: advanceTime()
    Controller->>CityObj: processTick()
    CityObj->>State: processTick()

    Note over State: Incrementa currTick e avvia il calcolo delle statistiche

    State->>TheGrid: calculateRawStats()

    TheGrid->>TheGrid: calculate statistics for grid entities

    alt Residential without nearby Power Plant
        Note over TheGrid: Residential non contribuisce alle metriche
        TheGrid-->>State: return totalStats
    else Residential with nearby Power Plant
        Note over TheGrid: Residential contribuisce alle metriche secondo i suoi effetti
        TheGrid-->>State: return totalStats
    end

    State->>State: updateStats(rawStats)
    State->>Observer: notifyObservers()
    Observer->>Observer: update(cityStats)

    State-->>CityObj: Tick completed
    CityObj-->>Controller: Tick completed
    Controller-->>System: Simulation advanced
```
