package com.citylogic.controller;

import com.citylogic.model.Cell;
import com.citylogic.model.City;
import com.citylogic.strategy.CityPolicyStrategy;
import com.citylogic.persistence.CityPersistenceManager;


//gestisce la logica di business e fa da tramite tra i comandi dell'utente e il mototre di gioco
//protegge lo stato interno della città evitando accessi non controllati

public class GameController {

    private City city;//lo stato attuale della città gestita dal controller
    private CityPersistenceManager persistenceManager;//gestore per salvataggio e caricamento dei file


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

        if (city == null || cell == null || city.getCityState() == null || city.getCityState().getCityStats() == null) {
            return false;
        }
        //1)recupera denaro attuale dalle statistiche ed il costo della cella
        int currentMoney = city.getCityState().getCityStats().getMoney();
        int buildingCost = cell.getCost();

        //2)controllo del budget:se non ci sono abbastanza soldi, azione bloccata
        if (currentMoney < buildingCost) {
            return false;
        }

        //3)delega l'inserimento sicuro alla Griglia usando le coordinate già presenti nella cella(o passate)
        boolean placed = city.getCityState().getGrid().setCell(cell, cell.getX(), cell.getY());

        //4)se la griglia ha accettato il posizionamento, scala il budget
        if (placed) {
            city.getCityState().getCityStats().setMoney(currentMoney - buildingCost);
            return true;
        }

        return false;//fallito perchè coordinate fuori dai confini o cella occupata
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
    public void loadGame(String filePath){
        City loadedCity=persistenceManager.loadCity(filePath);
        if(loadedCity != null){
            this.city =loadedCity;
        }
    }


    //salva lo stato corrente della città su file tramite PersistenceManager
    public void saveGame(String filePath){
        if(city!= null){
            persistenceManager.saveCity(city,filePath);
        }
    }

    //restituisce l'oggetto City(utile ad esempio per la view
    public City getCity(){
        return city;
    }


}

