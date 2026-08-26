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
```
1. Entità Concettuali Principali
City (Città)
Definizione: L'entità aggregata principale che rappresenta l'intero sistema urbano in simulazione.

Responsabilità: Agisce come punto di riferimento centrale coordinando lo stato temporale, la struttura spaziale e le politiche attive.

CityState (Stato della Città)
Definizione: Il modello che incapsula la fotografia dinamica della città in un dato momento temporale (rappresentato dai ticks).

Responsabilità: Mantiene lo stato corrente, gestisce l'avanzamento del tempo, aggrega le metriche globali e orchestra le notifiche verso i componenti in ascolto (Observer).

Grid (Griglia / Mappa Urbana)
Definizione: La rappresentazione logica e spaziale del territorio cittadino (es. una matrice bidimensionale).

Responsabilità: Gestisce la disposizione topografica delle celle e la localizzazione delle infrastrutture e degli edifici nello spazio.

Cell (Cella / Entità Territoriale)
Definizione: L'unità elementare posizionata all'interno della griglia urbana. Può rappresentare un edificio specifico (Residenziale, Commerciale, Industriale) o un'infrastruttura/servizio (Strade, Parchi, Centrali Elettriche).

Responsabilità: Definisce l'impatto economico, sociale e ambientale sul sistema urbano.

Stats (Statistiche / Metriche Globali)
Definizione: Il set di indicatori di prestazione e benessere della città (es. Bilancio/Denaro, Popolazione, Felicità, Inquinamento, Energia).

Responsabilità: Quantificano lo stato di salute della città e cambiano dinamicamente in base all'interazione tra le celle e le politiche attive.

CityPolicy (Politica / Ordinanza Cittadina)
Definizione: Una linea guida strategica o normativa attivabile dall'utente (es. Tassa Ambientale vs Espansione Industriale).

Responsabilità: Modifica algoritmicamente il calcolo delle statistiche globali applicando regole di business specifiche (Strategy Pattern).

2. Relazioni Chiave del Dominio (Associations & Multiplicity)
City (1)  ---- possiede ---->  (1) CityState

Descrizione: Ogni città ha un unico stato corrente che ne definisce l'evoluzione temporale.

CityState (1)  ---- gestisce ---->  (1) Grid

Descrizione: Lo stato della città controlla la mappa territoriale su cui sono dislocate le risorse.

Grid (1)  ---- contiene ---->  (N) Cell

Descrizione: La griglia è composta da una matrice di celle territoriali (edifici o infrastrutture).

CityState (1)  ---- calcola/aggiorna ---->  (1) Stats

Descrizione: Lo stato aggrega e aggiorna costantemente le metriche globali della città a ogni ciclo temporale.

CityState (1)  ---- è influenzata da ---->  (0..1) CityPolicy

Descrizione: Lo stato della città applica una strategia di policy attiva per modulare l'impatto delle statistiche.

CityState (1)  ---- notifica ---->  (N) CityObserver

Descrizione: Il pattern Observer lega lo stato della città a componenti di visualizzazione o loggatori esterni per il tracciamento in tempo reale.
