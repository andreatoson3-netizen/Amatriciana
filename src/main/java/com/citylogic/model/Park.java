package com.citylogic.model;

//Rappresenta un parco pubblico all'interno della città.
//Estende la classe astratta Infrastructure e implementa il calcolo delle statistiche

public class Park extends Infrastructure {

    // Costruttore vuoto necessario per Jackson durante la persistenza JSON
     public Park() {

         super();
         this.setCost(200); // Costo di costruzione del parco
    }

     //Restituisce le statistiche specifiche prodotte da un parco.
     //Riduce l'inquinamento, aumenta la felicità e comporta un costo di manutenzione.
     //@return un oggetto Stats contenente le metriche del parco
     @Override
     public Stats returnStat() {
         // Parametri: (inquinamento, denaro, felicità, popolazione, energia)
         return new Stats(-3, -10, 25, 0, 0);
    }
}
