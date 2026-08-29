package com.citylogic.model;

// Rappresenta una centrale elettrica all'interno della città.
// Estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class PowerPlant extends Infrastructure {

    // Costruttore vuoto necessario per Jackson durante la persistenza JSON
    public PowerPlant(){
        super();
        this.setCost(1000); // Costo di costruzione della centrale
        this.setOperative(true); // La centrale è operativa fin dalla costruzione

    }

    // Restituisce le statistiche specifiche prodotte da una centrale elettrica.
    // Produce energia, ma genera anche inquinamento e comporta un costo di gestione.
    // @return un oggetto Stats contenente le metriche della centrale
    @Override
    public Stats returnStat(){
        // Parametri: (inquinamento, denaro, felicità, popolazione, energia)
        return new Stats(5,-100,-5,0,100);

    }
}
