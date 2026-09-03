package com.citylogic.simulation;

import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Gestisce lo stato corrente della città, inclusi il tick temporale,
// le statistiche aggregate, la griglia e la policy strategica attiva
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityState {
    private CityPolicyStrategy currentPolicy;
    private int currTick;//messo int per evitare di fare classe Tick
    private Stats cityStats;
    private Grid grid;


    // Lista degli Observer che devono essere notificati quando lo stato cambia.
    // Viene ignorata da Jackson perché gli Observer non fanno parte dello stato persistente della città
    @JsonIgnore
    private final List<CityObserver> observers;

    // Costruttore di default.
    // Inizializza il tick a zero, le statistiche a valori nulli, nessuna policy attiva e una nuova griglia
    public CityState(){
        this.currTick=0;
        this.cityStats= new Stats(0,0,0,0,0);
        this.currentPolicy=null; //Nessuna policy attiva all'avvio
        this.observers = new ArrayList<>();
        this.grid = new Grid();

    }

    // Aggiorna le statistiche della città a partire dai valori calcolati dalla griglia.
    // Se è presente una policy, questa viene applicata prima di aggiornare lo stato
    public void updateStats(Stats newStats) {
    if (newStats != null) {

        Stats statsToApply = newStats;

        // Applica la policy attualmente attiva, se presente
        if (this.currentPolicy != null) {
            statsToApply = this.currentPolicy.calculateStats(newStats);
        }

        // Recupera le statistiche precedenti necessarie per aggiornare i valori cumulativi
        int currentMoney = 0;
        int currentPollution = 0;

        if (this.cityStats != null) {
            currentMoney = this.cityStats.getMoney();
            currentPollution = this.cityStats.getPollution();
        }
        // Calcolo cumulativo dell'inquinamento con blocco al limite inferiore di 0
        int calculatedPollution = currentPollution + statsToApply.getPollution();
        int safePollution = Math.max(0, calculatedPollution);

        // Population e Happiness sono valori di stato:
        // vengono sostituiti con quelli calcolati per il tick corrente.
        //
        // Money e Pollution sono invece valori dinamici:
        // la variazione prodotta dagli edifici viene aggiunta
        // al valore accumulato nei tick precedenti.
        //
        // Energy viene ricalcolata per il tick corrente.

        Stats updatedStats = new Stats(
                safePollution,
                currentMoney + statsToApply.getMoney(),
                statsToApply.getHappiness(),
                statsToApply.getPopulation(),
                statsToApply.getEnergy()
        );

        this.cityStats = updatedStats;

        // Notifica gli Observer del nuovo stato della città
        notifyObservers();
        }
    }


    // Avanza di un tick la simulazione e aggiorna le statistiche della città
    public void processTick(){
        this.currTick++;
        
        //1)ottiene le statistiche grezze interrogando la griglia(se la griglia è inizializzata)
        if( this.grid != null){

            // 1) AZIONE: Distribuisce l'energia disponibile e determina quali strutture
            // possono rimanere operative
            this.grid.distributeEnergy();
            
            // Calcola le statistiche complessive delle strutture operative
            Stats rawStats=this.grid.calculateRawStats();
            
            //2) Aggiorna le statistiche applicando anche l'eventuale policy e notifica gli Observer
            updateStats(rawStats);
        }
        else{
        // Se la griglia non è disponibile, notifica comunque gli Observer dell'avanzamento del tick
        notifyObservers();
        }
    }

    // Registra un nuovo Observer, evitando duplicati
    public void addObserver(CityObserver observer){
        if(observer !=null && !this.observers.contains(observer)){
            this.observers.add(observer);
        }
    }

    // Rimuove un Observer dalla lista degli elementi da notificare
    public void removeObserver(CityObserver observer){
        if(observer != null){
            this.observers.remove(observer);
        }
    }
    
    // Notifica tutti gli Observer inviando loro le statistiche aggiornate
    public void notifyObservers(){
        for (CityObserver observer : this.observers) {
            observer.update(this.cityStats);
        }
    }


    // Getter e setter utilizzati anche da Jackson per la serializzazione JSON
    public Stats getCityStats(){
        return this.cityStats;
    }
    
    public void setCityStats(Stats cityStats){
        this.cityStats=cityStats;
        notifyObservers();
    }

    public Grid getGrid(){
        return this.grid;
    }
    
    public void setGrid(Grid grid){
        this.grid=grid;
    }

    public int getCurrTick() {
        return currTick;
    }
    
    public void setCurrTick(int currTick){
        this.currTick=currTick;
    }

    public CityPolicyStrategy getCurrentPolicyStrategy(){
        return currentPolicy;
    }
    
    // Imposta la policy attiva e notifica gli Observer del cambiamento
    public void setPolicy(CityPolicyStrategy p){
        this.currentPolicy=p;
        notifyObservers();
    }

    // Verifica se la città è in bancarotta a causa di un bilancio negativo.
    // @return true se il denaro disponibile è minore di zero
    public boolean isBankrupt() {
        return this.cityStats != null && this.cityStats.getMoney() < 0;
    }

    // Conta le abitazioni che non hanno una fonte di energia nelle vicinanze.
    // @return numero di abitazioni non alimentate
    public int getUnpoweredCount() {
        if (this.grid != null) {
            return this.grid.countUnpoweredResidential();
        }
        return 0;
    }
    
    // Restituisce il numero di strutture attualmente in blackout
    public int getBlackoutCount() {
        if (this.grid != null) {
            return this.grid.getBlackoutCount();
        }
        return 0;
    }
}
