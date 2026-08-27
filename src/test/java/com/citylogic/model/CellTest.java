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

        /*
         > [Nota A: Perchè testare i default?]
         > Ci assicuriamo che una cella appena creata non sia erroneamente dichiarata
         > già operativa, libera o con coordinate/costi fantasma per sbaglio.
         */
        assertFalse(cell.isFree(), "Una cella appena creata deve nascere con free = false");
        assertFalse(cell.getFree(), "Il getter getFree() deve essere coerente e restituire false");
        assertFalse(cell.isOperative(), "Una cella appena creata deve nascere con isOperative = false");
        assertEquals(0, cell.getCost(), "Una cella generica appena creata deve avere un costo di default pari a 0");
        assertEquals(0, cell.getX(), "La coordinata X di default deve essere 0");
        assertEquals(0, cell.getY(), "La coordinata Y di default deve essere 0");
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
        assertTrue(cell.getFree(), "Il getter getFree() non ha registrato il cambio di stato");
        assertTrue(cell.isOperative(), "La cella non ha registrato il cambio di stato a 'operativa'");
    }

    @Test
    void testSetAndGetCost() {
        // Act
        int testCost = 350;
        cell.setCost(testCost);

        // Assert
        /*
         > [Nota C: Validazione Finanziaria Base]
         > Verifichiamo l'incapsulamento del parametro cost integrato in Cell.
         */
        assertEquals(testCost, cell.getCost(), "Il getter del costo non restituisce il valore assegnato dal setter");
    }

    @Test
    void testSetAndGetCoordinates() {
        // Act
        cell.setX(12);
        cell.setY(8);

        // Assert
        /*
         > [Nota D: Validazione Posizionamento Spaziale]
         > Assicuriamo che le coordinate vengano immagazzinate correttamente
         > per la serializzazione JSON e le logiche della Grid.
         */
        assertEquals(12, cell.getX(), "Il setter della coordinata X ha fallito");
        assertEquals(8, cell.getY(), "Il setter della coordinata Y ha fallito");
    }
}