package com.citylogic.model;

// Rappresenta un edificio commerciale all'interno della città.
// Estende la classe astratta Building e definisce il proprio impatto sulle metriche
public class Commercial extends Building {
    
    // Costruttore vuoto necessario a Jackson per la persistenza JSON
    public Commercial() {
        super();
        this.setCost(800); // Imposta il costo di costruzione dell'edificio commerciale
    }

    // Restituisce le variazioni delle metriche associate all'edificio commerciale
    // @return un oggetto Stats contenente i valori relativi all'edificio
    @Override
    public Stats returnStat(){
        // Parametri nell'ordine: inquinamento, denaro, felicità, popolazione, energia
        //Ad esempio: buon guadagno economico e felicità, consumo energetico moderato
        return new Stats(1,45,10,0,-15);

    }
}
