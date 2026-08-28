package com.citylogic.view;

import com.citylogic.controller.GameController;
import com.citylogic.model.Cell;
import com.citylogic.model.CellFactory;
import com.citylogic.model.Grid;
import com.citylogic.model.Stats;
import com.citylogic.model.Residential;
import com.citylogic.model.Factory;
import com.citylogic.model.Commercial;
import com.citylogic.model.Park;
import com.citylogic.model.Road;
import com.citylogic.model.PowerPlant;
import com.citylogic.simulation.CityObserver;
import com.citylogic.strategy.EnvironmentalTax;
import com.citylogic.strategy.IndustrialExpansion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

public class CityDashboard extends JFrame implements CityObserver {

    private final GameController controller;
    private final CellFactory cellFactory;

    private final JButton[][] gridButtons = new JButton[20][20];

    // =========================
    // CITY STATS
    // =========================

    private JLabel moneyLabel;
    private JLabel pollutionLabel;
    private JLabel happinessLabel;
    private JLabel populationLabel;
    private JLabel energyLabel;
    private JLabel tickLabel;

    // =========================
    // BUILDING
    // =========================

    private String selectedBuilding = null;

    // =========================
    // POLICY
    // =========================

    private JButton noPolicyButton;
    private JButton environmentalButton;
    private JButton industrialButton;

    // =========================
    // CONSTRUCTOR
    // =========================

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

