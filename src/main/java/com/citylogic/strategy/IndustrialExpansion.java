package com.citylogic.strategy;

import com.citylogic.model.Stats;

//politica di espansione industriale: aumenta notevolmente i guadagni
//e l'energia, ma a prezzo di un forte incremento dell'inquinamento.
public class IndustrialExpansion implements CityPolicyStrategy {

    @Override
    public Stats calculateStats(Stats rawStats) {
        if (rawStats == null) {
            return new Stats();
        }

        // - Aumenta i guadagni del 30% (fattore 1.30)
        // - Aumenta l'inquinamento del 40% (fattore 1.40)
        // - Riduce leggermente la felicità del 10% (fattore 0.90)
        // - Aumenta la produzione energetica di una quota o fattore
        return new Stats(
                (int) (rawStats.getPollution() * 1.40),   // Inquinamento in forte aumento
                (int) (rawStats.getMoney() * 1.30),       // Guadagni industriali alti
                (int) (rawStats.getHappiness() * 0.90),   // Felicità in calo
                rawStats.getPopulation(),                 // Popolazione invariata
                rawStats.getEnergy() + 15                 // Bonus fisso di energia prodotta
        );
    }
}
