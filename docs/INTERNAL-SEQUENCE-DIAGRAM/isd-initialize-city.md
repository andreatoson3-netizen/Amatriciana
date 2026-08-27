sequenceDiagram
    autonumber
    actor Mayor as City Mayor
    participant Controller as GameController
    participant CityObj as City
    participant State as CityState
    participant TheGrid as Grid

    Note over Mayor, TheGrid: Scenario: startNewGame()
    Mayor->>Controller: startNewGame()
    Controller->>CityObj: new City() & initCity()
    CityObj->>State: new CityState()
    State->>TheGrid: new Grid() (20x20 matrix)
    TheGrid-->>State: return Grid
    State-->>CityObj: return CityState
    CityObj-->>Controller: return City initialized
    Controller-->>Mayor: New city initialized & Display city grid and stats
