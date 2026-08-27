package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private Grid grid;

    @BeforeEach
    void setUp() {
        // Arrange globale: istanziamo una griglia pulita 20x20
        grid = new Grid();
    }

    @Test
    void testGetCell_BoundaryValueAnalysis() {
        // Act & Assert
        // 1. Coordinata valida (deve restituire null perché è vuota, ma non crashare)
        assertDoesNotThrow(() -> grid.getCell(0, 0), "Crash su coordinate 0,0 valide");
        assertNull(grid.getCell(0, 0));

        // 2. Limite negativo (Out of Bounds)
        assertNull(grid.getCell(-1, 5), "Coordinate negative non protette");
        assertNull(grid.getCell(5, -1), "Coordinate negative non protette");

        // 3. Limite superiore (Out of Bounds - la griglia è 20x20, quindi max indice è 19)
        assertNull(grid.getCell(20, 5), "Limite superiore X (20) non protetto");
        assertNull(grid.getCell(5, 20), "Limite superiore Y (20) non protetto");
    }

    @Test
    void testHasPowerPlant_SpatialSearch() {
        // Arrange: Griglia inizialmente vuota
        assertFalse(grid.hasPowerPlant(), "Una griglia vuota non dovrebbe avere centrali");

        // Act: Forziamo l'inserimento di una centrale nell'array (violando l'incapsulamento per test)
        grid.getGriglia()[5][5] = new PowerPlant();

        // Assert: Il metodo deve scandire la matrice e trovarla
        assertTrue(grid.hasPowerPlant(), "Il metodo non ha rilevato la centrale appena inserita");
    }

    @Test
    void testCalculateRawStats_RuleEnforcement_SadPath() {
        // Arrange: Inseriamo un'area residenziale MA nessuna centrale elettrica
        Residential res = new Residential();
        res.setFree(false); // Simuliamo che sia costruita
        grid.getGriglia()[10][10] = res;

        // Act
        Stats totalStats = grid.calculateRawStats();

        // Assert: Senza energia, la regola impone che l'area residenziale venga ignorata (blocco continue)
        assertEquals(0, totalStats.getMoney(), "Un'area residenziale senza energia non dovrebbe produrre statistiche");
        assertEquals(0, totalStats.getPopulation(), "Un'area residenziale senza energia non dovrebbe produrre popolazione");
    }

    @Test
    void testCalculateRawStats_RuleEnforcement_HappyPath() {
        // Arrange: Inseriamo SIA un'area residenziale CHE una centrale elettrica
        Residential res = new Residential();
        res.setFree(false);
        PowerPlant power = new PowerPlant();
        power.setFree(false);

        grid.getGriglia()[10][10] = res;
        grid.getGriglia()[5][5] = power;

        // Act
        Stats totalStats = grid.calculateRawStats();

        // Assert: Essendoci energia, le statistiche devono essere maggiori di zero
        // (Almeno la centrale inquina e costa, e il residenziale produce abitanti/soldi)
        assertTrue(totalStats.getPollution() != 0 || totalStats.getPopulation() != 0,
                "Le statistiche non sono state aggregate correttamente nonostante la presenza di energia");
    }
}