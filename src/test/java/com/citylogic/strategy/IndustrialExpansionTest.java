package com.citylogic.strategy;

import com.citylogic.model.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndustrialExpansionTest {

    private IndustrialExpansion strategy;

    // Fixture: creiamo un'istanza pulita della policy prima di ogni test
    @BeforeEach
    void setUp() {
        // Arrange globale
        strategy = new IndustrialExpansion();
    }

    @Test
    void testCalculateStats_HappyPath() {
        // Arrange: Valori a 100 per validazione immediata delle percentuali
        Stats rawStats = new Stats(100, 100, 100, 50, 10);

        // Act
        Stats result = strategy.calculateStats(rawStats);

        // Assert: Verifica matematica rigorosa
        assertEquals(140, result.getPollution(), "Inquinamento non aumentato del 40%");
        assertEquals(130, result.getMoney(), "Soldi non aumentati del 30%");
        assertEquals(90, result.getHappiness(), "Felicità non ridotta del 10%");

        // Verifica dei valori non influenzati o con bonus fissi
        assertEquals(50, result.getPopulation(), "La popolazione doveva rimanere invariata");
        assertEquals(25, result.getEnergy(), "Bonus energia (+15) fallito (10 + 15 deve fare 25)");
    }

    @Test
    void testCalculateStats_ZeroValues_Boundary() {
        // Arrange: Partiamo da statistiche tutte azzerate
        Stats zeroStats = new Stats(0, 0, 0, 0, 0);

        // Act
        Stats result = strategy.calculateStats(zeroStats);

        // Assert: I fattori moltiplicativi restano a zero, ma il bonus additivo deve applicarsi
        assertEquals(0, result.getPollution());
        assertEquals(0, result.getMoney());
        assertEquals(0, result.getHappiness());
        assertEquals(15, result.getEnergy(), "Il bonus additivo fisso di 15 deve applicarsi anche con energia di base a 0");
    }

    @Test
    void testCalculateStats_NullInput_SadPath() {
        // Arrange: Simuliamo il passaggio di un oggetto non instanziato
        Stats nullStats = null;

        // Act & Assert
        // Verifica che la guard clause impedisca il crash per NullPointerException
        Stats result = assertDoesNotThrow(() -> strategy.calculateStats(nullStats),
                "La politica è andata in crash ricevendo un input null");

        // Assicuriamo che l'oggetto restituito sia neutro e istanziato
        assertNotNull(result, "L'oggetto salvagente restituito non deve essere null");
        assertEquals(0, result.getMoney(), "L'oggetto salvagente deve avere statistiche a 0");
        assertEquals(0, result.getEnergy(), "L'oggetto salvagente deve avere statistiche a 0");
    }

    @Test
    void testCalculateStats_Immutability() {
        // Arrange
        Stats originalStats = new Stats(100, 100, 100, 50, 10);

        // Act
        Stats result = strategy.calculateStats(originalStats);

        // Assert
        // Verifica l'incapsulamento: la politica deve sputare un oggetto nuovo, non alterare il vecchio
        assertNotSame(originalStats, result, "Restituito il puntatore originale invece di una nuova istanza");

        // Il vecchio oggetto non deve aver subito side-effects
        assertEquals(10, originalStats.getEnergy(), "L'oggetto originale ha subito side-effects");
    }

    /*
    // [ATTENZIONE: SCOMMENTARE QUESTO TEST E CANCELLARE testCalculateStats_HappyPath
    // QUANDO IL TEAM PASSERA' AL MOLTIPLICATORE PERCENTUALE PER L'ENERGIA]

    @Test
    void testCalculateStats_EnergyMultiplier_HappyPath() {
        // Arrange: Partiamo da valori a 100 per validare facilmente le percentuali
        Stats rawStats = new Stats(100, 100, 100, 50, 100);

        // Act
        Stats result = strategy.calculateStats(rawStats);

        // Assert: Verifica matematica rigorosa
        assertEquals(140, result.getPollution(), "Inquinamento non aumentato del 40%");
        assertEquals(130, result.getMoney(), "Soldi non aumentati del 30%");
        assertEquals(90, result.getHappiness(), "Felicità non ridotta del 10%");
        assertEquals(50, result.getPopulation(), "La popolazione doveva rimanere invariata");

        // Verifica del nuovo fattore moltiplicativo sull'energia (es. +20%)
        // 100 * 1.20 = 120
        assertEquals(120, result.getEnergy(), "Moltiplicatore energia errato o non applicato");
    }
    */
}