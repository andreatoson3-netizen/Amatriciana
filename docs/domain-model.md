 ```mermaid
      classDiagram
    direction LR

    class City {
        +initCity()
        +getCityState()
        +processTick()
    }

    class CityState {
        -currTick : int
        +updateStats()
        +processTick()
        +addObserver()
        +notifyObservers()
    }

    class Grid {
        +getGriglia()
    }

    class Cell {
        -x : int
        -y : int
        +getStats()
    }

    class Stats {
        -money : int
        -happiness : int
        -pollution : int
        -population : int
        -energy : int
        +add()
    }

    class CityPolicy {
        <<interface>>
        +calculateStats()
    }

    class CityObserver {
        <<interface>>
        +update()
    }

    City "1" *-- "1" CityState : possiede
    CityState "1" --> "1" Grid : gestisce
    Grid "1" o-- "*" Cell : contiene
    CityState "1" --> "1" Stats : aggrega / calcola
    CityState "1" --> "0..1" CityPolicy : applica strategia
    CityState "1" --> "*" CityObserver : notifica
