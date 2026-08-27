package com.citylogic.model;

import com.citylogic.simulation.CityState;
//rappresenta la città all'interno della simulazione.
//funge da tramite(delegatore) tra il GameController e la classe CityState
// incapsulando la logica strutturale e temporale della partita
public class City {

    private CityState cityState;//gestisce lo stato interno,le statistiche e i tick della città

    //inizializza una nuova città creando una nuova istanza di CityState
    public void initCity() {

        this.cityState = new CityState();
    }
    //metodo che serve ad inizilizzare il budget
    public void initNewGameBudget(){
        if(this.cityState != null && this.cityState.getCityStats()!= null){
            this.cityState.getCityStats().setMoney(5000);

        }
    }


    //restituisce lo stato corrente della città(usato dal gamecontroller
    // per gestire la policy,i salvataggi e gli osservatori)
        public CityState getCityState() {
        return this.cityState;
    }

    //delega l'avanzamento del tempo(tramite tick) a llo stato della città
    public void processTick() {
        if (this.cityState != null) {
            this.cityState.processTick();
        }
    }
}