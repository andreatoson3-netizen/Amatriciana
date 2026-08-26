package com.citylogic.model;

//rappresenta un edificio industriale(fabbrica) all'interno della città
//estende la classe astratta Building e implementa il calcolo delle statistiche
public class Factory extends Building {
    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Factory(){
        super();
    }

    //restituisce le statistiche specifiche prodotte da una fabbrica
    //genera molto denaro e operatività ma alza inquinamento e abbassa felicità
    //@return un oggetto Stats con i valori propri dell'edificio
    @Override
    public Stats returnStat(){
        //parametri di esempio(inquinamento,denaro,felicità,popolazione,energia)
        //ad esempio: molta produzione economica ma forte inquinamento e consumo di energia
        return new Stats(40,100,-10,0,-20);
    }
}
