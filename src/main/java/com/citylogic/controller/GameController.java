package com.citylogic.controller;

import com.citylogic.model.Cell;
import com.citylogic.model.CellFactory;
import com.citylogic.model.City;
import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import com.citylogic.persistence.CityPersistenceManager;
import com.citylogic.simulation.CityObserver;
import com.citylogic.strategy.EnvironmentalTax;
import com.citylogic.strategy.IndustrialExpansion;

//gestisce la logica di business e fa da tramite tra i comandi dell'utente e il mototre di gioco
//protegge lo stato interno della città evitando accessi non controllati
public class GameController {

    private City city; //lo stato attuale della città gestita dal controller
    private final CityPersistenceManager persistenceManager; //gestore per salvataggio e caricamento dei file

    // La fabbrica appartiene al Controller, non alla View
    // Viene utilizzata per creare gli edifici richiesti dalla View
    // evitando che sia la View a occuparsi direttamente della loro istanziazione
    private final CellFactory cellFactory;

    // Protocollo di risposta per la GUI
    // Rappresenta i possibili risultati di una richiesta di costruzione
    // e permette alla View di mostrare il messaggio appropriato
    public enum BuildResult { SUCCESS, NO_FUNDS, INVALID_POSITION, UNKNOWN_TYPE }

    public GameController() {
        this.persistenceManager = new CityPersistenceManager();
        this.cellFactory = new CellFactory(); // Inizializza la CellFactory che verrà utilizzata dal Controller per creare le diverse tipologie di edifici
        //appena avviato il controller parte una nuova partita di default
        startNewGame();
    }

    //inizializza una nuova partita azzerando/creando una nuova città
    public void startNewGame(){
        this.city = new City();
        this.city.initCity();
        this.city.initNewGameBudget();//metodo della classe City che serve ad inizializzare il budget a 5000
    }

    // Gestisce l'intera transazione edilizia e finanziaria, isolando la GUI dalla logica
    public BuildResult placeBuilding(String buildingType, int x, int y) {

        if (city == null || city.getCityState() == null) { // Controlla che esistano una città e il relativo stato prima
            return BuildResult.INVALID_POSITION;           // di procedere con la costruzione
        }

        Cell cell;
        try {
            // Delega la creazione alla CellFactory,
            // in base al tipo richiesto dalla View
            cell = cellFactory.createCell(buildingType);
        } catch (IllegalArgumentException e) {
            return BuildResult.UNKNOWN_TYPE;
        }

        // Recupera il denaro attuale della città
        int currentMoney = getMoney();

        // Controlla che il budget disponibile sia sufficiente
        // per sostenere il costo dell'edificio
        if (currentMoney < cell.getCost()) {
            return BuildResult.NO_FUNDS;
        }

        // Imposta le coordinate dell'edificio prima di inserirlo nella griglia
        cell.setX(x);
        cell.setY(y);
        boolean placed = city.getCityState().getGrid().setCell(cell, x, y);

        if (placed) {
            // Se il posizionamento è avvenuto correttamente, scala dal budget il costo dell'edificio
            city.getCityState().getCityStats().setMoney(currentMoney - cell.getCost());

            // Notifica gli Observer che lo stato della città è cambiato,
            city.getCityState().notifyObservers();
            return BuildResult.SUCCESS;
        }
        // Se il posizionamento non è riuscito, comunica alla View che la posizione scelta non è valida
        return BuildResult.INVALID_POSITION;
    }


    // Gestisce la demolizione e il rimborso delle risorse
    public boolean demolishBuilding(int x, int y) {
        if (city == null || city.getCityState() == null) return false;

        // Tenta di estrarre la cella dalla griglia
        Cell removedCell = city.getCityState().getGrid().removeCell(x, y);

        if (removedCell != null) {
            int currentMoney = getMoney();
            // Rimborso del 100% del costo dell'edificio demolito
            city.getCityState().getCityStats().setMoney(currentMoney + removedCell.getCost());

            // Notifica la grafica dell'avvenuta demolizione e del nuovo budget
            city.getCityState().notifyObservers();
            return true;
        }

        return false; // Casella già vuota o coordinate non valide
    }