        updatePolicyButtons();
    }

    // =========================
    // WINDOW
    // =========================

    private void initializeWindow() {

        setTitle("SimCity Lite");

        setSize(1200, 750);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
    }

    // =========================
    // GUI
    // =========================

    private void createGUI() {

        setLayout(new BorderLayout(10, 10));

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel("SimCity Lite", SwingConstants.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        JPanel titlePanel =
                new JPanel(new BorderLayout());

        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 5, 10
                )
        );

        titlePanel.add(
                titleLabel,
                BorderLayout.CENTER
        );

        add(
                titlePanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CITY STATS
        // =====================================================

        JPanel statsPanel =
                new JPanel(new GridLayout(2, 3, 10, 5));

        statsPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "CITY STATS",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14)
                )
        );

        moneyLabel =
                new JLabel("Money: 0");

        pollutionLabel =
                new JLabel("Pollution: 0");

        happinessLabel =
                new JLabel("Happiness: 0");

        populationLabel =
                new JLabel("Population: 0");

        energyLabel =
                new JLabel("Energy: 0");

        tickLabel =
                new JLabel("Tick: 0");

        statsPanel.add(moneyLabel);
        statsPanel.add(pollutionLabel);
        statsPanel.add(happinessLabel);
        statsPanel.add(populationLabel);
        statsPanel.add(energyLabel);
        statsPanel.add(tickLabel);

        JPanel statsContainer =
                new JPanel(new BorderLayout());

        statsContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 10, 5, 10
                )
        );

        statsContainer.add(
                statsPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // CENTER AREA
        // =====================================================

        JPanel centerPanel =
                new JPanel(new BorderLayout());

        centerPanel.add(
                statsContainer,
                BorderLayout.NORTH
        );

        // =====================================================
        // CITY GRID
        // =====================================================

        JPanel gridPanel =
                new JPanel(new GridLayout(20, 20));

        gridPanel.setBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY)
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
                        buildOnCell(
                                finalX,
                                finalY
                        )
                );

                gridButtons[x][y] = button;

                gridPanel.add(button);
            }
        }

        centerPanel.add(
                gridPanel,
                BorderLayout.CENTER
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // RIGHT COMMAND PANEL
        // =====================================================

        JPanel commandsPanel =
                new JPanel();

        commandsPanel.setLayout(
                new BoxLayout(
                        commandsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        commandsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 5, 10, 10
                )
        );

        // =====================================================
        // NEXT TICK
        // =====================================================

        JButton tickButton =
                new JButton("Next Tick");

        tickButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        tickButton.addActionListener(e -> {

            controller.advanceTime();

            int unpoweredResidential =
                    controller.getCity()
                            .getCityState()
                            .getGrid()
                            .countUnpoweredResidential();

            if (unpoweredResidential > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Attention!\n" +
                        unpoweredResidential +
                        " residential zone(s) do not have " +
                        "a nearby Power Plant.\n" +
                        "They did not contribute to the " +
                        "city's metrics during this tick.",
                        "Residential zones without power",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        commandsPanel.add(tickButton);

        // =====================================================
        // BUILD SECTION
        // =====================================================

        commandsPanel.add(
                Box.createVerticalStrut(15)
        );

        JPanel buildPanel =
                new JPanel();

        buildPanel.setLayout(
                new BoxLayout(
                        buildPanel,
                        BoxLayout.Y_AXIS
                )
        );

        buildPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "BUILD",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14)
                )
        );

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

        // Building actions

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

        buildPanel.add(houseButton);

        buildPanel.add(
                Box.createVerticalStrut(5)
        );

        buildPanel.add(factoryButton);

        buildPanel.add(
                Box.createVerticalStrut(5)
        );

        buildPanel.add(commercialButton);

        buildPanel.add(
                Box.createVerticalStrut(5)
        );

        buildPanel.add(parkButton);

        buildPanel.add(
                Box.createVerticalStrut(5)
        );

        buildPanel.add(roadButton);

        buildPanel.add(
                Box.createVerticalStrut(5)
        );

        buildPanel.add(powerPlantButton);

        commandsPanel.add(buildPanel);

        // =====================================================
        // POLICY SECTION
        // =====================================================

        commandsPanel.add(
                Box.createVerticalStrut(15)
        );

        JPanel policyPanel =
                new JPanel();

        policyPanel.setLayout(
                new BoxLayout(
                        policyPanel,
                        BoxLayout.Y_AXIS
                )
        );

        policyPanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "POLICY",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14)
                )
        );

        noPolicyButton =
                new JButton("No Policy");

        environmentalButton =
                new JButton("Environmental Tax");

        industrialButton =
                new JButton("Industrial Expansion");

        // Policy actions

        noPolicyButton.addActionListener(e -> {

            controller.activatePolicy(null);

            updatePolicyButtons();
        });

        environmentalButton.addActionListener(e -> {

            controller.activatePolicy(
                    new EnvironmentalTax()
            );

            updatePolicyButtons();
        });

        industrialButton.addActionListener(e -> {

            controller.activatePolicy(
                    new IndustrialExpansion()
            );

            updatePolicyButtons();
        });

        policyPanel.add(noPolicyButton);

        policyPanel.add(
                Box.createVerticalStrut(5)
        );

        policyPanel.add(environmentalButton);

        policyPanel.add(
                Box.createVerticalStrut(5)
        );

        policyPanel.add(industrialButton);

        commandsPanel.add(policyPanel);

        // =====================================================
        // GAME SECTION
        // =====================================================

        commandsPanel.add(
                Box.createVerticalStrut(15)
        );

        JPanel gamePanel =
                new JPanel();

        gamePanel.setLayout(
                new BoxLayout(
                        gamePanel,
                        BoxLayout.Y_AXIS
                )
        );

        gamePanel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "GAME",
                        TitledBorder.CENTER,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 14)
                )
        );

        JButton newGameButton =
                new JButton("New Game");

        JButton saveGameButton =
                new JButton("Save Game");

        JButton loadGameButton =
                new JButton("Load Game");

        // =====================================================
        // NEW GAME
        // =====================================================

        newGameButton.addActionListener(e -> {

            int answer =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to start a new game?",
                            "New Game",
                            JOptionPane.YES_NO_OPTION
                    );

            if (answer != JOptionPane.YES_OPTION) {
                return;
            }

            controller.startNewGame();

            controller.getCity()
                    .getCityState()
                    .addObserver(this);

            selectedBuilding = null;

            refreshGrid();

            refreshStats(
                    controller.getCity()
                            .getCityState()
                            .getCityStats()
            );

            updatePolicyButtons();
        });

        // =====================================================
        // SAVE GAME
        // =====================================================

        saveGameButton.addActionListener(e -> {

    JFileChooser fileChooser =
            new JFileChooser();

    fileChooser.setDialogTitle(
            "Save Game"
    );

    int result =
            fileChooser.showSaveDialog(this);

    if (result != JFileChooser.APPROVE_OPTION) {
        return;
    }

    File file =
            fileChooser.getSelectedFile();

    String path =
            file.getAbsolutePath();

    if (!path.toLowerCase().endsWith(".json")) {
        path += ".json";
    }

    boolean saved =
            controller.saveGame(path);

    if (saved) {

        JOptionPane.showMessageDialog(
                this,
                "Game saved successfully.",
                "Save Game",
                JOptionPane.INFORMATION_MESSAGE
        );

    } else {

        JOptionPane.showMessageDialog(
                this,
                "Unable to save the game.",
                "Save Game",
                JOptionPane.ERROR_MESSAGE
        );
    }
});

        // =====================================================
        // LOAD GAME
        // =====================================================

        loadGameButton.addActionListener(e -> {

    JFileChooser fileChooser =
            new JFileChooser();

    fileChooser.setDialogTitle(
            "Load Game"
    );

    int result =
            fileChooser.showOpenDialog(this);

    if (result != JFileChooser.APPROVE_OPTION) {
        return;
    }

    File file =
            fileChooser.getSelectedFile();

    // Prova a caricare la partita
    boolean loaded =
            controller.loadGame(
                    file.getAbsolutePath()
            );

    // Se il caricamento fallisce,
    // la città precedente rimane invariata
    if (!loaded) {

        JOptionPane.showMessageDialog(
                this,
                "Unable to load the selected game.",
                "Load Game",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // La nuova città caricata diventa osservata dalla GUI
    controller.getCity()
            .getCityState()
            .addObserver(this);

    selectedBuilding = null;

    refreshGrid();

    refreshStats(
            controller.getCity()
                    .getCityState()
                    .getCityStats()
    );

    updatePolicyButtons();

    JOptionPane.showMessageDialog(
            this,
            "Game loaded successfully.",
            "Load Game",
            JOptionPane.INFORMATION_MESSAGE
    );
});

        // Add game buttons

        gamePanel.add(newGameButton);

        gamePanel.add(
                Box.createVerticalStrut(5)
        );

        gamePanel.add(saveGameButton);

        gamePanel.add(
                Box.createVerticalStrut(5)
        );

        gamePanel.add(loadGameButton);

        commandsPanel.add(gamePanel);

        add(
                commandsPanel,
                BorderLayout.EAST
        );
    }

    // =========================================================
    // POLICY BUTTON UPDATE
    // =========================================================

    private void updatePolicyButtons() {

        if (noPolicyButton == null ||
                environmentalButton == null ||
                industrialButton == null) {
            return;
        }

        Object currentPolicy =
                controller.getCity()
                        .getCityState()
                        .getCurrentPolicyStrategy();

        // =====================================================
        // NO POLICY
        // =====================================================

        if (currentPolicy == null) {

            noPolicyButton.setText(
                    "No Policy - ACTIVE"
            );

            environmentalButton.setText(
                    "Environmental Tax - INACTIVE"
            );

            industrialButton.setText(
                    "Industrial Expansion - INACTIVE"
            );

            return;
        }

        // =====================================================
        // ENVIRONMENTAL TAX
        // =====================================================

        if (currentPolicy instanceof EnvironmentalTax) {

            noPolicyButton.setText(
                    "No Policy - INACTIVE"
            );

            environmentalButton.setText(
                    "Environmental Tax - ACTIVE"
            );

            industrialButton.setText(
                    "Industrial Expansion - INACTIVE"
            );

            return;
        }

        // =====================================================
        // INDUSTRIAL EXPANSION
        // =====================================================

        if (currentPolicy instanceof IndustrialExpansion) {

            noPolicyButton.setText(
                    "No Policy - INACTIVE"
            );

            environmentalButton.setText(
                    "Environmental Tax - INACTIVE"
            );

            industrialButton.setText(
                    "Industrial Expansion - ACTIVE"
            );
        }
    }

    // =========================================================
    // BUILD
    // =========================================================

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

    // =========================================================
    // GRID UPDATE
    // =========================================================

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
                        cell instanceof Residential
                ) {

                    button.setText("H");

                } else if (
                        cell instanceof Factory
                ) {

                    button.setText("F");

                } else if (
                        cell instanceof Commercial
                ) {

                    button.setText("S");

                } else if (
                        cell instanceof Park
                ) {

                    button.setText("P");

                } else if (
                        cell instanceof Road
                ) {

                    button.setText("R");

                } else if (
                        cell instanceof PowerPlant
                ) {

                    button.setText("PP");
                }
            }
        }
    }

    // =========================================================
    // STATS UPDATE
    // =========================================================

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

    // =========================================================
    // OBSERVER
    // =========================================================

    @Override
    public void update(Stats currentStats) {

        SwingUtilities.invokeLater(() -> {

            refreshStats(currentStats);

            refreshGrid();

            updatePolicyButtons();
        });
    }
}
