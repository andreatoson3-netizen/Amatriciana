package com.citylogic.model;

// Rappresenta una strada all'interno della città.
// Estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class Road  extends Infrastructure{

    // Costruttore vuoto necessario per Jackson durante la persistenza JSON
    public Road(){
        super();
        this.setCost(50); // Costo di costruzione della strada
    }

    // Restituisce le statistiche specifiche prodotte da una strada.
    // Contribuisce all'operatività e alla connessione della città,
    // generando un piccolo costo di manutenzione e un leggero inquinamento.
    // @return un oggetto Stats contenente le metriche della strada
    @Override
    public Stats returnStat(){
        // Parametri: (inquinamento, denaro, felicità, popolazione, energia)
        return new Stats(1,-5,2,0,0);

    }
}
