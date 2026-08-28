package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        // Arrange globale
        factory = new Factory();
    }

    @Test
    void testConstructor_InitializationAndCost() {
        // Assert
        // Verifica che il costo di costruzione della fabbrica sia 1500
        assertEquals(1500, factory.getCost(), "Il costo di costruzione di Factory deve essere 1500");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = factory.returnStat();

        // Assert: Validazione delle metriche industriali
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(8, stats.getPollution(), "Inquinamento errato per Factory (+8 atteso)");
        assertEquals(40, stats.getMoney(), "Rendimento economico errato per Factory (+40 atteso)");
        assertEquals(-8, stats.getHappiness(), "Impatto sulla felicità errato per Factory (-8 atteso)");
        assertEquals(0, stats.getPopulation(), "Factory non deve generare residenti diretti");
        assertEquals(-25, stats.getEnergy(), "Consumo energetico errato per Factory (-25 atteso)");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = factory.returnStat();
        Stats stats2 = factory.returnStat();

        // Assert: Verifica che vengano create istanze indipendenti
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni chiamata");
    }
}