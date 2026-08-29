package com.citylogic.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Classe astratta che rappresenta una cella della griglia.
// Viene estesa da Building e Infrastructure

// Annotazione di Jackson necessaria per mantenere il tipo concreto
// delle sottoclassi durante la serializzazione e deserializzazione JSON
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS,include=JsonTypeInfo.As.PROPERTY,property="@class")

public abstract class Cell {
    boolean free; //Indica se la cella della griglia è libera oppure occupata da una struttura
    int x; //Coordinata x della cella nella griglia
    int y; //Coordinata y della cella nella griglia
    private boolean isOperative = true; //Indica se la struttura presente nella cella è attiva e funzionante(true)
                                        // o spenta/guasta(false)
    private int cost;          // Costo di costruzione della struttura

    // Restituisce lo stato della cella
    // @return true se la cella è libera, false se è occupata
    public boolean isFree(){
       return this.free;
    }

    // Restituisce l'impatto della struttura sulle metriche della città.
    // Viene implementato dalle classi concrete Building e Infrastructure.
    // @return un oggetto Stats contenente le variazioni delle metriche (denaro,inquinamento,felicità,ecc..)
    public abstract Stats returnStat();

    // Costruttore vuoto necessario a Jackson per la deserializzazione JSON.
    public Cell(){
        this.cost=0;
    }

    // Getter e setter utilizzati anche da Jackson per accedere agli attributi.
    public boolean getFree(){
        return free;
    }

    public void setFree(boolean free){
        this.free=free;
    }

    public boolean isOperative(){
        return isOperative;
    }

    public void setOperative(boolean operative){
           isOperative=operative;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x=x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y=y;
    }

    public int getCost(){
        return cost;
    }
    public void setCost(int cost){
        this.cost=cost;
    }

}
