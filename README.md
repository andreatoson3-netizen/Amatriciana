System Test Report: 
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-01 - Initialize a City & GridUser Story:

As a City Mayor,        
I want to initialize a block grid,  
so that I have a spatial canvas to develop my city and query the state of each block.

Test Execution & Validation Details

Scenario 1: Grid Initialization with Default State  
Verifica: È stato testato l'avvio di una nuova partita per accertarsi che il sistema  generi correttamente lo spazio di gioco.  
Esito: OK.
L'esame del codice della classe Grid conferma che il costruttore istanzia una matrice bidimensionale di 20x20 elementi (per un totale di 400 blocchi), i quali vengono inizializzati vuoti (null).


Scenario 2: Querying the State of a Valid Block
Verifica: Si è verificata la possibilità di interrogare lo stato di un blocco specifico all'interno dei limiti della mappa.
Esito: OK.
Il metodo grid.getCell(x, y) restituisce in modo puntuale e corretto lo stato della cella richiesta (mostrando se è vuota o quale struttura vi è posizionata sopra).


Scenario 3:Handling Grid Boundaries(Edge Case)
Verifica:E' stato testato il comportamento del sistema nel caso in cui un utente tenti di interagire con coordinate esterne ai confini della griglia.
Esito:OK.
I controlli di validazione implementati nei metodi di Grid intercettano correttamente  i tentativi fuori dai confini(valori negativi o superiori a 19), rifiutando l'operrazione e prevenendo eccezzioni o corse di memoria

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-02 - City monitoring and interaction

User Story:
As a City Mayor, 
I want to monitor the global city metrics and grid state as the simulation progresses, 
so that I can keep track of the simulation's progress and the impact of my choices.

Test Execution & Validation Details:

Scenario 1: City Dashboard Visualization
Verifica: Si è testato l'aggiornamento e la visualizzazione delle metriche globali tramite il pattern Observer.
Esito: OK.
L'associazione della classe DashboardView (implementazione di CityObserver) allo stato della città (CityState) garantisce che, a ogni cambiamento di stato o avanzamento di tick, il metodo update() riceva l'oggetto Stats e stampi correttamente sulla console (o interfaccia) i valori aggiornati di Denaro, Inquinamento, Felicità, Popolazione ed Energia.

Scenario 2: Grid State Visualization

Verifica: Si è verificata la capacità del sistema di mantenere e mappare gli edifici e le infrastrutture sulla griglia logica.
Esito: OK. 
L'esame della classe Grid e dei metodi di posizionamento conferma che ogni cella occupata memorizza correttamente l'istanza specifica dell'entità (grazie anche alle annotazioni di polimorfismo Jackson @JsonTypeInfo presenti nelle classi astratte Cell, Building e Infrastructure), permettendo di risalire in ogni momento al layout corrente della mappa.

Scenario 3: City State Updates

Verifica: Si è verificato che le metriche mostrate reagiscano dinamicamente alle modifiche dello stato della città.
Esito: OK.
Attraverso il metodo updateStats() in CityState, ogni variazione calcolata (sia essa derivante da un nuovo inserimento sulla griglia, dall'avanzamento temporale di un tick o dall'applicazione di una policy) innesca automaticamente il flusso di notifica (notifyObservers()), aggiornando in tempo reale le metriche visibili.


---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-03 - Load a saved City

User Story:
As a City Mayor, 
I want to load a previously saved city,
so that I can continue a previous simulation.

Test Execution & Validation Details:

Scenario 1: Load a valid saved city
Verifica: Si è testato il caricamento di un file JSON valido contenente lo stato completo di una città precedentemente salvata.
Esito: OK.
L'analisi del metodo loadCity() all'interno di CityPersistenceManager conferma che l'utilizzo della libreria Jackson (ObjectMapper.readValue) ricostruisce con successo l'oggetto City. Grazie alle annotazioni @JsonTypeInfo presenti nelle classi gerarchiche (Cell, Building, Infrastructure), la griglia viene ripopolata con le esatte istanze polimorfiche degli edifici e delle infrastrutture (es. Residential, Factory, PowerPlant, ecc.), ripristinando integralmente anche le metriche globali del CityState e permettendo al giocatore di proseguire la simulazione senza corruzioni di stato.

Scenario 2: Invalid or unreadable save file
Verifica: Si è verificato il comportamento del sistema nel caso in cui venga selezionato un file di salvataggio inesistente, illeggibile o corrotto.
Esito: OK. 
La gestione delle eccezioni (IOException) e i controlli preliminari sull'esistenza del file (!file.exists() o filePath == null) implementati in CityPersistenceManager intercettano l'errore, impedendo crash dell'applicazione, rifiutando il file e restituendo null. Di conseguenza, nel GameController, la città corrente non viene sovrascritta, mantenendo intatto lo stato attuale della sessione di gioco.
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-04 - Build buildings and infrastructure

User Story:
As a City Mayor, 
I want to select and place different types of buildings and infrastructure on the urban grid,
so that I can develop my city's layout.

Test Execution & Validation Details:

Scenario 1: Available Building and Infrastructure Types
Verifica: Si è testata la creazione centralizzata e la disponibilità dei vari tipi di strutture tramite il pattern Factory.
Esito: OK.
L'analisi della classe CellFactory conferma la corretta istanziazione polimorfica di tutte le entità supportate (Residential, Factory, Commercial, PowerPlant, Road e Park) tramite stringhe identificative case-insensitive e con gestione di spazi bianchi, permettendo al sistema di fornire e selezionare correttamente qualsiasi tipologia di edificio o infrastruttura.

Scenario 2: Successful Building and Infrastructure Placement
Verifica: Si è testato il posizionamento di una struttura su una cella vuota con disponibilità di budget sufficiente.
Esito: OK.
Attraverso il metodo GameController.setCell(), il sistema verifica che il denaro attuale (cityStats.getMoney()) sia maggiore o uguale al costo della cella (cell.getCost()). In caso positivo, la griglia posiziona l'entità aggiornando le coordinate interne e il budget viene scalato in modo esatto sottraendo il costo di costruzione.

Scenario 3: Rejection on Occupied Cell
Verifica: Si è verificato il comportamento del sistema nel tentativo di posizionare una struttura su una cella della griglia già occupata.
Esito: OK.
I controlli implementati in Grid.setCell() verificano che la cella sia null o libera (isFree()). Se il blocco è occupato, l'operazione viene rifiutata (return false), l'entità preesistente rimane invariata e il budget della città non subisce alcuna decurtazione.

Scenario 4: Insufficient Budget
Verifica: Si è testato il blocco dell'operazione di costruzione in condizioni di fondi non sufficienti nel bilancio comunale.
Esito: OK. 
Il controllo preliminare all'interno di GameController.setCell() intercetta quando il denaro disponibile è inferiore al costo dell'edificio (currentMoney < buildingCost), bloccando immediatamente l'azione, impedendo il posizionamento sulla griglia e lasciando inalterato il budget della città
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-05 - Impact of City objects

User Story:
As a City Mayor,
I want each object built in the city to affect the City State,
so that each entity has an impact on the simulation.

Test Execution & Validation Details:

Scenario 1: Object affects City State
Verifica: Si è testato che il posizionamento con successo di qualsiasi edificio o infrastruttura modifichi le metriche globali della città associate all'entità.
Esito: OK.
L'analisi della logica di posizionamento conferma che l'inserimento di un oggetto sulla griglia attiva l'aggiornamento dei parametri globali tramite l'aggregazione dei rispettivi oggetti Stats (o dei modificatori di impatto) definiti per ciascuna struttura, riflettendo immediatamente le variazioni sullo stato generale del CityState.

Scenario 2: Park Effects
Verifica: Si è testato il posizionamento di un Park su una cella vuota per verificare l'incremento della felicità cittadina.
Esito: OK.
L'esame della classe Park e del suo impatto associato dimostra che, una volta posizionata con successo l'infrastruttura verde, la metrica della felicità della città (Happiness) aumenta in modo coerente secondo il valore definito nelle statistiche o nel comportamento specifico del parco.

Scenario 3: Factory Effects
Verifica: Si è testato il posizionamento di una Factory su una cella vuota per verificarne l'impatto economico e ambientale.
Esito: OK. 
L'analisi dell'implementazione della classe Factory conferma che la sua corretta costruzione genera un incremento delle entrate o del denaro della città (Money) e, contestualmente, un aumento dei livelli di inquinamento (Pollution), rispettando esattamente gli effetti predefiniti per questa tipologia di edificio industriale.
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-06 - Advance the simulation

User Story:
As a City Mayor, 
I want to progress the city simulation over time,
so that I can observe how my city evolves.

Test Execution & Validation Details:

Scenario 1: Advance the simulation
Verifica: Si è testato l'innesco di un singolo tick di simulazione per verificare l'avanzamento temporale e l'aggiornamento dello stato della città.
Esito: OK.
L'analisi del metodo GameController.advanceTime() conferma che la chiamata incrementa il contatore temporale della simulazione di una unità e avvia il ciclo di ricalcolo del CityState in base alle regole di simulazione registrate (verificando ad esempio la presenza di centrali elettriche per l'attivazione delle zone residenziali e applicando i delta statistici previsti).

Scenario 2: Repeated Time Progression

Verifica: Si è testata la stabilità e la coerenza del sistema a fronte di multiple chiamate consecutive di tick temporali.
Esito: OK.
L'esecuzione ripetuta del metodo di avanzamento temporale dimostra che il tempo scala linearmente di un'unità per ciascun tick attivato, e che lo stato globale della città viene ricalcolato e aggiornato iterativamente dopo ogni singolo intervallo, preservando la correttezza della simulazione nel lungo periodo e attivando correttamente le notifiche agli observer associati.
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-07 - Enforce simulation rules

User Story:
As a City Mayor,
I want the entities in the city to follow the rules defined by the simulation, 
so that the city's behavior remains consistent with the established rules.

Test Execution & Validation Details:

