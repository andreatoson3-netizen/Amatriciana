package com.citylogic.model;

//rappresenta una centrale elettrica all'interno della città
//estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class PowerPlant extends Infrastructure {

    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public PowerPlant(){
        super();
    }

    //restituisce le statistiche specifiche prodotte da una centrale elettrica
    //produce molta energia, ma genera anche inquinamento,ed  ha costo di gestione
    // @return un oggetto Stats con i valori propri della Infrastruttura
    @Override
    public Stats returnStats(){
        //parametri di esempio:(inquinamento,denaro,felicità,popolazione,energia,operatività)
        //Ad esempio:inquinamento alto, costo economico negativo(spesa), emergia molto positiva
        return new Stats(30,-40,-5,0,100,5);

    }
}
