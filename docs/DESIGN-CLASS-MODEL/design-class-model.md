```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% 1. DEFINIZIONE CLASSI E INTERFACCE
    %% ==========================================

    class GameController {
        -City city
        -CityPersistenceManager persistenceManager
        +startNewGame() : void
        +setCell(Cell cell) : boolean
        +activatePolicy(CityPolicyStrategy policy): void
        +advanceTime() : void
        +loadGame(String filePath) : boolean
        +saveGame(String filePath): boolean
        +getCity() : City
    }

    class CellFactory {
        +createCell(String cellType):  Cell
    }

    class CityPersistenceManager {
        -ObjectMapper objectMapper
        +saveCity(City city, String filePath): boolean
        +loadCity(String filePath) :City
    }

    class CityObserver {
        <<interface>>
        +update(Stats currentStats): void
    }


    class City {
        -CityState cityState
        +initCity() void
        +initNewGameBudget(): void
        +getCityState():CityState
        +processTick(): void
    }
    
    class CityState {
        -CityPolicyStrategy currentPolicy
        -int currTick
        -Stats cityStats
        -Grid grid
        -List~CityObserver~ observers
        +updateStats(Stats newStats): void
        +processTick(): void
        +addObserver(CityObserver observer): void
        +removeObserver(CityObserver observer):void
         +notifyObservers(): void
        +getCityStats() Stats
        +setCityStats(Stats cityStats):void
        +getGrid() :Grid
        +setGrid(Grid grid):Grid
        +getCurrTick() :int
        +setCurrTick(): int
        +getCurrentPolicyStrategy():CityPolicyStrategy
         +setPolicy(CityPolicyStrategy p): void
        
       
    }

    class CityPolicyStrategy {
        <<interface>>
        +calculateStats(Stats rawStats) Stats
    }

    class EnvironmentalTax {
        +calculateStats(Stats rawStats) Stats
    }

    class IndustrialExpansion {
        +calculateStats(Stats rawStats) Stats
    }

    class Grid {
        -Cell[][] griglia
        +getCell(int x, int y) : Cell
        +hasPowerPlant : boolean
        +hasNearbyPowerPlant(int x, int y):boolean
        +countUnpoweredResidentila(): int
        +calculateRawStats() : Stats
        +setCell(Cell cell,int x,int y): boolean
        +getGriglia():Cell[][]
        +setGriglia(Cell[][] griglia):void
        
    }

    class Cell {
        <<abstract>>
        -boolean free
        -boolean isOperative
        -int x
        -int y
        -int cost
        +isFree(): boolean
        +returnStat(): Stats
        +getFree() : boolean
        +setFree(boolean free):void
        +isOperative():boolean
        +setOperative(boolean operative):void
        +getX():int
        +setX(int x):void
        +getY():int
        +setY(int y):void
        +getCost():int
        +setCost(int cost):void
        
    }

    class Stats {
        -int pollution
        -int money
        -int happiness
        -int population
        -int energy
        +add(Stats other) :void
        +multiply(double factor):Stats
        +getPollution() int
        +setPollution(int pollution):void
        +getMoney() int
        +setMoney(int money):void
        +getHappiness() int
        +setHappiness(int happiness):void
        +getPopulation() int
        +setPopulation(int population):void
        +getEnergy() int
        +setEnergy(int energy):void
        
   }

    class Infrastructure {
        <<abstract>>
        +returnStat() : Stats
    }
    
    class Building {
        <<abstract>>
      +returnStat():Stats
    }

    class PowerPlant {
        +returnStat() : Stats
    }
    
    class Road {
        +returnStat(): Stats
    }
    
    class Park {
        +returnStat(): Stats
    }

    class Residential {
        +returnStat(): Stats
    }
    
    class Factory {
        +returnStat() Stats
    }
    
    class Commercial {
        +returnStat() Stats
    }

    %% ==========================================
    %% 2. RELAZIONI, ASSOCIAZIONI ED EREDITARIETÀ
    %% ==========================================

    GameController --> City : gestisce
    
    %% Relazioni Pattern e Gestori Esterni
    GameController ..> CellFactory : delega creazione a
    GameController ..> CityPersistenceManager : delega I/O a
    CellFactory ..> Cell : istanzia

    City --> CityState : possiede
    City --> Grid : 1 rappresentata da
    Grid --> Cell : composta da
    
    %% Observer Pattern
    CityObserver <|.. DashboardView : realizza
    CityState o-- "*" CityObserver : notifica
    
    %% CityState possiede le statistiche attuali della città
    CityState "1" *-- "1" Stats : contiene

    %% Cell crea le statistiche per il calcolo del delta
    Cell ..> Stats : crea / restituisce

    CityState o-- CityPolicyStrategy : utilizza
    CityPolicyStrategy <|.. EnvironmentalTax : realizza
    CityPolicyStrategy <|.. IndustrialExpansion : realizza

    Cell <|-- Infrastructure
    Cell <|-- Building

    Infrastructure <|-- PowerPlant
    Infrastructure <|-- Road
    Infrastructure <|-- Park

    Building <|-- Residential
    Building <|-- Factory
    Building <|-- Commercial
```

