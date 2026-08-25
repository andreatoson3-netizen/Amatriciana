package com.citylogic.model;

//Rappresenta un parco pubblico all'interno della città.
//Estende la classe astratta Infrastructure e implementa il calcolo delle statistiche.

public class Park extends Infrastructure {

    //Costruttore vuoto di default necessario per Jackson (persistenza JSON).
     public Park() {
        super();
    }

     //Restituisce le statistiche specifiche prodotte da un parco.
     //Riduce l'inquinamento (valore negativo), aumenta notevolmente la felicità e richiede un piccolo costo di manutenzione.
     //@return un oggetto Stats con i valori propri di questa infrastruttura
     @Override
     public Stats returnStats() {
          // Parametri di esempio: (inquinamento, denaro, felicità, popolazione, energia, operatività)
         // Ad esempio: assorbe inquinamento (-10), costo di manutenzione (-10), grande bonus di felicità (+25).
         return new Stats(-10, -10, 25, 0, 0, 2);
    }
}