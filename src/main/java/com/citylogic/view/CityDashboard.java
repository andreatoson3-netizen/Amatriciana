package com.citylogic.view;

import com.citylogic.controller.GameController;
import com.citylogic.model.Cell;
import com.citylogic.model.CellFactory;
import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.simulation.CityObserver;
import com.citylogic.strategy.EnvironmentalTax;
import com.citylogic.strategy.IndustrialExpansion;

import javax.swing.*;
import java.awt.*;

public class CityDashboard extends JFrame implements CityObserver {

    private final GameController controller;
    private final CellFactory cellFactory;

    private final JButton[][] gridButtons = new JButton[20][20];

    private JLabel moneyLabel;
    private JLabel pollutionLabel;
    private JLabel happinessLabel;
    private JLabel populationLabel;
    private JLabel energyLabel;
    private JLabel tickLabel;

    private String selectedBuilding = null;

    public CityDashboard(GameController controller) {

        this.controller = controller;
        this.cellFactory = new CellFactory();

        // La GUI si registra come Observer
        controller.getCity()
                .getCityState()
                .addObserver(this);

        initializeWindow();
        createGUI();

        refreshGrid();

        refreshStats(
                controller.getCity()
                        .getCityState()
                        .getCityStats()
        );
    }

    private void initializeWindow() {

        setTitle("CityLogic");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createGUI() {

        setLayout(new BorderLayout());

        // =========================
        // TOP: CITY STATUS
        // =========================

        JPanel statsPanel = new JPanel(new GridLayout(2, 3));

        moneyLabel = new JLabel("Money: 0");
        pollutionLabel = new JLabel("Pollution: 0");
        happinessLabel = new JLabel("Happiness: 0");
        populationLabel = new JLabel("Population: 0");
        energyLabel = new JLabel("Energy: 0");
        tickLabel = new JLabel("Tick: 0");

        statsPanel.add(moneyLabel);
        statsPanel.add(pollutionLabel);
        statsPanel.add(happinessLabel);
        statsPanel.add(populationLabel);
        statsPanel.add(energyLabel);
        statsPanel.add(tickLabel);

        add(statsPanel, BorderLayout.NORTH);


        // =========================
        // CENTER: CITY GRID
        // =========================

        JPanel gridPanel = new JPanel(
                new GridLayout(20, 20)
        );

        for (int x = 0; x < 20; x++) {

            for (int y = 0; y < 20; y++) {

                final int finalX = x;
                final int finalY = y;

                JButton button = new JButton();

                button.setMargin(
                        new Insets(0, 0, 0, 0)
                );

                button.addActionListener(e ->
                        buildOnCell(finalX, finalY)
                );

                gridButtons[x][y] = button;
                gridPanel.add(button);
            }
        }

        add(gridPanel, BorderLayout.CENTER);


        // =========================
        // RIGHT: COMMANDS
        // =========================

        JPanel commandsPanel = new JPanel();

        commandsPanel.setLayout(
                new BoxLayout(
                        commandsPanel,
                        BoxLayout.Y_AXIS
                )
        );


        // =========================
        // BUILDING BUTTONS
        // =========================

        JButton houseButton =
                new JButton("Build House");

        JButton factoryButton =
                new JButton("Build Factory");

        JButton commercialButton =
                new JButton("Build Shop");

        JButton parkButton =
                new JButton("Build Park");

        JButton roadButton =
                new JButton("Build Road");

        JButton powerPlantButton =
                new JButton("Build Power Plant");


        // =========================
        // POLICY BUTTONS
        // =========================

        JButton environmentalButton =
                new JButton("Environmental Tax");

        JButton industrialButton =
                new JButton("Industrial Expansion");


        // =========================
        // TIME BUTTON
        // =========================

        JButton tickButton =
                new JButton("Next Tick");


        // =========================
        // BUILDING ACTIONS
        // =========================

        houseButton.addActionListener(e ->
                selectedBuilding = "residential"
        );

        factoryButton.addActionListener(e ->
                selectedBuilding = "factory"
        );

        commercialButton.addActionListener(e ->
                selectedBuilding = "commercial"
        );

        parkButton.addActionListener(e ->
                selectedBuilding = "park"
        );

        roadButton.addActionListener(e ->
                selectedBuilding = "road"
        );

        powerPlantButton.addActionListener(e ->
                selectedBuilding = "powerplant"
        );


        // =========================
        // POLICY ACTIONS
        // =========================

        environmentalButton.addActionListener(e ->
                controller.activatePolicy(
                        new EnvironmentalTax()
                )
        );

        industrialButton.addActionListener(e ->
                controller.activatePolicy(
                        new IndustrialExpansion()
                )
        );


        // =========================
        // TIME ACTION
        // =========================

        tickButton.addActionListener(e ->
                controller.advanceTime()
        );


        // =========================
        // ADD BUILDING BUTTONS
        // =========================

        commandsPanel.add(houseButton);

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(factoryButton);

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(commercialButton);

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(parkButton);

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(roadButton);

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(powerPlantButton);


        // =========================
        // SEPARATOR
        // =========================

        commandsPanel.add(
                Box.createVerticalStrut(20)
        );


        // =========================
        // ADD POLICY BUTTONS
        // =========================

        commandsPanel.add(
                environmentalButton
        );

        commandsPanel.add(
                Box.createVerticalStrut(10)
        );

        commandsPanel.add(
                industrialButton
        );


        // =========================
        // SEPARATOR
        // =========================

        commandsPanel.add(
                Box.createVerticalStrut(20)
        );


        // =========================
        // NEXT TICK
        // =========================

        commandsPanel.add(tickButton);


        add(
                commandsPanel,
                BorderLayout.EAST
        );
    }


    // =========================
    // BUILD
    // =========================

    private void buildOnCell(int x, int y) {

        if (selectedBuilding == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a building first."
            );

            return;
        }

        Cell cell;

        try {

            cell =
                    cellFactory.createCell(
                            selectedBuilding
                    );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unknown building type."
            );

            return;
        }


