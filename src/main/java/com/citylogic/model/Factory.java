package com.citylogic.model;

// Rappresenta un edificio industriale (fabbrica) all'interno della città
// Estende la classe astratta Building e definisce il proprio impatto sulle metriche
public class Factory extends Building {
    
    // Costruttore vuoto necessario a Jackson per la persistenza JSON
    public Factory(){
        super();
        this.setCost(1500); // Imposta il costo di costruzione della fabbrica
    }

     // Restituisce le variazioni delle metriche associate alla fabbrica
    // @return un oggetto Stats contenente i valori relativi all'edificio
    @Override
    public Stats returnStat(){
        // Parametri nell'ordine: inquinamento, denaro, felicità, popolazione, energia
        // Ad esempio: molta produzione economica, ma forte inquinamento e consumo di energia
        return new Stats(8,40,-8,0,-25);
    }
}
