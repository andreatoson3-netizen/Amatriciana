package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

//annotazioni di Jackson per gestire correttamente la serializzazione JSON delle sottoclassi
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS,include=JsonTypeInfo.As.PROPERTY,property="@class")

public abstract class Block {
    boolean free;//indica se la cella della griglia è libera oppure occupata da una struttura
    int x; //prima coordinata del blocco all'interno della griglia della città
    int y; //seconda coordinata del blocco all'interno della griglia della città

    //restituisce lo stato del blocco
    //@return true se il blocco è vuoto/disponibile, false se è occupato
    public boolean isFree(){
       return this.free;
    }

    //metodo astratto che restituisce l'impatto avuto dal blocco.
    // Viene implementato dalle classi figlie Building o Infrastructure
    //@return un oggetto Stats contenente le metriche(denaro,inquinamento,felicità,ecc..)
    public abstract Stats returnStats();


}