        cell.setX(x);
        cell.setY(y);


        boolean success =
                controller.setCell(cell);


        if (!success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cannot build here.\n" +
                    "Possible reasons:\n" +
                    "- not enough money\n" +
                    "- cell already occupied\n" +
                    "- invalid position"
            );

            return;
        }


        refreshGrid();

        selectedBuilding = null;
    }


    // =========================
    // GRID UPDATE
    // =========================

    private void refreshGrid() {

        Grid grid =
                controller.getCity()
                        .getCityState()
                        .getGrid();


        for (int x = 0; x < 20; x++) {

            for (int y = 0; y < 20; y++) {

                Cell cell =
                        grid.getCell(x, y);

                JButton button =
                        gridButtons[x][y];


                if (cell == null) {

                    button.setText("");

                } else if (
                        cell instanceof com.citylogic.model.Residential
                ) {

                    button.setText("H");

                } else if (
                        cell instanceof com.citylogic.model.Factory
                ) {

                    button.setText("F");

                } else if (
                        cell instanceof com.citylogic.model.Commercial
                ) {

                    button.setText("C");

                } else if (
                        cell instanceof com.citylogic.model.Park
                ) {

                    button.setText("P");

                } else if (
                        cell instanceof com.citylogic.model.Road
                ) {

                    button.setText("R");

                } else if (
                        cell instanceof com.citylogic.model.PowerPlant
                ) {

                    button.setText("PP");
                }
            }
        }
    }


    // =========================
    // STATS UPDATE
    // =========================

    private void refreshStats(Stats stats) {

        if (stats == null) {
            return;
        }


        moneyLabel.setText(
                "Money: " +
                stats.getMoney()
        );


        pollutionLabel.setText(
                "Pollution: " +
                stats.getPollution()
        );


        happinessLabel.setText(
                "Happiness: " +
                stats.getHappiness()
        );


        populationLabel.setText(
                "Population: " +
                stats.getPopulation()
        );


        energyLabel.setText(
                "Energy: " +
                stats.getEnergy()
        );


        tickLabel.setText(
                "Tick: " +
                controller.getCity()
                        .getCityState()
                        .getCurrTick()
        );
    }


    // =========================
    // OBSERVER
    // =========================

    @Override
    public void update(Stats currentStats) {

        // La modifica arriva dal Model.
        // Aggiorniamo la GUI sull'Event Dispatch Thread.

        SwingUtilities.invokeLater(() -> {

            refreshStats(currentStats);
            refreshGrid();

        });
    }
}
