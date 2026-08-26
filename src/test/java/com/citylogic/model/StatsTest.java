package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    private Stats baseStats;

    // Fixture: esegue questo blocco prima di OGNI singolo @Test per garantire l'isolamento.
    @BeforeEach
    void setUp() {
        // Arrange globale
        baseStats = new Stats(10, 100, 50, 20, 5);
    }

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Stats defaultStats = new Stats();

        // Assert: verifica i valori a 0 essenziali per Jackson
        assertEquals(0, defaultStats.getPollution(), "Pollution non inizializzata a 0");
        assertEquals(0, defaultStats.getMoney(), "Money non inizializzata a 0");
        assertEquals(0, defaultStats.getHappiness(), "Happiness non inizializzata a 0");
        assertEquals(0, defaultStats.getPopulation(), "Population non inizializzata a 0");
        assertEquals(0, defaultStats.getEnergy(), "Energy non inizializzata a 0");
    }

    @Test
    void testAddValidStats() {
        // Arrange
        // Aggiungiamo un mix di valori negativi e positivi per testare le Classi di Equivalenza
        Stats additionalStats = new Stats(-5, 50, -10, 10, 0);

        // Act
        baseStats.add(additionalStats);

        // Assert
        assertEquals(5, baseStats.getPollution(), "Addizione pollution fallita (10 - 5)");
        assertEquals(150, baseStats.getMoney(), "Addizione money fallita (100 + 50)");
        assertEquals(40, baseStats.getHappiness(), "Addizione happiness fallita (50 - 10)");
        assertEquals(30, baseStats.getPopulation(), "Addizione population fallita (20 + 10)");
        assertEquals(5, baseStats.getEnergy(), "Addizione energy fallita (5 + 0)");
    }

    @Test
    void testAddNullStats_SadPath() {
        // Arrange
        // (baseStats già inizializzata dal @BeforeEach)

        // Act & Assert
        // Verifica che il metodo non lanci una NullPointerException se gli passi null
        assertDoesNotThrow(() -> baseStats.add(null), "L'aggiunta di un oggetto null ha causato un crash");

        // Assert: verifica che i valori originali siano rimasti inalterati
        assertEquals(10, baseStats.getPollution(), "L'oggetto originale è stato corrotto da un input null");
    }

    @Test
    void testMultiplyWithTruncation_BoundaryValue() {
        // Arrange
        double factor = 1.5;

        // Act
        Stats multipliedStats = baseStats.multiply(factor);

        // Assert
        // Verifichiamo che il cast a (int) esegua il troncamento senza arrotondamenti per eccesso.
        // Energy era 5. 5 * 1.5 = 7.5 -> castato a (int) diventa 7.
        assertEquals(15, multipliedStats.getPollution());
        assertEquals(150, multipliedStats.getMoney());
        assertEquals(75, multipliedStats.getHappiness());
        assertEquals(30, multipliedStats.getPopulation());
        assertEquals(7, multipliedStats.getEnergy(), "Il troncamento decimale da 7.5 a 7 ha fallito");
    }

    @Test
    void testMultiplyImmutability() {
        // Arrange
        double factor = 2.0;

        // Act
        Stats newStats = baseStats.multiply(factor);

        // Assert
        // Assicuriamoci che multiply() restituisca una nuova istanza...
        assertNotSame(baseStats, newStats, "multiply() ha restituito la stessa referenza, violando l'immutabilità");
        // ... e che l'istanza vecchia non sia stata modificata per side-effect
        assertEquals(10, baseStats.getPollution(), "L'oggetto originale è stato alterato da multiply()");
    }
}