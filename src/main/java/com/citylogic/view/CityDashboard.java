package com.citylogic.view;

import com.citylogic.controller.GameController;
import com.citylogic.model.*;
import com.citylogic.simulation.CityObserver;
import com.citylogic.strategy.EnvironmentalTax;
import com.citylogic.strategy.IndustrialExpansion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

// Finestra principale del gioco. Si aggiorna da sola in automatico quando i dati cambiano.
public class CityDashboard extends JFrame implements CityObserver {

    private final GameController controller;
    private final CellFactory cellFactory;
    // Matrice che contiene i 400 bottoni fisici cliccabili della mappa
    private final JButton[][] gridButtons = new JButton[20][20];

    // UI COMPONENTS

    // Etichette dei contatori numerici in alto
    private JLabel moneyLabel, pollutionLabel, happinessLabel, populationLabel, energyLabel, tickLabel;
    // Memorizza cosa l'utente ha in mano e vuole costruire (es. "residential" o "road")
    private String selectedBuilding = null;

    private JButton noPolicyButton, environmentalButton, industrialButton;
    private JButton houseButton, factoryButton, commercialButton, parkButton, roadButton, powerPlantButton;

    // CONSTRUCTOR
    public CityDashboard(GameController controller) {
        this.controller = controller;
        this.cellFactory = new CellFactory();

        // Collega l'interfaccia ai dati: in questo modo la dashboard "ascolta" i cambiamenti
        controller.getCity().getCityState().addObserver(this);

        initializeWindow();
        createGUI();

        // Primo caricamento visivo della griglia all'avvio del gioco
        refreshGrid();
        refreshStats(controller.getCity().getCityState().getCityStats());
        updatePolicyButtons();
    }

