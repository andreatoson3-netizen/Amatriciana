package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Classe astratta che rappresenta un'infrastruttura generica all'interno della città.
// Estende Cell e costituisce la classe base per PowerPlant, Park e Road

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,property="@class")

public abstract  class Infrastructure extends Cell {

    // Costruttore vuoto necessario per Jackson durante la persistenza JSON
    public Infrastructure() {
        super();
        this.free = false; // L'infrastruttura occupa la cella della griglia
    }


    // Metodo astratto che ogni infrastruttura concreta
    // (PowerPlant, Road, Park) deve implementare
    // per restituire le proprie statistiche.
    @Override
    public abstract Stats returnStat();
}


