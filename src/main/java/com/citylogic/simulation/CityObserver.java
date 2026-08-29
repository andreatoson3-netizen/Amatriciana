package com.citylogic.simulation;

import com.citylogic.model.Stats;

// Interfaccia utilizzata per il pattern Observer.
// Permette agli oggetti che osservano la città di ricevere
// aggiornamenti quando cambiano le statistiche della simulazione
public interface CityObserver {
    
    // Notifica l'Observer fornendo le statistiche aggiornate della città
    void update(Stats currentStats);
}
