package com.citylogic.simulation;

import com.citylogic.model.Stats;
//interfaccia per il pattern Observer
//permette agli elementi in ascolto di 
//ricevere aggiornamenti automatici sullo stato delle città ogni volta che si verificano cambiamenti
public interface CityObserver {
    //metodo usato per notificare i cambiamenti di stato
    void update(Stats currentStats);
}
