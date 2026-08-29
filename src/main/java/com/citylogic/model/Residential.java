package com.citylogic.model;


// Rappresenta un edificio residenziale all'interno della città.
// Estende la classe astratta Building e implementa il calcolo delle statistiche
public class Residential extends Building {
    
    // Costruttore vuoto necessario per Jackson durante la persistenza JSON
    public Residential(){
        super();
        this.setCost(500); // Costo di costruzione dell'edificio residenziale
    }

    // Restituisce le statistiche specifiche prodotte da un edificio residenziale.
    // Aumenta la popolazione e la felicità, ma richiede energia e genera inquinamento.
    // @return un oggetto Stats contenente le metriche dell'edificio
    @Override
    public Stats returnStat(){
        // Parametri: (inquinamento, denaro, felicità, popolazione, energia)
        // Un valore negativo in energia indica un consumo
        return new Stats(1,5,15,50,-10);
    }
}
