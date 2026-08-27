package com.citylogic.persistence;

import com.citylogic.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CityPersistenceManagerTest {

    private CityPersistenceManager persistenceManager;

    @BeforeEach
    void setUp() {
        // Arrange iniziale: istanziamo il nostro manager della persistenza
        persistenceManager = new CityPersistenceManager();
    }

    @Test
    void testSaveAndLoadCity_HappyPath(@TempDir Path tempDir) {
        // Arrange
        // Creiamo un oggetto City di test e inizializziamo il budget
        City originalCity = new City();
        originalCity.initCity();
        originalCity.initNewGameBudget(); // Imposta il budget iniziale (es. 5000)

        // Creiamo un percorso file temporaneo sicuro per il test
        File tempFile = tempDir.resolve("test_city.json").toFile();
        String filePath = tempFile.getAbsolutePath();

        // Act
        // 1. Salviamo la città su file JSON
        persistenceManager.saveCity(originalCity, filePath);

        // Verifichiamo che il file sia stato effettivamente creato
        assertTrue(tempFile.exists(), "Il file JSON della città deve essere stato creato");

        // 2. Carichiamo la città dal file JSON appena creato
        City loadedCity = persistenceManager.loadCity(filePath);

        // Assert
        // Verifichiamo che la città caricata non sia nulla e mantenga lo stato corretto
        assertNotNull(loadedCity, "La città caricata non deve essere null");
        assertNotNull(loadedCity.getCityState(), "Lo stato interno della città caricata non deve essere null");
        assertNotNull(loadedCity.getCityState().getCityStats(), "Le statistiche della città caricata non devono essere null");

        // Verifichiamo che il budget sia stato preservato correttamente attraverso la serializzazione JSON
        assertEquals(originalCity.getCityState().getCityStats().getMoney(),
                loadedCity.getCityState().getCityStats().getMoney(),
                "Il budget della città caricata deve corrispondere a quello salvato");
    }

    @Test
    void testLoadCity_FileNotExists() {
        // Arrange
        String nonExistentPath = "file_che_non_esiste_abc123.json";

        // Act
        City loadedCity = persistenceManager.loadCity(nonExistentPath);

        // Assert
        // Verifichiamo che se il file non esiste, il metodo gestisca la cosa restituendo null in sicurezza
        assertNull(loadedCity, "Caricare un file inesistente deve restituire null");
    }

    @Test
    void testSaveCity_NullParameters() {
        // Act & Assert
        // Verifichiamo che passare parametri null non lanci eccezioni impreviste (gestione difensiva)
        assertDoesNotThrow(() -> persistenceManager.saveCity(null, "path.json"),
                "Salvare una città null non deve generare eccezioni");
        assertDoesNotThrow(() -> persistenceManager.saveCity(new City(), null),
                "Salvare con un filePath null non deve generare eccezioni");
    }

    @Test
    void testLoadCity_NullPath() {
        // Act
        City loadedCity = persistenceManager.loadCity(null);

        // Assert
        assertNull(loadedCity, "Passare un path null al caricamento deve restituire null");
    }
}