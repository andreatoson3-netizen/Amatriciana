package com.citylogic.strategy;

import com.citylogic.model.Stats;

//politica di tassa ambientale:riduce l'inquinamento ed aumenta la felicità
//riducendo però leggermente i guadagni economici

public class EnvironmentalTax implements CityPolicyStrategy {

    @Override
    public Stats calculateStats(Stats rawStats) {
        if (rawStats == null) {
            return new Stats();
        }

        //applichiamo i fattori percentuali usando il metodo multiply della classe Stats
        //riduce l'inquinamento del 20%(fattore 0.80)
        //riduce i soldi del 5%(fattore 0.95)
        //aumenta la felicità del 10%(fattore 1.10)

        //poichè le politiche possono avere impatti diversi per ogni variabile statistica
        //costruiamo il nuovo oggetto combinando i vari calcoli mirati
        return new Stats(
                (int) (rawStats.getPollution() * 0.80),//inquinamento ridotto
                (int) (rawStats.getMoney() * 0.95),     //soldi ridotti
                (int) (rawStats.getHappiness() * 1.10), //felicità aumentata
                rawStats.getPopulation(),            //popolazione invariata
                rawStats.getEnergy()                 //energia invariata
        );

    }
}


