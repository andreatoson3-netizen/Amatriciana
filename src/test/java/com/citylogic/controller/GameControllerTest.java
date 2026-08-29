
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
            assertEquals(100, controller.getMoney(), "Il budget non deve essere modificato in caso di fallimento");
        }

        @Test
        void testActivatePolicy() {
            controller.startNewGame();

            controller.activatePolicy("environmental");

            assertEquals("EnvironmentalTax", controller.getCurrentPolicyName(), "La Environmental Tax deve essere attivata correttamente");
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
        @Test
        void testDemolishBuilding_SuccessAndRefund() {
            controller.startNewGame();
            int initialMoney = controller.getMoney();

            // Costruiamo (spesa -500)
            controller.placeBuilding("residential", 5, 5);
            assertTrue(controller.getMoney() < initialMoney, "Il denaro deve scendere dopo la costruzione");

            // Demoliamo (rimborso +500)
            boolean demolished = controller.demolishBuilding(5, 5);

            // Assert
            assertTrue(demolished, "La demolizione deve avere successo su una cella occupata");
            assertEquals(initialMoney, controller.getMoney(), "Il budget deve tornare esattamente al valore iniziale dopo il rimborso");
        }

    }

