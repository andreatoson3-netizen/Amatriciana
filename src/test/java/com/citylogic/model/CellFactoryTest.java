package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellFactoryTest {

    private CellFactory factory;

    @BeforeEach
    void setUp() {
        // Arrange globale: istanziamo la Factory prima di ogni test
        factory = new CellFactory();
    }

    @Test
    void testCreateAllValidCellTypes_HappyPath() {
        // Act & Assert
        // Verifichiamo il 100% di Branch Coverage dello switch
        // assertInstanceOf controllerà che l'oggetto creato esista e sia della classe corretta
        assertInstanceOf(Residential.class, factory.createCell("residential"));
        assertInstanceOf(Factory.class, factory.createCell("factory"));
        assertInstanceOf(Commercial.class, factory.createCell("commercial"));
        assertInstanceOf(PowerPlant.class, factory.createCell("powerplant"));
        assertInstanceOf(Road.class, factory.createCell("road"));
        assertInstanceOf(Park.class, factory.createCell("park"));
    }

    @Test
    void testCreateCellWithDirtyString_EquivalencePartitioning() {
        // Arrange: una stringa volutamente disordinata
        String dirtyInput = "   PoWeRpLaNt   ";

        // Act
        Cell result = factory.createCell(dirtyInput);

        // Assert
        // La factory deve pulire la stringa (trim + toLowerCase) e agganciare il case corretto
        assertInstanceOf(PowerPlant.class, result, "La sanificazione della stringa è fallita");
    }

    @Test
    void testCreateCellWithNull_Boundary() {
        // Act
        Cell result = factory.createCell(null);

        // Assert
        assertNull(result, "Passando null, la factory deve interrompersi e restituire null");
    }

    @Test
    void testCreateCellWithInvalidType_SadPath() {
        // Arrange
        String invalidInput = "ospedale";

        // Act & Assert
        // Verifichiamo che il default dello switch lanci l'eccezione prevista
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createCell(invalidInput),
                "La factory doveva lanciare un'eccezione per un edificio non previsto"
        );

        // Verifichiamo la qualità del log di errore
        assertTrue(exception.getMessage().contains(invalidInput),
                "Il messaggio di errore dovrebbe citare la stringa problematica");
    }
}