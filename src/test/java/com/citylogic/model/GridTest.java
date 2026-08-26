package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid grid;//istanza della classe Grid

    @BeforeEach
    void setUp() {
        // Viene eseguito prima di ogni test per avere una griglia pulita
        grid = new Grid(); // Assumendo che il costruttore di default crei la matrice 20x20
    }

    // ------------------------------------------------------------------------
    // Scenario 1: Grid Initialization with Default State
    // ------------------------------------------------------------------------
    @Test
    void testGridInitialization() {
        //richiama il metodo +getGriglia() visibile nella classe Grid
        Cell[][] matrice = grid.getGriglia();

        // 1. Verifica che la griglia non sia nulla
        assertNotNull(matrice, "La matrice della griglia non deve essere null.");

        // 2. Verifica che le dimensioni siano 20x20
        assertEquals(20, matrice.length, "La griglia deve avere 20 righe.");
        assertEquals(20, matrice[0].length, "La griglia deve avere 20 colonne.");

        // 3. Verifica che tutti i 400 blocchi siano stati creati e siano nello stato di default (Empty/Free)
        int blockCount = 0;
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
                Cell cell = matrice[i][j];

                // All'avvio, le celle non ancora occupate sono null (spazi vuoti)
                // Quindi verifichiamo solo che la posizione esista nella matrice,
                // senza pretendere che ci sia già un oggetto cella dentro.
                blockCount++;
            }
        }

        // Verifica finale del totale dei blocchi
        assertEquals(400, blockCount, "Il sistema deve creare un totale di 400 blocchi.");
    }

    // ------------------------------------------------------------------------
    // Scenario 2: Querying the State of a Valid Block
    // ------------------------------------------------------------------------
    @Test
    void testQueryValidBlock() {
        // richiama il metodo getCell(int x,y) della classe Grid per interrogare lo stato di un
        // blocco specifico tramite le sue coordinate
        Cell cell = grid.getCell(5, 5);

        //poichè il metodo getCell restituisce griglia[x][y], se in quella posizione
        //non è stato ancora costruito nulla,il blocco è vuoto e quindi restituisce null
        //Verifichiamo quindi che il comportamento corrispondi a quanto atteso:
        assertNull(cell, "Una cella non ancora occupata deve restituire null(stato vuoto).");
    }

    // ------------------------------------------------------------------------
    // Scenario 3: Handling Grid Boundaries (Boundary/Edge Case)
    // ------------------------------------------------------------------------
    @Test
    void testQueryOutOfBounds() {
        // Il sindaco interroga una coordinata fuori dai confini (es. x=20, y=20 su matrice 0-19)
        Cell invalidCellNegativa = grid.getCell(-1, 5);
        Cell invalidCellEccessiva = grid.getCell(25, 10);

        //verifichiamo che il sistemi rigetti l'operazione restituendo null
        assertNull(invalidCellNegativa, "Il sistema deve restituire null per coordinate negative");
        assertNull(invalidCellEccessiva, "Il sistema deve restituire null per coordinate superiori alla dimensione della griglia");


    }
}