L'architettura del sistema è stata progettata per mantenere i componenti separati, facili da testare e pronti per future espansioni, applicando i principi GRASP (Alta Coesione, Basso Accoppiamento) e i design pattern della Gang of Four (GoF).

Core Engine e Modello di Dominio

City & CityState: City è la classe base che avvia il simulatore. Per evitare che diventi troppo complessa ("God Object"), delega tutta la memoria del gioco (turno corrente, statistiche, politiche attive) alla classe CityState.

Grid & Cell: La mappa spaziale è gestita da Grid. Invece di avere matrici separate per ogni tipo di edificio, la griglia usa la classe astratta Cell. Questo permette alla mappa di iterare su tutti gli edifici in modo polimorfico, chiamando il metodo generico returnStat() senza dover sapere esattamente quale edificio sta elaborando.

Stats: Funziona come un Data Transfer Object (DTO). Incapsula tutte le variabili (soldi, inquinamento, energia) rendendole private, ed espone metodi getter sicuri e metodi matematici interni (add, multiply) per impedire che altre classi alterino i dati in modo imprevisto.

Design Pattern Implementati

Model-View-Controller (GameController): Il sistema isola la logica di business dall'interfaccia grafica. Il GameController gestisce gli input dell'utente (es. piazzare un edificio, attivare una politica) e li traduce in comandi per il motore di gioco, proteggendo lo stato interno di City.

Factory Pattern (CellFactory): Il compito di istanziare gli edifici fisici è delegato a una fabbrica dedicata. Quando il Controller deve piazzare un edificio, non chiama il costruttore concreto (es. new Park()), ma chiede alla CellFactory di restituirgli una generica Cell. Questo rende il codice flessibile: aggiungere nuovi edifici in futuro richiederà modifiche solo alla Factory.

Observer Pattern (CityObserver & DashboardView): Garantisce il flusso dei dati verso lo schermo senza bloccare il motore. A ogni fine turno, CityState (il Soggetto) notifica gli osservatori registrati (es. DashboardView) inviando l'oggetto Stats aggiornato. In questo modo il motore non dipende dalla specifica tecnologia grafica usata (Swing, Web, ecc.).

Strategy Pattern (CityPolicyStrategy): Le normative cittadine (es. Tasse, Espansione Industriale) alterano il calcolo dei punteggi. Invece di riempire il motore con blocchi if/else, ogni politica è una classe separata che implementa la stessa interfaccia. Le regole possono così essere scambiate a runtime in modo trasparente.

Pure Fabrication (CityPersistenceManager): Per mantenere l'Alta Coesione e rispettare il principio di Singola Responsabilità (SRP), la logica di I/O (lettura e scrittura dei file JSON per i salvataggi) è stata completamente rimossa da City e isolata in un gestore dedicato.
