```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% 1. DEFINIZIONE CLASSI E INTERFACCE
    %% ==========================================

   class BuildResult {
        <<enumeration>>
        SUCCESS
        NO_FUNDS
        INVALID_POSITION
        UNKNOWN_TYPE
    }

    %% Questa linea indica che l'enum è annidato / appartiene a GameController
    GameController *-- BuildResult : +BuildResult


    class GameController {
        -City city
        -CityPersistenceManager persistenceManager
        -CellFactory cellFactory
        +startNewGame() : void
        +placeBuilding(String buildingType,int x,int y):BuildResult
        +demolishBuilding(int x,int y):boolean
        +activatePolicy(String policyName): void
        +advanceTime() : void
        +loadGame(String filePath) : boolean
        +getCityStats():Stats
        +getGrid():Grid
        +getCurrentTick(): int
        +getMoney():int
        +setMoney(int money):void
        +getCurrentPolicy():CityPolicyStrategy
        +getCurrentPolicyName():String
        +addObserver(CityObserver observer):void
        +saveGame(String filePath):boolean
        +isBankrupt():boolean
        +getUnpoweredCount():int
        +getBlackoutCount():int
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


    class CityDashboard {
        -GameController controller
        -JButton[][] gridButtons
        -JLabel moneyLabel
        -JLabel pollutionLabel
        -JLabel happinessLabel
        -JLabel populationLabel
        -JLabel energyLabel
        -JLabel tickLabel
        -String selectedBuilding
        -JButton noPolicyButton
        -JButton environmentalButton
        -JButton industrialButton
        -JButton houseButton
        -JButton factoryButton
        -JButton commercialButton
        -JButton parkButton
        -JButton roadButton
        -JButton powerPlantButton
        -JButton tickButton
        -JButton demolishButton
        +CityDashboard(GameController controller)
        -initializeWindow() void
        -createGUI() void
        -createTitlePanel() JPanel
        -createStatsPanel() JPanel
        -createGridPanel() JPanel
        -createCommandsPanel() JPanel
        -buildOnCell(int x, int y) void
        -handleNewGame() void
        -handleSaveGame() void
        -handleLoadGame() void
        -updatePolicyButtons() void
        -refreshGrid() void
        -refreshStats(Stats stats) void
        -setGameControlsEnabled(boolean enabled) void
        +update(Stats currentStats) void
    }

   

    class JFrame {
        <<Java Swing>>
    }

    JFrame <|-- CityDashboard : extends
    CityObserver <|.. CityDashboard : implements
    CityDashboard --> GameController : uses


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
        +getCityStats() :Stats
        +setCityStats(Stats cityStats):void
        +getGrid() :Grid
        +setGrid(Grid grid):void
        +getCurrTick() :int
        +setCurrTick(int currTick): void
        +getCurrentPolicyStrategy():CityPolicyStrategy
        +setPolicy(CityPolicyStrategy p): void
        +isBankrupt():boolean
        +getUnpoweredCount():int
        +getBlackoutCount(): int
       
    }

    class CityPolicyStrategy {
        <<interface>>
        +calculateStats(Stats rawStats): Stats
    }

    class EnvironmentalTax {
        +calculateStats(Stats rawStats): Stats
    }

    class IndustrialExpansion {
        +calculateStats(Stats rawStats): Stats
    }

    class Grid {
        -Cell[][] griglia
        --Queue~Cell~ blackoutQueue
        +getCell(int x, int y) : Cell
        +hasPowerPlant() :boolean
        -hasNearbyPowerPlant(int x, int y):boolean
        +countUnpoweredResidential(): int
        +getBlackoutCount():int
        +distributeEnergy():void
        +calculateRawStats() : Stats
        +removeCell(int x,int y):Cell
        +setCell(Cell cell,int x,int y): boolean
        +getBlackoutQueue():Queue~Cell~
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
        +getPollution() :int
        +setPollution(int pollution):void
        +getMoney() :int
        +setMoney(int money):void
        +getHappiness(): int
        +setHappiness(int happiness):void
        +getPopulation() :int
        +setPopulation(int population):void
        +getEnergy(): int
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
        +returnStat(): Stats
    }
    
    class Commercial {
        +returnStat(): Stats
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
    CityState --> Grid : gestisce
    Grid --> Cell : composta da
    
    %% Observer Pattern
    CityState -- "*" CityObserver : notifica

    JFrame <|-- CityDashboard : estende
    CityObserver <|.. CityDashboard : implementa
    CityDashboard --> GameController : usa

    
    %% CityState possiede le statistiche attuali della città
    CityState "1" *-- "1" Stats : contiene

    %% Cell crea le statistiche per il calcolo del delta
    Cell ..> Stats : crea / restituisce

    CityState "1" --> "0..1" CityPolicyStrategy : utilizza
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
---

The system architecture has been designed to keep components separate, easy to test, and ready for future expansions, applying GRASP principles (High Cohesion, Low Coupling) and Gang of Four (GoF) design patterns.

Core Engine and Domain Model

City & CityState: City represents the city and coordinates the startup and processing of the simulation. To prevent it from becoming too complex ("God Object"), it delegates simulation state management to the CityState class.

Grid & Cell: The spatial map is managed by Grid. Instead of having separate arrays for each type of building, the grid uses the abstract Cell class. This allows the map to iterate over all buildings polymorphically by calling the generic returnStat() method without needing to know the exact building it is processing.

Stats: Encapsulates the main city metrics (Money, Population, Happiness, Pollution, and Energy), keeping the related attributes private and providing getters, setters, and operations to combine and modify the values.

Implemented Design Patterns

Model-View-Controller (GameController): The system isolates business logic from the graphical interface. The GameController handles user input (e.g., placing a building, activating a policy) and translates it into commands for the game engine, protecting City's internal state.

Factory Pattern (CellFactory): The task of instantiating physical buildings is delegated to a dedicated factory. When the Controller needs to place a building, it does not call the concrete constructor (e.g., new Park()), but asks CellFactory to return a generic Cell. This makes the code flexible: adding new buildings in the future will require modifications only to the Factory.

Observer Pattern (CityObserver): Guarantees data flow to the screen without blocking the engine. At the end of each turn, CityState (the Subject) notifies registered observers by sending the updated Stats object. This way, the engine does not depend on the specific graphical technology used (Swing, Web, etc.).

Strategy Pattern (CityPolicyStrategy): City regulations (e.g., Taxes, Industrial Expansion) alter score calculations. Instead of cluttering the engine with if/else blocks, each policy is a separate class implementing the same interface. Rules can thus be swapped transparently at runtime.

Pure Fabrication (CityPersistenceManager): To maintain High Cohesion and respect the Single Responsibility Principle (SRP), I/O logic (reading and writing JSON save files) has been completely removed from City and isolated in a dedicated manager

