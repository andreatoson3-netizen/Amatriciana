package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommercialTest {

    private Commercial commercial;

    @BeforeEach
    void setUp() {
        // Arrange globale
        commercial = new Commercial();
    }

    @Test
    void testConstructor_InitializationAndCost() {
        // Assert
        // Verifica che il costo di costruzione ereditato sia inizializzato a 800
        assertEquals(800, commercial.getCost(), "Il costo di costruzione del Commercial deve essere 800");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = commercial.returnStat();

        // Assert: Validazione delle metriche specifiche del blocco commerciale
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(10, stats.getPollution(), "Inquinamento errato per Commercial");
        assertEquals(60, stats.getMoney(), "Rendimento economico errato per Commercial");
        assertEquals(10, stats.getHappiness(), "Rendimento felicità errato per Commercial");
        assertEquals(0, stats.getPopulation(), "La zona commerciale non deve generare popolazione diretta");
        assertEquals(-15, stats.getEnergy(), "Il consumo energetico deve essere -15");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = commercial.returnStat();
        Stats stats2 = commercial.returnStat();

        // Assert: Verifica che non vengano condivisi puntatori in memoria
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni invocazione");
    }
}