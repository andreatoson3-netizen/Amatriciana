package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParkTest {

    private Park park;

    @BeforeEach
    void setUp() {
        // Arrange globale
        park = new Park();
    }

    @Test
    void testConstructor_InitializationAndCost() {
        // Assert
        // Verifica che il costo di costruzione del parco sia 200
        assertEquals(200, park.getCost(), "Il costo di costruzione di Park deve essere 200");
    }

    @Test
    void testReturnStat_MetricValues() {
        // Act
        Stats stats = park.returnStat();

        // Assert: Validazione delle metriche ecologiche e di bilancio
        assertNotNull(stats, "returnStat non deve restituire null");
        assertEquals(-10, stats.getPollution(), "Assorbimento inquinamento errato per Park (-10 atteso)");
        assertEquals(-10, stats.getMoney(), "Costo di manutenzione errato per Park (-10 atteso)");
        assertEquals(25, stats.getHappiness(), "Bonus felicità errato per Park (+25 atteso)");
        assertEquals(0, stats.getPopulation(), "Park non deve generare residenti");
        assertEquals(0, stats.getEnergy(), "Park non deve richiedere o produrre energia");
    }

    @Test
    void testReturnStat_ReturnsNewInstanceEachTime() {
        // Act
        Stats stats1 = park.returnStat();
        Stats stats2 = park.returnStat();

        // Assert: Verifica l'indipendenza delle istanze create
        assertNotSame(stats1, stats2, "returnStat deve restituire una nuova istanza ad ogni chiamata");
    }
}