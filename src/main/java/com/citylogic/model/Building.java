package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
//classe astratta che rappresenta un edificio generico all'interno della città
//estende Cell ed è specializzata in edifici residenziali, industriali e commerciali

//scritture per Jackson
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY,property="@class")


public abstract class Building extends Cell {
    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Building(){
        super();
        this.free=false;//edificio occupa il blocco, quindi non è piu' libero
    }

    //metodo astratto ereditato da Block che ciascun edificio concreto
    // (Residential,Factory,Commercial) dovrà implementare per restiruire le proprie metriche
    @Override
    public abstract Stats returnStat();

}

