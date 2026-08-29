package com.citylogic.model;

import com.citylogic.simulation.CityState;

// Rappresenta la città all'interno della simulazione.
// Funziona da tramite tra GameController e CityState,
// delegando a CityState la gestione dello stato della simulazione
public class City {
    
    // Gestisce lo stato interno, le statistiche e il tick corrente della città
    private CityState cityState; 

    // Inizializza una nuova città creando una nuova istanza di CityState
    public void initCity() {

        this.cityState = new CityState();
    }
    
    // Inizializza il budget di una nuova partita.
    public void initNewGameBudget(){
        if(this.cityState != null && this.cityState.getCityStats()!= null){
            this.cityState.getCityStats().setMoney(5000);

        }
    }

    // Restituisce lo stato corrente della città.
    // Viene utilizzato dal GameController per accedere allo stato della simulazione
        public CityState getCityState() {
        return this.cityState;
    }

    // Delega l'avanzamento del tempo, tramite un tick, a CityState.
    public void processTick() {
        if (this.cityState != null) {
            this.cityState.processTick();
        }
    }
}
