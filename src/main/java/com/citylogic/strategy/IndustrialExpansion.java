package com.citylogic.strategy;

import com.citylogic.model.Stats;

// Implementa la politica di espansione industriale.
// Aumenta i guadagni e l'energia, ma provoca un forte incremento
// dell'inquinamento e una leggera riduzione della felicità
public class IndustrialExpansion implements CityPolicyStrategy {

    @Override
    public Stats calculateStats(Stats rawStats) {
        if (rawStats == null) {
            return new Stats();
        }

        /*
         * Applica i seguenti effetti alle statistiche:
         * - aumenta l'inquinamento del 40% (fattore 1.40)
         * - aumenta i guadagni del 30% (fattore 1.30)
         * - riduce la felicità del 10% (fattore 0.90)
         * - lascia invariata la popolazione
         * - aggiunge un bonus fisso di 15 all'energia.
         */
        return new Stats(
                (int) (rawStats.getPollution() * 1.40),   // Inquinamento in forte aumento
                (int) (rawStats.getMoney() * 1.30),       // Guadagni aumentati
                (int) (rawStats.getHappiness() * 0.90),   // Felicità ridotta
                rawStats.getPopulation(),                 // Popolazione invariata
                rawStats.getEnergy() + 15                 // Bonus fisso di energia prodotta
        );
    }
}
