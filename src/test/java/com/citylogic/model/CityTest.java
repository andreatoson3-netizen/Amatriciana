package com.citylogic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    private City city;

    @BeforeEach
    void setUp() {
        // Arrange globale
        city = new City();
    }

    @Test
    void testInitialState_IsNullBeforeInit() {
        // Assert
        // Prima di chiamare initCity(), cityState non deve essere allocato
        assertNull(city.getCityState(), "CityState deve essere null prima di initCity()");
    }

    @Test
    void testInitCity_CreatesCityStateInstance() {
        // Act
        city.initCity();

        // Assert
        assertNotNull(city.getCityState(), "initCity() deve istanziare un nuovo CityState");
    }

    @Test
    void testProcessTick_WhenCityStateIsNull_DoesNotCrash() {
        // Act & Assert
        // Verifica che la guard clause impedisca un NullPointerException
        assertDoesNotThrow(() -> city.processTick(),
                "Invocare processTick() con cityState null non deve lanciare eccezioni");
    }

    @Test
    void testProcessTick_WhenCityStateIsInitialized_ExecutesSuccessfully() {
        // Arrange
        city.initCity();

        // Act & Assert
        // Verifica la corretta delegazione a CityState
        assertDoesNotThrow(() -> city.processTick(),
                "Invocare processTick() con cityState inizializzato deve completarsi con successo");
    }

    // ------------------------------------------------------------------------
    // NUOVI TEST: Inizializzazione Budget
    // ------------------------------------------------------------------------

    @Test
    void testInitNewGameBudget_WhenCityStateIsNull_DoesNotCrash() {
        // Act & Assert (Sad Path)
        // City non è ancora inizializzata (cityState è null)
        assertDoesNotThrow(() -> city.initNewGameBudget(),
                "Invocare initNewGameBudget() prima di initCity() non deve causare crash");
    }

    @Test
    void testInitNewGameBudget_SetsMoneyTo5000() {
        // Arrange (Happy Path)
        city.initCity(); // 1. Creiamo lo stato della città

        // Act
        city.initNewGameBudget(); // 2. Applichiamo il budget

        // Assert
        // Andiamo a pescare il valore profondo per verificare l'assegnazione
        int actualMoney = city.getCityState().getCityStats().getMoney();
        assertEquals(5000, actualMoney, "Il metodo deve forzare il budget iniziale esattamente a 5000");
    }
}