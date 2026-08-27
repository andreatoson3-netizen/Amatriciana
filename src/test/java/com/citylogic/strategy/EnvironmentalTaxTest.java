package com.citylogic.strategy;

import com.citylogic.model.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnvironmentalTaxTest {

    private EnvironmentalTax strategy;

    // Fixture: isoliamo il test creando una nuova istanza prima di ogni esecuzione
    @BeforeEach
    void setUp() {
        // Arrange globale
        strategy = new EnvironmentalTax();
    }

    @Test
    void testCalculateStats_HappyPath() {
        // Arrange: Partiamo da valori facili (100) per verificare le percentuali al volo
        Stats rawStats = new Stats(100, 100, 100, 50, 10);

        // Act
        Stats result = strategy.calculateStats(rawStats);

        // Assert: Verifica matematica rigorosa
        assertEquals(80, result.getPollution(), "Inquinamento non ridotto del 20%");
        assertEquals(95, result.getMoney(), "Soldi non ridotti del 5%");
        assertEquals(110, result.getHappiness(), "Felicità non aumentata del 10%");

        // Verifica dei valori che non devono subire alterazioni
        assertEquals(50, result.getPopulation(), "La popolazione doveva rimanere invariata");
        assertEquals(10, result.getEnergy(), "L'energia doveva rimanere invariata");
    }

    @Test
    void testCalculateStats_ZeroValues_Boundary() {
        // Arrange: Test dei valori limite (0)
        Stats zeroStats = new Stats(0, 0, 0, 0, 0);

        // Act
        Stats result = strategy.calculateStats(zeroStats);

        // Assert: Nessuna percentuale deve generare valori anomali partendo da 0
        assertEquals(0, result.getPollution());
        assertEquals(0, result.getMoney());
        assertEquals(0, result.getHappiness());
    }

    @Test
    void testCalculateStats_NullInput_SadPath() {
        // Arrange
        Stats nullStats = null;

        // Act & Assert
        // Il calcolo non deve esplodere ricevendo un parametro null
        Stats result = assertDoesNotThrow(() -> strategy.calculateStats(nullStats),
                "Il calcolo è andato in crash ricevendo un parametro null");

        assertNotNull(result, "Il risultato non deve mai essere null");
        assertEquals(0, result.getMoney(), "In caso di null, deve restituire statistiche vuote a zero");
    }

    @Test
    void testCalculateStats_Immutability() {
        // Arrange
        Stats originalStats = new Stats(100, 100, 100, 50, 10);

        // Act
        Stats result = strategy.calculateStats(originalStats);

        // Assert
        // Verifica che la strategia rispetti l'incapsulamento restituendo una copia nuova
        assertNotSame(originalStats, result, "Restituito il puntatore originale invece di una nuova istanza");

        // Verifica che l'oggetto passato non sia stato alterato
        assertEquals(100, originalStats.getPollution(), "L'oggetto originale ha subito side-effects");
    }
}