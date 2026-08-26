package com.citylogic.model;

//rappresenta una strada all'interno della città
//estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class Road  extends Infrastructure{

    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Road(){
        super();
    }

    //restituisce le statistiche specifiche prodotte da una strada
    //aiuta operatività e connessione,generando costo di manutenzione e leggero inquinamento
    //@return un oggetto Stats con i valori propri dell'infrastruttura
    @Override
    public Stats returnStat(){
        //parametri di esempio(inquinamento,denaro,felicità,popolazione,energia)
        //ad esempio: leggero inquinamento,piccolo costo manutenzione, bonus all'operatività
        return new Stats(2,-5,2,0,0);

    }
}
