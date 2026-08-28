package com.citylogic.model;

//Rappresenta un edificio residenziale all'interno della città
//estende la classe astratta Building ed implementa il calcolo delle statistiche
public class Residential extends Building {
    // costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Residential(){
        super();
        this.setCost(500);//costo per costruire casa residenziale
    }

    //restituisce le statistiche specifiche prodotte da un edificio residenziale
    //ad esempio aumenta la popolazione e la felicità
    // ma richiede energia e genera inquinamento
    //@return un oggetto Stats con i valori propri dell'edificio
    @Override
    public Stats returnStat(){
        //parametri di esempio(inquinamento,denaro,felicità,popolazione,energia)
        // i valori negativi in energia indicano consumo
        return new Stats(1,15,10,50,-10);
    }
}