    //attiva una politica cittadina(Strategy pattern) modificando lo stato della città
    public void activatePolicy(String policyName) {
        // Controlla che esistano una città e il relativo stato prima di modificare la policy attiva
        if (city == null || city.getCityState() == null) {
            return;
        }

        if (policyName == null) {    // Se non viene specificata alcuna policy, rimuove quella attualmente attiva
            city.getCityState().setPolicy(null);
        } else if (policyName.equals("environmental")) {    // Se viene richiesta la politica ambientale, crea e attiva una nuova istanza di EnvironmentalTax
            city.getCityState().setPolicy(new EnvironmentalTax());
        } else if (policyName.equals("industrial")) {    // Se viene richiesta la politica industriale, crea e attiva una nuova istanza di IndustrialExpansion
            city.getCityState().setPolicy(new IndustrialExpansion());
        }
    }

    //fa avanzare il tempo di gioco di un tick(esempio un turno o un intervallo temporale)
    //delegando l'aggiornamento logico direttamente alla classe City
    public void advanceTime(){
        if(city != null) {
            city.processTick();
        }
    }

    //carica una partita salvata da file delegando il lavoro al PersistenceManager
    //sostituendo la città corrente con quella caricata
    public boolean loadGame(String filePath) {
        City loadedCity = persistenceManager.loadCity(filePath);
        if (loadedCity != null) {
            this.city = loadedCity;
            return true;
        }
        return false;
    }

    //restituisce le statistiche globali della città
    public Stats getCityStats() {
        return city.getCityState().getCityStats();
    }

    //restituisce la griglia del gioco attuale
    public Grid getGrid() {
        return city.getCityState().getGrid();
    }

    //restituisce il numero di tick corrente della simulazione
    public int getCurrentTick() {
        return city.getCityState().getCurrTick();
    }

    //restituisce  il budget disponibile
    public int getMoney() {
        return city.getCityState().getCityStats().getMoney();
    }
    
    // permette al Controller di modificare il budget della città
    // senza esporre direttamente CityState alla View o ad altre classi esterne
    public void setMoney(int money) {
        city.getCityState().getCityStats().setMoney(money);
    }

    //restituisce la strategia di politica cittadina attualmente attiva
    public CityPolicyStrategy getCurrentPolicy() {
        return city.getCityState().getCurrentPolicyStrategy();
    }
    
    // Restituisce alla View il nome della classe della policy attualmente attiva
    // senza obbligarla ad accedere direttamente all'oggetto CityPolicyStrategy
    public String getCurrentPolicyName() {
        CityPolicyStrategy policy = getCurrentPolicy();    // Recupera la policy attualmente attiva tramite il Controller

        if (policy == null) {
            return null;
        }

        return policy.getClass().getSimpleName();    // Restituisce il nome della classe della policy, ad esempio "EnvironmentalTax" o "IndustrialExpansion"
    }

    // Registra un Observer nel sistema di osservazione della città.
    // In questo modo la View può ricevere una notifica quando
    // lo stato della città cambia, senza accedere direttamente a CityState
    public void addObserver(CityObserver observer) {
        city.getCityState().addObserver(observer);
    }

    //salva lo stato corrente della città su file tramite PersistenceManager
    public boolean saveGame(String filePath){
        if(city != null){
            return persistenceManager.saveCity(city, filePath);
        }
        return false;
    }

    // innesca un game over se si va in bancarotta
    public boolean isBankrupt() {
        return city != null && city.getCityState().isBankrupt();
    }

    // innesca un game over se la felicità dei cittadini è troppo bassa
    public boolean isRevolt() {
        if (city != null && city.getCityState() != null && city.getCityState().getCityStats() != null) {
            // Game Over se la felicità scende a -100 o peggio
            return city.getCityState().getCityStats().getHappiness() <= -100;
        }
        return false;
    }

    // Espone il numero di case senza energia alla View
    public int getUnpoweredCount() {
        return city != null ? city.getCityState().getUnpoweredCount() : 0;
    }
}
