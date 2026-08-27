System Test Report: 

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

Scenario 3: Handling Grid Boundaries (Edge Case)
Verifica: È stato testato il comportamento del sistema nel caso in cui un utente tenti di interagire con coordinate esterne ai confini della griglia.
Esito: OK.
I controlli di validazione implementati nei metodi di Grid intercettano correttamente i tentativi fuori dai confini (valori negativi o superiori a 19), rifiutando l'operazione e prevenendo eccezioni o corse di memoria
