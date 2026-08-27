package com.citylogic.simulation;

import com.citylogic.model.Stats;
import com.citylogic.strategy.CityPolicyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityStateTest {

    private CityState cityState;

    @BeforeEach
    void setUp() {
        // Arrange globale
        cityState = new CityState();
    }

    @Test
    void testConstructor_InitialState() {
        // Assert: verifica i valori di default
        assertEquals(0, cityState.getCurrTick(), "Il tick iniziale deve essere 0");
        assertNotNull(cityState.getCityStats(), "Le statistiche non devono essere null all'avvio");
        assertNull(cityState.getCurrentPolicyStrategy(), "Non ci deve essere nessuna policy attiva all'avvio");
    }

    @Test
    void testProcessTick_IncrementsTickCounter() {
        // Act
        cityState.processTick();

        // Assert
        assertEquals(1, cityState.getCurrTick(), "Il primo tick deve portare il contatore a 1");
    }

    @Test
    void testObserverNotification_OnProcessTick() {
        // Arrange: Creiamo e registriamo la spia
        TestSpyObserver spyObserver = new TestSpyObserver();
        cityState.addObserver(spyObserver);

        // Act: Il tick scatena notifyObservers()
        cityState.processTick();

        // Assert: Verifica che la notifica sia arrivata
        assertTrue(spyObserver.wasUpdated, "L'observer doveva essere notificato durante il tick");
    }

    @Test
    void testRemoveObserver_StopsNotifications() {
        // Arrange
        TestSpyObserver spyObserver = new TestSpyObserver();
        cityState.addObserver(spyObserver); // Lo aggiungiamo
        cityState.removeObserver(spyObserver); // Lo rimuoviamo subito dopo

        // Act
        cityState.processTick();

        // Assert: La spia NON deve aver ricevuto l'aggiornamento
        assertFalse(spyObserver.wasUpdated, "L'observer rimosso non deve ricevere notifiche");
    }

    @Test
    void testUpdateStats_AppliesPolicyIfExists() {
        // Arrange
        Stats rawStats = new Stats(10, 100, 50, 20, 5);

        // Mock Strategy: raddoppia i soldi
        CityPolicyStrategy doubleMoneyPolicy = new CityPolicyStrategy() {
            @Override
            public Stats calculateStats(Stats baseStats) {
                return new Stats(
                        baseStats.getPollution(),
                        baseStats.getMoney() * 2,
                        baseStats.getHappiness(),
                        baseStats.getPopulation(),
                        baseStats.getEnergy()
                );
            }
        };
        cityState.setPolicy(doubleMoneyPolicy);

        // Act
        cityState.updateStats(rawStats);

        // Assert
        Stats finalStats = cityState.getCityStats();
        assertEquals(200, finalStats.getMoney(), "La policy doveva raddoppiare i soldi calcolati");
        assertEquals(10, finalStats.getPollution(), "Gli altri parametri dovevano rimanere inalterati");
    }

    // ------------------------------------------------------------------------
    // CLASSE DI SUPPORTO: Mock Observer per tracciare le chiamate di notifica
    // ------------------------------------------------------------------------
    private static class TestSpyObserver implements CityObserver {
        boolean wasUpdated = false;

        @Override
        public void update(Stats currentStats) {
            this.wasUpdated = true;
        }
    }
}