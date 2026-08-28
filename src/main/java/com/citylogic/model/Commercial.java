package com.citylogic.model;

//rappresenta un edificio commerciale all'interno della città
//estende la classe astratta Building ed implementa il calcolo delle statistiche
public class Commercial extends Building {
    //costruttore vuoto di default necessario per Jackson(persistenza JSON)
    public Commercial() {
        super();
        this.setCost(800);//costo di costruzione Commercial
    }

    //restituisce le statistiche specifiche prodotte da un edificio commerciale
    //genera una buona quantità di denaro e felicità con impatto su inquinamento e consumo energetico
    //@return un oggetto Stats con i valori propri dell'edificio
    @Override
    public Stats returnStat(){
        //parametri di esempio:(inquinamento,denaro,felicità,popolazione,energia)
        //Ad esempio:buon guadagno economico e felicità, consumo energetico moderato
        return new Stats(1,60,10,0,-15);

    }
}
