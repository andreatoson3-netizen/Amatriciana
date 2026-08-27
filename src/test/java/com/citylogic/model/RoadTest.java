package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoadTest {

    private Road road;

    @BeforeEach
    void setUp() {
        // Arrange globale
        road = new Road();
    }

    @Test
    void testConstructor_InitializationAndCost() {
        // Assert
        // Verifica il costo di costruzione della strada pari a 50
        assertEquals(50, road.getCost(), "Il costo di costruzione di Road deve essere 50");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = road.returnStat();

        // Assert: Validazione delle metriche del segmento stradale
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(2, stats.getPollution(), "Inquinamento errato per Road (+2 atteso)");
        assertEquals(-5, stats.getMoney(), "Costo di manutenzione errato per Road (-5 atteso)");
        assertEquals(2, stats.getHappiness(), "Bonus felicità errato per Road (+2 atteso)");
        assertEquals(0, stats.getPopulation(), "Road non deve generare residenti");
        assertEquals(0, stats.getEnergy(), "Road non deve consumare o produrre energia");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = road.returnStat();
        Stats stats2 = road.returnStat();

        // Assert: Verifica che non vengano riutilizzate le stesse istanze in memoria
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni chiamata");
    }
}