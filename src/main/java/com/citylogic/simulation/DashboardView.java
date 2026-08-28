package com.citylogic.simulation;

import com.citylogic.model.Stats;


public class DashboardView implements CityObserver {
    @Override
    public void update(Stats currentStats){
        System.out.println("\n========================================");
        System.out.println("          📊 CITYLOGIC DASHBOARD          ");
        System.out.println("========================================");

        if (currentStats != null) {
            System.out.println("  💰 Denaro:       " + currentStats.getMoney());
            System.out.println("  🏭 Inquinamento: " + currentStats.getPollution());
            System.out.println("  😊 Felicità:     " + currentStats.getHappiness());
            System.out.println("  👥 Popolazione:  " + currentStats.getPopulation());
            System.out.println("  ⚡ Energia:      " + currentStats.getEnergy());
        } else {
            System.out.println("  [Nessuna statistica disponibile]");
        }

        System.out.println("========================================\n");


    }

}
