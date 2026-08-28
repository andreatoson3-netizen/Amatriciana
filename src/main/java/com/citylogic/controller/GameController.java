package com.citylogic.controller;

import com.citylogic.model.Cell;
import com.citylogic.model.CellFactory; // Aggiunto import
import com.citylogic.model.City;
import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import com.citylogic.persistence.CityPersistenceManager;

//gestisce la logica di business e fa da tramite tra i comandi dell'utente e il mototre di gioco
//protegge lo stato interno della città evitando accessi non controllati
public class GameController {

    private City city; //lo stato attuale della città gestita dal controller
    private final CityPersistenceManager persistenceManager; //gestore per salvataggio e caricamento dei file

    // La fabbrica appartiene al Controller, non alla View
    private final CellFactory cellFactory;

    // Protocollo di risposta per la GUI
    public enum BuildResult { SUCCESS, NO_FUNDS, INVALID_POSITION, UNKNOWN_TYPE }

    public GameController() {
        this.persistenceManager = new CityPersistenceManager();
        this.cellFactory = new CellFactory(); // Inizializzazione fabbrica
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

        if (city == null || city.getCityState() == null) {
            return BuildResult.INVALID_POSITION;
        }

        Cell cell;
        try {
            // Delega la creazione alla factory
            cell = cellFactory.createCell(buildingType);
        } catch (IllegalArgumentException e) {
            return BuildResult.UNKNOWN_TYPE;
        }

        // Recupera il denaro attuale
        int currentMoney = getMoney();

        // Controllo del budget
        if (currentMoney < cell.getCost()) {
            return BuildResult.NO_FUNDS;
        }

        // Prova a posizionare
        cell.setX(x);
        cell.setY(y);
        boolean placed = city.getCityState().getGrid().setCell(cell, x, y);

        if (placed) {
            // Se posizionata, scala i soldi
            city.getCityState().getCityStats().setMoney(currentMoney - cell.getCost());

            // Notifica la grafica tramite Observer
            city.getCityState().notifyObservers();
            return BuildResult.SUCCESS;
        }

        return BuildResult.INVALID_POSITION;
    }

    //attiva una politica cittadina(Strategy pattern) modificando lo stato della città
    public void activatePolicy(CityPolicyStrategy policy){
        if(city != null && city.getCityState() != null ){
            city.getCityState().setPolicy(policy);
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

    //restituisce la strategia di politica cittadina attualmente attiva
    public CityPolicyStrategy getCurrentPolicy() {
        return city.getCityState().getCurrentPolicyStrategy();
    }

    //salva lo stato corrente della città su file tramite PersistenceManager
    public boolean saveGame(String filePath){
        if(city != null){
            return persistenceManager.saveCity(city, filePath);
        }
        return false;
    }

    //restituisce l'oggetto City(utile ad esempio per la view)
    public City getCity(){
        return city;
    }

    // Espone lo stato di bancarotta alla View
    public boolean isBankrupt() {
        return city != null && city.getCityState().isBankrupt();
    }

    // Espone il numero di case senza energia alla View
    public int getUnpoweredCount() {
        return city != null ? city.getCityState().getUnpoweredCount() : 0;
    }
}