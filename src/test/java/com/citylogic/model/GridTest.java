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

        // Act: Il nuovo motore richiede l'esecuzione a due passaggi
        grid.distributeEnergy(); // <-- PASSO MANCANTE: spegne la casa senza energia
        Stats totalStats = grid.calculateRawStats();

        // Assert: Essendo ibernata, i suoi introiti e abitanti devono essere ignorati
        assertEquals(0, totalStats.getMoney(), "Un'area residenziale senza energia non deve produrre introiti");
        assertEquals(0, totalStats.getPopulation(), "Un'area residenziale senza energia non deve produrre popolazione");
    }

    @Test
    void testCalculateRawStats_RuleEnforcement_HappyPath() {
        // Arrange: Inseriamo SIA un'area residenziale CHE una centrale elettrica vicina
        Residential res = new Residential();
        res.setFree(false);
        grid.setCell(res, 10, 10);

        PowerPlant power = new PowerPlant();
        power.setFree(false);
        grid.setCell(power, 9, 8);

        // Act: Il nuovo motore a due passaggi
        grid.distributeEnergy(); // <-- Riempie il serbatoio e accende la casa
        Stats totalStats = grid.calculateRawStats();

        // Assert: Essendoci energia, le statistiche devono essersi sommate regolarmente
        assertTrue(totalStats.getPollution() != 0 && totalStats.getPopulation() != 0,
                "Le statistiche non sono state aggregate correttamente nonostante la presenza di energia");
    }

    @Test
    void testRemoveCell_HappyPath() {
        // Arrange
        Residential res = new Residential();
        grid.setCell(res, 5, 5);

        // Act
        Cell removed = grid.removeCell(5, 5);

        // Assert
        assertNotNull(removed, "La cella rimossa non deve essere null");
        assertEquals(500, removed.getCost(), "La cella rimossa deve preservare il suo costo originale");
        assertNull(grid.getCell(5, 5), "La griglia deve risultare vuota alle coordinate 5,5");
    }

    @Test
    void testDistributeEnergy_BlackoutQueue() {
        // Arrange: 1 Centrale (+100 Energia) e 6 Fabbriche (-20 Energia l'una = -120 Totale)
        PowerPlant power = new PowerPlant();
        grid.setCell(power, 0, 0);

        for(int i = 1; i <= 6; i++) {
            grid.setCell(new Factory(), 0, i);
        }

        // Act
        grid.distributeEnergy();

        // Assert
        assertEquals(1, grid.getBlackoutQueue().size(), "Ci deve essere esattamente 1 fabbrica nella coda di blackout");
        // Verifica che calcolando le statistiche grezze, l'ultima fabbrica spenta venga ignorata
        Stats total = grid.calculateRawStats();
        assertTrue(total.getMoney() > 0, "Le fabbriche accese devono generare denaro");
    }
    @Test
    void testCountUnpoweredResidential() {
        // Arrange
        // Piazziamo 1 centrale e 2 case lontane (isolate), pi  1 casa vicina (alimentata)
        PowerPlant power = new PowerPlant();
        grid.setCell(power, 0, 0); // Centrale in 0,0 (raggio di copertura: coordinate la cui somma  <= 8)

        Residential vicina = new Residential();
        grid.setCell(vicina, 2, 2); // Distanza 4 (Alimentata)

        Residential lontana1 = new Residential();
        grid.setCell(lontana1, 15, 15); // Distanza 30 (Isolata)

        Residential lontana2 = new Residential();
        grid.setCell(lontana2, 19, 0); // Distanza 19 (Isolata)

        // Act
        int unpoweredCount = grid.countUnpoweredResidential();

        // Assert
        assertEquals(2, unpoweredCount, "Il contatore deve rilevare esattamente 2 zone residenziali fuori raggio");
    }

}