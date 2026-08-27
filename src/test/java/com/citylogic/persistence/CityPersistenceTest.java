//TEST USER STORY LOAD A SAVED CITY

package com.citylogic.persistence;

import com.citylogic.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CityPersistenceManagerTest {

    // Istanza di CityPersistenceManager
    // Deriva dal Design Class Model e gestisce la persistenza I/O (salvataggio e caricamento JSON).
    private CityPersistenceManager persistenceManager;

    @BeforeEach
    void setUp() {
        // Inizializza il manager prima di ogni singolo test
        persistenceManager = new CityPersistenceManager();
    }

    // ------------------------------------------------------------------------
    // Scenario 1: Load a valid saved city (User Story: Load a saved City)
    // ------------------------------------------------------------------------
    @Test
    void testSaveAndLoadValidCity(@TempDir Path tempDir) throws IOException {
        // @TempDir di JUnit 5 crea una cartella temporanea isolata per il test,
        // evitando di sporcare il workspace con file di salvataggio fittizi.
        File tempFile = tempDir.resolve("test_city_save.json").toFile();
        String filePath = tempFile.getAbsolutePath();

        // 1. Creiamo un oggetto City valido basato sulla classe di produzione
        City originalCity = new City();

        // Inizializziamo lo stato interno (richiamando initCity() come visto nel codice della classe City,
        // così che cityState non sia null e contenga dati strutturali validi da serializzare).
        originalCity.initCity();

        // 2. Salviamo la città su file JSON usando il metodo saveCity()
        persistenceManager.saveCity(originalCity, filePath);

        // Verifica strutturale: il file di salvataggio deve essere stato creato correttamente su disco
        assertTrue(tempFile.exists(), "Il file di salvataggio JSON deve essere creato con successo.");

        // 3. Ricarichiamo la città dal file appena salvato usando loadCity()
        City loadedCity = persistenceManager.loadCity(filePath);

        // 4. Asserzioni finali (Scenario 1 della User Story)
        assertNotNull(loadedCity, "La città caricata non deve essere null.");

        // Verifichiamo che il caricamento abbia ripristinato correttamente anche lo stato interno (CityState)
        assertNotNull(loadedCity.getCityState(), "Lo stato interno (CityState) della città deve essere ripristinato dal file JSON.");
    }

    // ------------------------------------------------------------------------
    // Scenario 2: Invalid or unreadable save file (User Story: Load a saved City)
    // ------------------------------------------------------------------------
    @Test
    void testLoadNonExistentOrInvalidFile() {
        // Simuliamo il tentativo del Sindaco di caricare un file che non esiste nel percorso specificato.
        String nonExistentPath = "file_che_non_esiste_123456.json";

        // Dal codice di CityPersistenceManager sappiamo che se il file non esiste,
        // il metodo intercetta il controllo e restituisce esplicitamente 'null'.
        City loadedCity = persistenceManager.loadCity(nonExistentPath);

        // Verifichiamo che il sistema rifiuti il caricamento non valido restituendo null (stato invariato)
        assertNull(loadedCity, "Il sistema deve restituire null se il file non esiste o non è leggibile.");
    }

    // Test di robustezza per parametri nulli
    @Test
    void testSaveOrLoadWithNullParameters() {
        // Verifica che passare argomenti nulli ai metodi non provochi crash imprevisti dell'applicazione
        assertDoesNotThrow(() -> {
            persistenceManager.saveCity(null, null);
            City result = persistenceManager.loadCity(null);
            assertNull(result, "Il caricamento con un percorso nullo deve restituire null in sicurezza.");
        }, "I metodi di persistenza devono gestire in modo difensivo i parametri nulli.");
    }
}
