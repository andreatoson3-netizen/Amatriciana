package com.citylogic.controller;

import com.citylogic.model.Cell;
import com.citylogic.model.City;
import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import com.citylogic.persistence.CityPersistenceManager;


//gestisce la logica di business e fa da tramite tra i comandi dell'utente e il mototre di gioco
//protegge lo stato interno della città evitando accessi non controllati

public class GameController {

    private City city;//lo stato attuale della città gestita dal controller
    private final CityPersistenceManager persistenceManager;//gestore per salvataggio e caricamento dei file


    public GameController() {
        this.persistenceManager = new CityPersistenceManager();
        //appena avviato il controller parte una nuova partita di default
        startNewGame();
    }

    //inizializza una nuova partita azzerando/creando una nuova città
    public void startNewGame(){
        this.city = new City();
        this.city.initCity();
        this.city.initNewGameBudget();//metodo della classe City che serve ad inizializzare il budget a 5000
    }

    //gestisce l'azione dell'utente di piazzare una cella/struttura sulla griglia
    //prende la cella,legge le sue coordinate(x,y) e la posiziona nella mappa della città
    public boolean setCell(Cell cell) {

        if (city == null ||
                cell == null ||
                city.getCityState() == null ||
                city.getCityState().getCityStats() == null) {

            return false;
        }

        // Recupera il denaro attuale
        int currentMoney =
                city.getCityState()
                        .getCityStats()
                        .getMoney();

        // Recupera il costo dell'edificio
        int buildingCost = cell.getCost();

        // Controllo del budget
        if (currentMoney < buildingCost) {
            return false;
        }

        // Prova a posizionare la cella
        boolean placed =
                city.getCityState()
                        .getGrid()
                        .setCell(cell, cell.getX(), cell.getY());

        // Se il posizionamento è riuscito
        if (placed) {

        // Scala immediatamente il costo di costruzione
        city.getCityState()
                .getCityStats()
                .setMoney(currentMoney - buildingCost);

        // Aggiorna la GUI
        city.getCityState().notifyObservers();

        return true;
    }

    return false;
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
    //@return l'oggetto Stats contenente i valori di denaro,inquinamento,felicità,...
    public Stats getCityStats() {
        return city.getCityState().getCityStats();
    }

    //restituisce la griglia del gioco attuale
    //@return l'oggetto grid che modella la mappa e la posizione delle strutture
    public Grid getGrid() {
        return city.getCityState().getGrid();
    }

    //restituisce il numero di tick corrente della simulazione
    //@return il valore intero del tick attuale
    public int getCurrentTick() {
        return city.getCityState().getCurrTick();
    }

    //restituisce  il budget disponibile
    //@return il valore intero del denaro attuale della città
    public int getMoney() {
        return city.getCityState().getCityStats().getMoney();
    }

    //restituisce la strategia di politica cittadina attualmente attiva
    //@return l'istanza di CityPolicyStrategy(EnviromentalTax oppure IndustrialExpansion)
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

    //restituisce l'oggetto City(utile ad esempio per la view
    public City getCity(){
        return city;
    }


}
