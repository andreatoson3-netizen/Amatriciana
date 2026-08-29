package com.citylogic.strategy;

import com.citylogic.model.Stats;

// Implementa la politica di tassa ambientale.
// Riduce l'inquinamento e aumenta la felicità,
// introducendo però una leggera riduzione dei guadagni economici

public class EnvironmentalTax implements CityPolicyStrategy {

    @Override
    public Stats calculateStats(Stats rawStats) {
        if (rawStats == null) {
            return new Stats();
        }

        /*
         * Applica i fattori percentuali alle diverse statistiche:
         * - inquinamento ridotto del 20% (fattore 0.80)
         * - denaro ridotto del 5% (fattore 0.95)
         * - felicità aumentata del 10% (fattore 1.10)
         * - popolazione ed energia rimangono invariate.
         *
         * Le statistiche vengono calcolate separatamente perché
         * la policy ha un effetto diverso su ciascuna variabile.
         */
        return new Stats(
                (int) (rawStats.getPollution() * 0.80), // Inquinamento ridotto
                (int) (rawStats.getMoney() * 0.95),     // Denaro ridotto
                (int) (rawStats.getHappiness() * 1.10), // Felicità aumentata
                rawStats.getPopulation(),               // Popolazione invariata
                rawStats.getEnergy()                    // Energia invariata
        );

    }
}


