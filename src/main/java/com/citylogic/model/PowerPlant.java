package com.citylogic.model;

//rappresenta una centrale elettrica all'interno della città
//estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class PowerPlant extends Infrastructure {

    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public PowerPlant(){
        super();
        this.setCost(1000);//assegna il costo di costruzione della centrale
        this.setOperative(true);

    }

    //restituisce le statistiche specifiche prodotte da una centrale elettrica
    //produce molta energia, ma genera anche inquinamento,ed  ha costo di gestione
    // @return un oggetto Stats con i valori propri della Infrastruttura
    @Override
    public Stats returnStat(){
        //parametri di esempio:(inquinamento,denaro,felicità,popolazione,energia)
        //Ad esempio:inquinamento alto, costo economico negativo(spesa), energia molto positiva
        return new Stats(5,-60,-5,0,120);

    }
}
