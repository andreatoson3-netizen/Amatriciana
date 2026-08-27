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
        // 1. Coordinata valida
        assertDoesNotThrow(() -> grid.getCell(0, 0), "Crash su coordinate 0,0 valide");
        assertNull(grid.getCell(0, 0), "Una griglia appena creata deve avere celle null");

        // 2. Limiti negativi (Out of Bounds)
        assertNull(grid.getCell(-1, 5), "Coordinate negative non protette (X)");
        assertNull(grid.getCell(5, -1), "Coordinate negative non protette (Y)");

        // 3. Limiti superiori (Out of Bounds - max indice 19)
        assertNull(grid.getCell(20, 5), "Limite superiore X (20) non protetto");
        assertNull(grid.getCell(5, 20), "Limite superiore Y (20) non protetto");
    }

    @Test
    void testSetCell_ValidAndInvalidPlacements() {
        // Arrange
        Residential newBuilding = new Residential();

        // Act & Assert - Happy Path (Piazzamento valido)
        boolean isPlaced = grid.setCell(newBuilding, 10, 10);
        assertTrue(isPlaced, "setCell deve restituire true per coordinate valide e casella vuota");
        assertEquals(10, newBuilding.getX(), "setCell non ha aggiornato la coordinata X interna dell'edificio");
        assertEquals(10, newBuilding.getY(), "setCell non ha aggiornato la coordinata Y interna dell'edificio");

        // Act & Assert - Sad Path (Casella già occupata)
        newBuilding.setFree(false); // Simuliamo che l'edificio sia costruito/occupato
        Factory factory = new Factory();
        boolean isOverwritten = grid.setCell(factory, 10, 10);
        assertFalse(isOverwritten, "setCell deve restituire false se cerca di sovrascrivere una casella occupata");

        // Act & Assert - Sad Path (Fuori limite)
        boolean isOutOfBounds = grid.setCell(new Residential(), 25, 25);
        assertFalse(isOutOfBounds, "setCell deve restituire false se le coordinate sono fuori dalla griglia");
    }

    @Test
    void testHasPowerPlant_SpatialSearch() {
        // Arrange: Griglia inizialmente vuota
        assertFalse(grid.hasPowerPlant(), "Una griglia vuota non dovrebbe avere centrali");

        // Act: Inserimento tramite il metodo ufficiale
        PowerPlant powerPlant = new PowerPlant();
        powerPlant.setFree(false);
        grid.setCell(powerPlant, 5, 5);

        // Assert: Ricerca della centrale
        assertTrue(grid.hasPowerPlant(), "Il metodo non ha rilevato la centrale appena inserita");
    }

    @Test
    void testCalculateRawStats_RuleEnforcement_SadPath() {
        // Arrange: Area residenziale SENZA centrale elettrica
        Residential res = new Residential();
        res.setFree(false);
        grid.setCell(res, 10, 10);

        // Act
        Stats totalStats = grid.calculateRawStats();

        // Assert: Senza energia, l'area residenziale viene ignorata
        assertEquals(0, totalStats.getMoney(), "Un'area residenziale senza energia non deve produrre introiti");
        assertEquals(0, totalStats.getPopulation(), "Un'area residenziale senza energia non deve produrre popolazione");
    }

    @Test
    void testCalculateRawStats_RuleEnforcement_HappyPath() {
        // Arrange: Inseriamo SIA un'area residenziale CHE una centrale elettrica
        Residential res = new Residential();
        res.setFree(false);
        grid.setCell(res, 10, 10);

        PowerPlant power = new PowerPlant();
        power.setFree(false);
        grid.setCell(power, 5, 5);

        // Act
        Stats totalStats = grid.calculateRawStats();

        // Assert: Essendoci energia, le statistiche devono essersi sommate regolarmente
        assertTrue(totalStats.getPollution() != 0 && totalStats.getPopulation() != 0,
                "Le statistiche non sono state aggregate correttamente nonostante la presenza di energia");
    }
}