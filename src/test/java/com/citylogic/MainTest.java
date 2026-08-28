package com.citylogic;

import org.junit.jupiter.api.Test;
import java.awt.GraphicsEnvironment;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void testMainExecution_SmokeTest() {
        // Guard Clause per ambienti server senza interfaccia grafica
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        // Act & Assert
        // Uno "Smoke Test" si limita a verificare che l'applicazione si accenda
        // senza prendere letteralmente fuoco (lanciare eccezioni fatali all'avvio).
        assertDoesNotThrow(() -> Main.main(new String[]{}),
                "Il metodo main ha generato un'eccezione bloccante durante l'avvio");
    }
}