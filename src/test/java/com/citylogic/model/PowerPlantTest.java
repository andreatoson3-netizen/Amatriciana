package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowerPlantTest {

    private PowerPlant powerPlant;

    @BeforeEach
    void setUp() {
        // Arrange globale
        powerPlant = new PowerPlant();
    }

    @Test
    void testConstructor_InitializationCostAndOperativeState() {
        // Assert
        // Verifica il costo di costruzione di 1000
        assertEquals(1000, powerPlant.getCost(), "Il costo di costruzione di PowerPlant deve essere 1000");

        // Verifica che la centrale nasca subito attiva/funzionante di default
        assertTrue(powerPlant.isOperative(), "PowerPlant deve nascere con isOperative = true");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = powerPlant.returnStat();

        // Assert: Validazione della produzione energetica e dei costi di gestione
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(30, stats.getPollution(), "Inquinamento errato per PowerPlant (+30 atteso)");
        assertEquals(-100, stats.getMoney(), "Costo di mantenimento errato per PowerPlant (-100 atteso)");
        assertEquals(-5, stats.getHappiness(), "Impatto felicità errato per PowerPlant (-5 atteso)");
        assertEquals(0, stats.getPopulation(), "PowerPlant non deve generare residenti");
        assertEquals(100, stats.getEnergy(), "Produzione energetica errata per PowerPlant (+100 atteso)");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = powerPlant.returnStat();
        Stats stats2 = powerPlant.returnStat();

        // Assert: Verifica che non vengano condivisi puntatori in memoria
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni invocazione");
    }
}