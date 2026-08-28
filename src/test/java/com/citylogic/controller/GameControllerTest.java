
    package com.citylogic.controller;

import com.citylogic.model.Residential;
import com.citylogic.model.Factory;
import com.citylogic.strategy.EnvironmentalTax;
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
            int money = controller.getCity().getCityState().getCityStats().getMoney();
            assertEquals(5000, money, "Il nuovo gioco deve partire con un budget di 5000");
        }

        @Test
        void testSetCell_SuccessAndBudgetDeduction() {
            controller.startNewGame();
            Residential house = new Residential();
            house.setX(2);
            house.setY(2);
            // Costo della Residential di default è 500

            boolean success = controller.setCell(house);
            assertTrue(success, "Il piazzamento della casa deve avere successo con budget pieno");

            int remainingMoney = controller.getCity().getCityState().getCityStats().getMoney();
            assertEquals(4500, remainingMoney, "Il budget deve essere decrementato del costo dell'edificio (500)");
        }

        @Test
        void testSetCell_InsufficientFunds() {
            controller.startNewGame();

            // Creiamo un edificio molto costoso (o prosciughiamo il budget)
            Factory expensiveFactory = new Factory(); // Costo 1500
            expensiveFactory.setX(0);// Supponendo metodi di posizionamento
            expensiveFactory.setY(0);

            // Simuliamo fondi insufficienti forzando il budget a 100
            controller.getCity().getCityState().getCityStats().setMoney(100);

            boolean success = controller.setCell(expensiveFactory);
            assertFalse(success, "Il piazzamento deve fallire se i fondi non sono sufficienti");
            assertEquals(100, controller.getCity().getCityState().getCityStats().getMoney(), "Il budget non deve essere modificato in caso di fallimento");
        }

        @Test
        void testActivatePolicy() {
            controller.startNewGame();
            EnvironmentalTax policy = new EnvironmentalTax();

            controller.activatePolicy(policy);
            assertNotNull(controller.getCity().getCityState().getCurrentPolicyStrategy(), "La policy deve essere attivata correttamente");
        }

        @Test
        void testSaveAndLoadGame() {
            controller.startNewGame();
            String testFile = "controller_save_test.json";

            // Modifichiamo lo stato (es. spendiamo soldi)
            Residential house = new Residential();
            house.setX(1);
            house.setY(1);
            controller.setCell(house);
            int moneyBeforeSave = controller.getCity().getCityState().getCityStats().getMoney();

            // Salvataggio
            boolean saved = controller.saveGame(testFile);
            assertTrue(saved, "Il salvataggio tramite controller deve riuscire");

            // Avviamo un nuovo gioco (resetta i soldi a 5000)
            controller.startNewGame();
            assertEquals(5000, controller.getCity().getCityState().getCityStats().getMoney());

            // Caricamento
            boolean loaded = controller.loadGame(testFile);
            assertTrue(loaded, "Il caricamento tramite controller deve riuscire");

            int moneyAfterLoad = controller.getCity().getCityState().getCityStats().getMoney();
            assertEquals(moneyBeforeSave, moneyAfterLoad, "Il budget ricaricato deve corrispondere allo stato salvato");

            // Pulizia file di test
            File f = new File(testFile);
            if (f.exists()) {
                f.delete();
            }
        }
    }

