package com.citylogic.simulation;

import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;

//Gestisce lo stato corrente della città,inclusi i tick temporali
//le statistiche aggregate e la policy strategica attiva
public class CityState {
    private CityPolicyStrategy currentPolicy;
    private int currTick;//definito come int e non Tick per evitare di costruire una classe Tick
    private Stats cityStats;

    //costruttore di default. Inizializza i tick a zero e crea oggetto Stats vuoto
    public CityState(){
        this.currTick=0;
        this.cityStats= new Stats(0,0,0,0,0,0);
        this.currentPolicy=null;//Nessuna policy attiva all'avvio
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
        }
    }

    //restiruisce le statistiche correnti della città
    //@return un oggetto Stats con i valori attuali
    public Stats getCityStats(){
        return this.cityStats;
    }

    //imposta una nuova politica strategica per la città
    //@param p è la strategia da impostare
    public void setPolicy(CityPolicyStrategy p){
        this.currentPolicy=p;
    }

    //avanza il contatore dei tick temporali della simulazione
    // e processa gli eventi periodici
    public void processTick(){
        this.currTick++;

    }


    //Getter e Setter per Jackson
    public int getCurrTick() {
        return currTick;
    }

    public void setCurrTick(int currTick){
        this.currTick=currTick;
    }

    public CityPolicyStrategy getCurrentPolicyStrategy(){
        return currentPolicy;
    }

}