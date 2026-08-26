package com.citylogic.simulation;

import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;

//Gestisce lo stato corrente della città,inclusi i tick temporali
//le statistiche aggregate e la policy strategica attiva
public class CityState {
    private CityPolicyStrategy currentPolicy;
    private int currTick;//messo int per evitare di fare classe Tick
    private Stats cityStats;
    private Grid grid;

    //pattern observer(ignorato da Jackson durante il salvataggio JSON per evitare conflitti)
    @JsonIgnore
    private List<CityObserver> observers;

    //costruttore di default. Inizializza i tick a zero e crea oggetto Stats vuoto
    public CityState(){
        this.currTick=0;
        this.cityStats= new Stats(0,0,0,0,0);
        this.currentPolicy=null; //Nessuna policy attiva all'avvio
        this.observers = new ArrayList<>();

    }

    //aggiorna le statistiche della città.Se è applicata una policy(Strategy pattern),
    //la applica prima di memorizzare o sommare i valori
    //@param newStats le statistiche grezze calcolate dalla griglia
    public void updateStats(Stats newStats){
        if (newStats != null){
            Stats statsToApply = newStats;
            //Se c'è una politica attiva,applica la strategia di calcolo
            if(this.currentPolicy != null){
                statsToApply=this.currentPolicy.calculateStats(newStats);
            }
            //aggiorna le statistiche correnti della città
            this.cityStats=statsToApply;
            notifyObservers();//notifica gli osservatori che le statistiche sono cambiate
        }
    }


    //avanza il contatore dei tick temporali della simulazione
    // e processa gli eventi periodici
    public void processTick(){
        this.currTick++;
        //1)ottiene le statistiche grezze interrogando la griglia(se la griglia è inizializzata)
        if( this.grid != null){
            Stats rawStats=this.gridcalculateRawStats();
        //2)aggiorna le statistiche applicando la policy e notificando gli observer(fatto dal metodo upDate)
            uptadeStats(rawStats);
        }
        else{
        //se la griglia non è presente allora notifica gli osservatori del tick
        notifyObservers();
        }
    }

    public void addObserver(CityObserver observer){
        if(observer !=null && !this.observers.contains(observer)){
            this.observers.add(observer);
        }
    }

    public void notifyObservers(){
        for(CityObserver observer: this.observers){
            observer.update(this.cityStats);
        }
    }


    //Getter e Setter per Jackson
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


    public void setPolicy(CityPolicyStrategy p){
        this.currentPolicy=p;
        notifyObservers();
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
    public void setCurrentPolicyStrategy(CityPolicyStrategy currentPolicy ){
        this.currentPolicy=currentPolicy;
    }

}
