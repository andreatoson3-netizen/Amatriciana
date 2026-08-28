package com.citylogic.view;

import com.citylogic.controller.GameController;
import com.citylogic.model.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CityDashboardTest {

    private GameController controller;
    private CityDashboard dashboard;

    @BeforeEach
    void setUp() {
        // Guard Clause: Se il test gira su un server senza monitor (es. GitHub Actions),
        // saltiamo il setup per evitare la HeadlessException.
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        controller = new GameController();
        dashboard = new CityDashboard(controller);
    }

    @Test
    void testDashboardInitialization_HappyPath() {
        if (GraphicsEnvironment.isHeadless()) return;

        // Assert: Verifica che la finestra esista e abbia i parametri vitali corretti
        assertNotNull(dashboard, "La dashboard non deve essere null dopo l'istanziazione");
        assertEquals("SimCity Lite", dashboard.getTitle(), "Il titolo della finestra è errato");
        assertEquals(JFrame.EXIT_ON_CLOSE, dashboard.getDefaultCloseOperation(), "L'operazione di chiusura deve terminare il processo");
    }

    @Test
    void testUpdate_DoesNotCrashUI() {
        if (GraphicsEnvironment.isHeadless()) return;

        // Arrange: Creiamo statistiche sicure (no bancarotta, no rivolta) per evitare popup bloccanti
        Stats safeStats = new Stats(10, 5000, 50, 100, 50);

        // Act & Assert
        // Verifichiamo che la notifica dal pattern Observer non faccia esplodere i thread di Swing
        assertDoesNotThrow(() -> dashboard.update(safeStats),
                "L'aggiornamento dell'interfaccia ha generato un'eccezione non gestita");
    }
}