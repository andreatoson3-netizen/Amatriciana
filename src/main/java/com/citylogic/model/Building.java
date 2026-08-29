package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
// Classe astratta che rappresenta un edificio generico all'interno della città.
// È specializzata nelle tipologie Residential, Factory e Commercial.

// Configurazione necessaria a Jackson per riconoscere il tipo concreto
// dell'edificio durante la persistenza JSON.
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY,property="@class")


public abstract class Building extends Cell {
    //Costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Building(){
        super();
        this.free=false; // Un edificio occupa la cella, quindi non è libera.
    }

    // Ogni edificio concreto implementa questo metodo per restituire
    // gli effetti sulle metriche della città.
    @Override
    public abstract Stats returnStat();

}

