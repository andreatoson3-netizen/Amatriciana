package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

//classe astratta che rappresenta un'infrastruttura generica all'interno della città
//estende Block e fa da base per PowerPlant,Park e Road

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,property="@class")

public abstract  class Infrastructure extends Cell {

    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Infrastructure() {
        super();
        this.free = false;//un'infrastruttura occupa un blocco, che quindi è occupato
    }


    //metodo astratta ereditato da Block che ciascuna infrastruttura
    // (PowerPlant,Road,Park) dovrà implementare per restituire le proprie metriche
    @Override
    public abstract Stats returnStat();
}


