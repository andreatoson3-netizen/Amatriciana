package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResidentialTest {

    private Residential residential;

    @BeforeEach
    void setUp() {
        // Arrange globale
        residential = new Residential();
    }

    @Test
    void testConstructor_InitializationAndCost() {
        // Assert
        // Verifica che il costo di costruzione residenziale sia 500
        assertEquals(500, residential.getCost(), "Il costo di costruzione di Residential deve essere 500");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = residential.returnStat();

        // Assert: Validazione delle metriche specifiche del quartiere residenziale
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(5, stats.getPollution(), "Inquinamento errato per Residential (+5 atteso)");
        assertEquals(10, stats.getMoney(), "Rendimento economico errato per Residential (+10 atteso)");
        assertEquals(15, stats.getHappiness(), "Rendimento felicità errato per Residential (+15 atteso)");
        assertEquals(50, stats.getPopulation(), "Crescita demografica errata per Residential (+50 abitanti attesi)");
        assertEquals(-10, stats.getEnergy(), "Consumo energetico errato per Residential (-10 atteso)");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = residential.returnStat();
        Stats stats2 = residential.returnStat();

        // Assert: Verifica l'indipendenza delle istanze in memoria
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni invocazione");
    }
}