    // Imposta dimensioni, posizione centrale e chiusura della finestra
    private void initializeWindow() {
        setTitle("SimCity Lite");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    // GUI ASSEMBLY

    // Divide lo schermo nelle varie zone: titolo, mappa e pannello laterale
    private void createGUI() {
        setLayout(new BorderLayout(10, 10));

        add(createTitlePanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createStatsPanel(), BorderLayout.NORTH);
        centerPanel.add(createGridPanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(createCommandsPanel(), BorderLayout.EAST);
    }

    // Disegna la scritta del titolo in alto
    private JPanel createTitlePanel() {
        JLabel titleLabel = new JLabel("SimCity Lite", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    // Disegna il riquadro con i valori di bilancio (soldi, inquinamento, ecc.)
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "CITY STATS",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

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

        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        statsContainer.add(statsPanel, BorderLayout.CENTER);
        return statsContainer;
    }

    // Disegna la griglia di gioco 20x20
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(20, 20));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                final int finalX = x;
                final int finalY = y;

                JButton button = new JButton();
                button.setMargin(new Insets(8, 0, 0, 0)); // Correzione offset baseline
                button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

                // Quando l'utente clicca un bottone, innesca la funzione di costruzione passandogli le coordinate x, y
                button.addActionListener(e -> buildOnCell(finalX, finalY));

                gridButtons[x][y] = button;
                gridPanel.add(button);
            }
        }
        return gridPanel;
    }

    private JPanel createCommandsPanel() {
        JPanel commandsPanel = new JPanel();
        commandsPanel.setLayout(new BoxLayout(commandsPanel, BoxLayout.Y_AXIS));
        commandsPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));

        // --- TICK BUTTON ---
        JButton tickButton = new JButton("Next Tick");
        tickButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tickButton.addActionListener(e -> {
            controller.advanceTime();
            int unpowered = controller.getCity().getCityState().getGrid().countUnpoweredResidential();
            if (unpowered > 0) {
                JOptionPane.showMessageDialog(this,
                        "Attention!\n" + unpowered + " residential zone(s) do not have a nearby Power Plant.\nThey did not contribute to the city's metrics during this tick.",
                        "Residential zones without power", JOptionPane.WARNING_MESSAGE);
            }
        });
        commandsPanel.add(tickButton);
        commandsPanel.add(Box.createVerticalStrut(15));

        // --- BUILD SECTION ---
        JPanel buildPanel = new JPanel();
        buildPanel.setLayout(new BoxLayout(buildPanel, BoxLayout.Y_AXIS));
        buildPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "BUILD",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        houseButton = new JButton("Build House");
        factoryButton = new JButton("Build Factory");
        commercialButton = new JButton("Build Shop");
        parkButton = new JButton("Build Park");
        roadButton = new JButton("Build Road");
        powerPlantButton = new JButton("Build Power Plant");

        // Logica Toggle per la selezione persistente
        java.awt.event.ActionListener selettoreStrumento = e -> {
            JButton bottoneCliccato = (JButton) e.getSource();
            String strumentoScelto = e.getActionCommand();

            if (strumentoScelto.equals(selectedBuilding)) {
                selectedBuilding = null;
            } else {
                selectedBuilding = strumentoScelto;
            }

            houseButton.setBackground(null);
            factoryButton.setBackground(null);
            commercialButton.setBackground(null);
            parkButton.setBackground(null);
            roadButton.setBackground(null);
            powerPlantButton.setBackground(null);

            if (selectedBuilding != null) {
                bottoneCliccato.setBackground(Color.LIGHT_GRAY);
            }
        };

        houseButton.setActionCommand("residential");
        factoryButton.setActionCommand("factory");
        commercialButton.setActionCommand("commercial");
        parkButton.setActionCommand("park");
        roadButton.setActionCommand("road");
        powerPlantButton.setActionCommand("powerplant");

        houseButton.addActionListener(selettoreStrumento);
        factoryButton.addActionListener(selettoreStrumento);
        commercialButton.addActionListener(selettoreStrumento);
        parkButton.addActionListener(selettoreStrumento);
        roadButton.addActionListener(selettoreStrumento);
        powerPlantButton.addActionListener(selettoreStrumento);

        buildPanel.add(houseButton);
        buildPanel.add(Box.createVerticalStrut(5));
        buildPanel.add(factoryButton);
        buildPanel.add(Box.createVerticalStrut(5));
        buildPanel.add(commercialButton);
        buildPanel.add(Box.createVerticalStrut(5));
        buildPanel.add(parkButton);
        buildPanel.add(Box.createVerticalStrut(5));
        buildPanel.add(roadButton);
        buildPanel.add(Box.createVerticalStrut(5));
        buildPanel.add(powerPlantButton);

        commandsPanel.add(buildPanel);
        commandsPanel.add(Box.createVerticalStrut(15));

        // --- POLICY SECTION ---
        JPanel policyPanel = new JPanel();
        policyPanel.setLayout(new BoxLayout(policyPanel, BoxLayout.Y_AXIS));
        policyPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "POLICY",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        noPolicyButton = new JButton("No Policy");
        environmentalButton = new JButton("Environmental Tax");
        industrialButton = new JButton("Industrial Expansion");

        noPolicyButton.addActionListener(e -> { controller.activatePolicy(null); updatePolicyButtons(); });
        environmentalButton.addActionListener(e -> { controller.activatePolicy(new EnvironmentalTax()); updatePolicyButtons(); });
        industrialButton.addActionListener(e -> { controller.activatePolicy(new IndustrialExpansion()); updatePolicyButtons(); });

        policyPanel.add(noPolicyButton);
        policyPanel.add(Box.createVerticalStrut(5));
        policyPanel.add(environmentalButton);
        policyPanel.add(Box.createVerticalStrut(5));
        policyPanel.add(industrialButton);

        commandsPanel.add(policyPanel);
        commandsPanel.add(Box.createVerticalStrut(15));

        // --- GAME SECTION ---
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "GAME",
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));

        JButton newGameButton = new JButton("New Game");
        JButton saveGameButton = new JButton("Save Game");
        JButton loadGameButton = new JButton("Load Game");

        newGameButton.addActionListener(e -> handleNewGame());
        saveGameButton.addActionListener(e -> handleSaveGame());
        loadGameButton.addActionListener(e -> handleLoadGame());

        gamePanel.add(newGameButton);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(saveGameButton);
        gamePanel.add(Box.createVerticalStrut(5));
        gamePanel.add(loadGameButton);

        commandsPanel.add(gamePanel);

        return commandsPanel;
    }

    // =========================================================
    // GAME LOGIC HANDLERS
    // =========================================================

    private void buildOnCell(int x, int y) {
        if (selectedBuilding == null) {
            JOptionPane.showMessageDialog(this, "Select a building first.");
            return;
        }

        Cell cell;
        try {
            cell = cellFactory.createCell(selectedBuilding);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Unknown building type.");
            return;
        }

        // Controllo budget PRIMA di chiamare il controller
        int currentMoney = controller.getCity().getCityState().getCityStats().getMoney();
        if (currentMoney < cell.getCost()) {
            JOptionPane.showMessageDialog(this,
                    "Fondi non sufficienti!\nCosto struttura: " + cell.getCost() + " $\nSaldo attuale: " + currentMoney + " $",
                    "Transazione Negata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cell.setX(x);
        cell.setY(y);

        boolean success = controller.setCell(cell);
        if (!success) {
            // Uso dei Text Block come richiesto dall'IDE
            JOptionPane.showMessageDialog(this, """
                    Cannot build here.
                    Possible reasons:
                    - cell already occupied
                    - invalid position
                    """);
            return;
        }
        refreshGrid();
    }

    private void handleNewGame() {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to start a new game?", "New Game", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;

        controller.startNewGame();
        controller.getCity().getCityState().addObserver(this);
        selectedBuilding = null;
        refreshGrid();
        refreshStats(controller.getCity().getCityState().getCityStats());
        updatePolicyButtons();
    }

    private void handleSaveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".json")) path += ".json";

        if (controller.saveGame(path)) {
            JOptionPane.showMessageDialog(this, "Game saved successfully.", "Save Game", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Unable to save the game.", "Save Game", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        if (!controller.loadGame(fileChooser.getSelectedFile().getAbsolutePath())) {
            JOptionPane.showMessageDialog(this, "Unable to load the selected game.", "Load Game", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.getCity().getCityState().addObserver(this);
        selectedBuilding = null;
        refreshGrid();
        refreshStats(controller.getCity().getCityState().getCityStats());
        updatePolicyButtons();
        JOptionPane.showMessageDialog(this, "Game loaded successfully.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    // UI UPDATERS
    // =========================================================

    private void updatePolicyButtons() {
        if (noPolicyButton == null || environmentalButton == null || industrialButton == null) return;
        Object currentPolicy = controller.getCity().getCityState().getCurrentPolicyStrategy();

        noPolicyButton.setText(currentPolicy == null ? "No Policy - ACTIVE" : "No Policy - INACTIVE");
        environmentalButton.setText(currentPolicy instanceof EnvironmentalTax ? "Environmental Tax - ACTIVE" : "Environmental Tax - INACTIVE");
        industrialButton.setText(currentPolicy instanceof IndustrialExpansion ? "Industrial Expansion - ACTIVE" : "Industrial Expansion - INACTIVE");
    }

    private void refreshGrid() {
        Grid grid = controller.getCity().getCityState().getGrid();
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                Cell cell = grid.getCell(x, y);
                JButton button = gridButtons[x][y];
                if (cell == null) button.setText("");
                else if (cell instanceof Residential) button.setText("🏠");
                else if (cell instanceof Factory) button.setText("🏭");
                else if (cell instanceof Commercial) button.setText("🏪");
                else if (cell instanceof Park) button.setText("🌲");
                else if (cell instanceof Road) button.setText("🛣️");
                else if (cell instanceof PowerPlant) button.setText("⚡");
            }
        }
    }

    private void refreshStats(Stats stats) {
        if (stats == null) return;
        moneyLabel.setText("Money: " + stats.getMoney());
        pollutionLabel.setText("Pollution: " + stats.getPollution());
        happinessLabel.setText("Happiness: " + stats.getHappiness());
        populationLabel.setText("Population: " + stats.getPopulation());
        energyLabel.setText("Energy: " + stats.getEnergy());
        tickLabel.setText("Tick: " + controller.getCity().getCityState().getCurrTick());
    }

    @Override
    public void update(Stats currentStats) {
        SwingUtilities.invokeLater(() -> {
            refreshStats(currentStats);
            refreshGrid();
            updatePolicyButtons();
        });
    }
}