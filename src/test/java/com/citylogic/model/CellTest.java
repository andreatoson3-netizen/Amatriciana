package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    // [Concetto 1: La Dummy Class]
    /**
     * Creiamo una classe fittizia concreta (Dummy) interna al test.
     * Estende Cell e implementa il metodo astratto con un corpo vuoto.
     * Ci serve SOLO come "cavalletto" per istanziare un oggetto di tipo Cell.
     */
    private static class DummyCell extends Cell {
        @Override
        public Stats returnStat() {
            // Ritorno fittizio, non viene testato in questa suite
            return new Stats();
        }
    }

    private Cell cell;

    @BeforeEach
    void setUp() {
        // Arrange globale: Inizializziamo il nostro "cavalletto"
        cell = new DummyCell();
    }

    @Test
    void testCellDefaultState_HappyPath() {
        // Act & Assert
        // Verifichiamo i valori di default di Java all'istanza dell'oggetto.
        // I boolean in Java nascono a 'false' se non inizializzati diversamente.

        /*
         > [Nota A: Perchè testare i default?]
         > Ci assicuriamo che una cella appena creata non sia erroneamente dichiarata
         > già operativa o libera per sbaglio da future modifiche al costruttore.
         */
        assertFalse(cell.isFree(), "Una cella appena creata deve nascere con free = false");
        assertFalse(cell.isOperative(), "Una cella appena creata deve nascere con isOperative = false");
    }

    @Test
    void testStateMutation_EquivalencePartitioning() {
        // Act
        // Modifichiamo lo stato del dominio (non sono banali assegnazioni, ma logiche di gioco)
        cell.setFree(true);
        cell.setOperative(true);

        // Assert
        /*
         > [Nota B: Verifica della Mutazione]
         > Assicuriamo che il meccanismo interno della superclasse conservi
         > correttamente il cambio di stato, essenziale per la logica della Grid.
         */
        assertTrue(cell.isFree(), "La cella non ha registrato il cambio di stato a 'libera'");
        assertTrue(cell.isOperative(), "La cella non ha registrato il cambio di stato a 'operativa'");
    }
}