
    package com.citylogic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

    class GameControllerTest {

        private GameController controller;

        @BeforeEach
        void setUp() {
            controller = new GameController();
        }

        @Test
        void testStartNewGame_InitializesBudget() {
            controller.startNewGame();
            int money = controller.getMoney();
            assertEquals(5000, money, "Il nuovo gioco deve partire con un budget di 5000");
        }

        @Test
        void testSetCell_SuccessAndBudgetDeduction() {
            controller.startNewGame();
            
            GameController.BuildResult result =
                controller.placeBuilding("residential", 2, 2);

            assertEquals(GameController.BuildResult.SUCCESS, result, "Il piazzamento della casa deve avere successo con budget pieno");

            int remainingMoney = controller.getMoney();
            assertEquals(4500, remainingMoney, "Il budget deve essere decrementato del costo dell'edificio (500)");
        }

        @Test
        void testSetCell_InsufficientFunds() {
            controller.startNewGame();

            // Simuliamo fondi insufficienti forzando il budget a 100
            controller.setMoney(100);

            GameController.BuildResult result =
                controller.placeBuilding("factory", 0, 0);

            assertEquals(GameController.BuildResult.NO_FUNDS, result, "Il piazzamento deve fallire se i fondi non sono sufficienti");
            assertEquals(100, controller.getCity().getCityState().getCityStats().getMoney(), "Il budget non deve essere modificato in caso di fallimento");
        }

        @Test
        void testActivatePolicy() {
            controller.startNewGame();

            controller.activatePolicy("environmental");

            assertNotNull(controller.getCurrentPolicy(), "La policy deve essere attivata correttamente");
        }

        @Test
        void testSaveAndLoadGame() {
            controller.startNewGame();
            String testFile = "controller_save_test.json";

            // Modifichiamo lo stato (es. spendiamo soldi)
            GameController.BuildResult result =
                controller.placeBuilding("residential", 1, 1);

            assertEquals(GameController.BuildResult.SUCCESS, result, "Il piazzamento della casa deve avere successo");
            
            int moneyBeforeSave = controller.getMoney();

            // Salvataggio
            boolean saved = controller.saveGame(testFile);
            assertTrue(saved, "Il salvataggio tramite controller deve riuscire");

            // Avviamo un nuovo gioco (resetta i soldi a 5000)
            controller.startNewGame();
            assertEquals(5000, controller.getMoney());

            // Caricamento
            boolean loaded = controller.loadGame(testFile);
            assertTrue(loaded, "Il caricamento tramite controller deve riuscire");

            int moneyAfterLoad = controller.getMoney();
            assertEquals(moneyBeforeSave, moneyAfterLoad, "Il budget ricaricato deve corrispondere allo stato salvato");

            // Pulizia file di test
            File f = new File(testFile);
            if (f.exists()) {
                f.delete();
            }
        }
    }

