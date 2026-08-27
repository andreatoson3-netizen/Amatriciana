package com.citylogic;

import com.citylogic.controller.GameController;
import com.citylogic.view.CityDashboard;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Crea il controller
            GameController controller =
                    new GameController();

            // Crea e mostra la GUI
            CityDashboard dashboard =
                    new CityDashboard(controller);

            dashboard.setVisible(true);
        });
    }
}