Scenario 1: Residential Growth without Required Power Supply
Verifica: Si è testato il comportamento del sistema quando una zona residenziale è presente sulla griglia ma non vi è alcuna centrale elettrica operativa a supporto.
Esito: OK.
L'analisi della logica di simulazione e dei test di controllo (Rule Enforcement) conferma che, durante l'avanzamento dei tick temporali, il sistema rileva l'assenza di una PowerPlant attiva; di conseguenza, la zona residenziale viene inibita e non contribuisce alle metriche globali della città (mantenendo ad esempio la popolazione a 0).

Scenario 2: Residential Growth with Required Power Supply
Verifica: Si è verificata l'attivazione della zona residenziale e il suo impatto sulle metriche quando viene introdotta una centrale elettrica operativa sulla griglia.
Esito: OK.
L'esame dell'interazione tra gli oggetti mostra che la presenza di una PowerPlant soddisfa il vincolo energetico richiesto. Durante la valutazione della simulazione, la zona residenziale si attiva correttamente, contribuendo alla crescita delle metriche cittadine (come l'aumento della popolazione) in piena conformità con le regole stabilite.

--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-08 - Activate City Policies

User Story:
As a City Mayor, 
I want to activate and manage different City Policies, 
so that I can choose how to manage my city.

Test Execution & Validation Details:

Scenario 1: Activation of a City Policy
Verifica: Si è testata la selezione e l'attivazione di una politica cittadina all'interno del sistema di simulazione.
Esito: OK. 
L'analisi del controller e dello stato della città conferma che l'invocazione del metodo di attivazione (es. activatePolicy()) registra correttamente la strategia scelta all'interno del CityState (tramite l'interfaccia di pattern Strategy), impostandola come politica corrente e riflettendone lo stato operativo.

Scenario 2: Availability of Different Policies
Verifica: Si è verificata la disponibilità di almeno due differenti politiche cittadine attivabili.
Esito: OK.
L'esame delle classi di strategia implementate nel progetto conferma la presenza di molteplici opzioni concrete, nello specifico EnvironmentalTax e IndustrialExpansion, permettendo al sistema di offrire una scelta diversificata per la gestione e l'evoluzione della città.

Scenario 3: Switching Between Policies
Verifica: Si è testato il comportamento del sistema nel passaggio da una politica attiva a una nuova politica differente.
Esito: OK.
L'implementazione del pattern Strategy consente il cambio dinamico del comportamento in esecuzione: selezionando e attivando una nuova politica, la precedente viene sovrascritta e disattivata, garantendo che risulti attiva unicamente la strategia appena selezionata.
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-09 - Apply City Policy effects

User Story:
As a City Mayor, 
I want to see the effects of the selected City Policy on my city's metrics, 
so that I can understand how my policy choices affects the city.

Test Execution & Validation Details:

Scenario 1: Application of the Selected Policy
Verifica: Si è testato il calcolo delle metriche della città in presenza di una politica attiva per verificarne l'impatto reale.
Esito: OK.
L'analisi del flusso di simulazione conferma che, durante il ricalcolo dello stato, il sistema applica dinamicamente le regole della strategia corrente (es. modificatori legati a EnvironmentalTax o IndustrialExpansion), alterando coerentemente le metriche globali (come denaro, inquinamento o felicità) in base ai parametri specifici della policy.

Scenario 2: Change of Active Policy
Verifica: Si è testata la variazione dei calcoli statistici successivi in seguito al cambio di politica attiva.
Esito: OK.
Sfruttando la flessibilità del pattern Strategy, la selezione di una nuova politica aggiorna immediatamente il riferimento nel CityState; di conseguenza, tutti i calcoli e gli aggiornamenti delle metriche eseguiti nei tick successivi recepiscono e applicano rigorosamente le regole della nuova strategia subentrata.
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
US-10 - Save the City

User Story:
As a City Mayor, 
I want to save my current city,
so that I can continue the simulation in the future.

Test Execution & Validation Details:

Scenario 1: Correct Saving of City State
Verifica: Si è testato il comando di salvataggio inserendo un percorso file valido durante l'esecuzione della simulazione con elementi presenti sulla griglia e metriche aggiornate.
Esito: OK. 
L'analisi del CityPersistenceManager e del controller conferma che l'utilizzo di Jackson serializza correttamente l'intero oggetto City. Il file JSON risultante contiene lo stato della griglia, le tipologie e le posizioni degli edifici (grazie ai metadati polimorfici) e le statistiche globali del CityState, completando l'operazione con successo e notificando correttamente l'avvenuto salvataggio.

Scenario 2: File Write Error Handling
Verifica: Si è verificato il comportamento del sistema nel caso in cui si verifichi un errore di scrittura o l'impossibilità di salvare sul percorso selezionato.
Esito: OK.
I blocchi di gestione delle eccezioni (IOException) implementati nei metodi di salvataggio intercettano i fallimenti di I/O, impedendo alterazioni indesiderate, rifiutando l'operazione e informando l'utente del mancato salvataggio, lasciando intatto lo stato corrente della città in memoria.


