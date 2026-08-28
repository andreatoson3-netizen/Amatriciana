package com.citylogic.simulation;

import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Gestisce lo stato corrente della città,inclusi i tick temporali
//le statistiche aggregate e la policy strategica attiva
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityState {
    private CityPolicyStrategy currentPolicy;
    private int currTick;//messo int per evitare di fare classe Tick
    private Stats cityStats;
    private Grid grid;


    //pattern observer(ignorato da Jackson durante il salvataggio JSON per evitare conflitti)
    @JsonIgnore
    private final List<CityObserver> observers;

    //costruttore di default. Inizializza i tick a zero e crea oggetto Stats vuoto
    public CityState(){
        this.currTick=0;
        this.cityStats= new Stats(0,0,0,0,0);
        this.currentPolicy=null; //Nessuna policy attiva all'avvio
        this.observers = new ArrayList<>();
        this.grid = new Grid();

    }

    //aggiorna le statistiche della città.Se è applicata una policy(Strategy pattern),
    //la applica prima di memorizzare o sommare i valori
    //@param newStats le statistiche grezze calcolate dalla griglia
    /*public void updateStats(Stats newStats){
        if (newStats != null){
            Stats statsToApply = newStats;
            //Se c'è una politica attiva,applica la strategia di calcolo
            if(this.currentPolicy != null){
                statsToApply=this.currentPolicy.calculateStats(newStats);
            }
            //1)salviamo denaro attuale prima di aggiornare le metriche
            int currentMoney=0;
            if(this.cityStats!=null){
                currentMoney=this.cityStats.getMoney();
            }
            //2)Sostituiamo l'oggetto con le nuove statistiche calcolate dalla griglia
            //questo aggiorna correttamente popolazione,felicità,inquinamento ed energia
            this.cityStats=statsToApply;

            //3)Ripristiniamo il denaro sommandolo al flusso di cassa di questo turno
            this.cityStats.setMoney(currentMoney+statsToApply.getMoney());

            //4)notifica agli observer della GUI per aggiornare dashboard
            notifyObservers();
        }
    }*/

    public void updateStats(Stats newStats) {
    if (newStats != null) {

        Stats statsToApply = newStats;

        // Se c'è una policy attiva, applica la strategia
        if (this.currentPolicy != null) {
            statsToApply = this.currentPolicy.calculateStats(newStats);
        }

        // Recuperiamo le statistiche precedenti
        int currentMoney = 0;
        int currentPollution = 0;

        if (this.cityStats != null) {
            currentMoney = this.cityStats.getMoney();
            currentPollution = this.cityStats.getPollution();
        }

        // Population e Happiness sono valori di stato:
        // vengono sostituiti con quelli calcolati per il tick corrente.
        //
        // Money e Pollution sono invece valori dinamici:
        // la variazione prodotta dagli edifici viene aggiunta
        // al valore accumulato nei tick precedenti.
        //
        // Energy viene ricalcolata per il tick corrente.

        Stats updatedStats = new Stats(
                currentPollution + statsToApply.getPollution(),
                currentMoney + statsToApply.getMoney(),
                statsToApply.getHappiness(),
                statsToApply.getPopulation(),
                statsToApply.getEnergy()
        );

        this.cityStats = updatedStats;

        // Notifica gli observer, quindi la Dashboard aggiorna
        // automaticamente i valori visualizzati.
        notifyObservers();
        }
    }


    //avanza il contatore dei tick temporali della simulazione e processa gli eventi periodici
    public void processTick(){
        this.currTick++;
        //1)ottiene le statistiche grezze interrogando la griglia(se la griglia è inizializzata)
        if( this.grid != null){

            // 1) AZIONE: Distribuisce le risorse e iberna chi resta senza energia
            this.grid.distributeEnergy();

            Stats rawStats=this.grid.calculateRawStats();
        //2)aggiorna le statistiche applicando la policy e notificando gli observer(fatto dal metodo upDate)
            updateStats(rawStats);
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

    //rimuove osservatore dalla lista degli elementi in ascolto,
    // in modo che smetta di ricevere notifiche quando lo stato della città cambia(tramite notifyObserver)
    public void removeObserver(CityObserver observer){
        if(observer != null){
            this.observers.remove(observer);
        }
    }

    public void notifyObservers(){
        for (CityObserver observer : this.observers) {
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


    public int getCurrTick() {
        return currTick;
    }
    public void setCurrTick(int currTick){
        this.currTick=currTick;
    }

    public CityPolicyStrategy getCurrentPolicyStrategy(){
        return currentPolicy;
    }

    public void setPolicy(CityPolicyStrategy p){
        this.currentPolicy=p;
        notifyObservers();
    }

    //verifica se la città è andata in bancarotta a causa di un bilancio negativo
    //@return true se i fondi attuali sono minori di zero, false altrimenti
    // Verifica se la città è andata in bancarotta (Regola di Business)
    public boolean isBankrupt() {
        return this.cityStats != null && this.cityStats.getMoney() < 0;
    }

   // Interroga la griglia per contare i blackout (Regola di Business)
    public int getUnpoweredCount() {
        if (this.grid != null) {
            return this.grid.countUnpoweredResidential();
        }
        return 0;
    }

    public int getBlackoutCount() {
        if (this.grid != null) {
            return this.grid.getBlackoutCount();
        }
        return 0;
    }
}
