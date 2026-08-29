package com.citylogic.model;

import java.util.LinkedList;
import java.util.Queue;
import com.fasterxml.jackson.annotation.JsonIgnore; // gestione test blackout

//Gestisce la mappa logica della città attraverso una matrice bidimensionale (griglia)
public class Grid {

    //Matrice bidimensionale che rappresenta la di gioco (righe e colonne)
    private Cell[][] griglia;

    // Coda utilizzata per memorizzare le strutture che non possono essere
    // alimentate a causa della mancanza di energia disponibile
    @JsonIgnore
    private final Queue<Cell> blackoutQueue = new LinkedList<>();

    // Costruttore vuoto utilizzato da Jackson e per inizializzare una griglia di dimensione 20x20
    public Grid(){
        this.griglia=new Cell[20][20];
    }

    // Restituisce la cella presente nella posizione (x,y).
    // @param x coordinata x della cella.
    // @param y coordinata y della cella.
    // @return la cella nella posizione indicata, oppure null se le coordinate non sono valide
    public Cell getCell(int x, int y){
        if(x>=0 && x<griglia.length && y>=0 && y<griglia[0].length) {
            return griglia[x][y];
        }
        return null;
    }

    // Verifica se nella griglia è presente almeno un PowerPlant.
    // @return true se è presente una PowerPlant, false altrimenti
    public boolean hasPowerPlant(){
        for (Cell[] cells : griglia) {
            for (Cell cell : cells) {
                if (cell instanceof PowerPlant) {
                    return true;
                }
            }
        }
        return false;
    }

    // Verifica se esiste una PowerPlant sufficientemente vicina alla posizione indicata.
    // Viene utilizzato per determinare se una Residential può essere alimentata
    private boolean hasNearbyPowerPlant(int x, int y) {

        for (int i = 0; i < griglia.length; i++) {
            for (int j = 0; j < griglia[i].length; j++) {

                if (griglia[i][j] instanceof PowerPlant) {

                    int distanceX = Math.abs(i - x);
                    int distanceY = Math.abs(j - y);

                    // La PowerPlant alimenta la cella se la distanza di Manhattan è al massimo 8
                    if (distanceX + distanceY <= 8) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Conta il numero di edifici Residential che non hanno una PowerPlant nel raggio richiesto.
    // @return numero di Residential non alimentate.
    public int countUnpoweredResidential() {
        int count = 0;

        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell currentBlock = griglia[x][y];

                if (currentBlock instanceof Residential &&
                        !hasNearbyPowerPlant(x, y)) {

                    count++;
                }
            }
        }

        return count;
    }

    // Restituisce il numero di strutture presenti nella coda di blackout
    @JsonIgnore
    public int getBlackoutCount() {
        return blackoutQueue.size();
    }

    // Distribuisce l'energia disponibile tra le strutture che ne hanno bisogno
    // e gestisce le strutture che rimangono senza energia
    public void distributeEnergy() {
        int availableEnergy = 0;
        blackoutQueue.clear();

        // FASE 1: calcola l'energia totale prodotta dalle PowerPlant
        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell cell = griglia[x][y];

                if (cell != null && !cell.isFree()) {

                    Stats s = cell.returnStat();

                    if (s.getEnergy() > 0) {
                        availableEnergy += s.getEnergy();
                        cell.setOperative(true); // Le centrali sono sempre operative
                    }
                }
            }
        }

        // FASE 2: assegna l'energia disponibile alle strutture che la consumano
        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell cell = griglia[x][y];

                if (cell != null && !cell.isFree()) {

                    Stats s = cell.returnStat();

                    // Le strutture che producono energia sono già state gestite
                    if (s.getEnergy() > 0) continue;

                    // Una Residential deve avere una PowerPlant nel raggio richiesto
                    if (cell instanceof Residential && !hasNearbyPowerPlant(x, y)) {
                        cell.setOperative(false);
                        continue;
                    }

                    // Gestisce le strutture che consumano energia
                    if (s.getEnergy() < 0) {

                        int requiredEnergy = Math.abs(s.getEnergy());

                        if (availableEnergy >= requiredEnergy) {
                            availableEnergy -= requiredEnergy;  // Scala dal serbatoio
                            cell.setOperative(true);            // Accesa e funzionante
                        } else {
                            cell.setOperative(false);           // Ibernata
                            blackoutQueue.offer(cell);          // Aggiunta alla coda di attesa corrente
                        }
                    } else {
                        // Le strutture che non producono né consumano energia rimangono operative
                        cell.setOperative(true);
                    }
                }
            }
        }
    }

    // Calcola le statistiche complessive della griglia considerando
    // solamente le strutture occupate e operative.
    // @return un oggetto Stats contenente le metriche aggregate della città
    public Stats calculateRawStats(){
        Stats totalStats = new Stats(0,0,0,0,0); //Inizializza a zero

        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell currentBlock = griglia[x][y];

                if (currentBlock != null && !currentBlock.isFree()) {

                    // Le statistiche vengono sommate solo se la struttura è attualmente operativa
                    if (currentBlock.isOperative()) {
                        totalStats.add(currentBlock.returnStat());
                    }
                }
            }
        }

        return totalStats;
    }


    // Rimuove la cella dalla posizione indicata e la restituisce.
    // @return la cella rimossa, oppure null se la posizione è vuota o non valida
    public Cell removeCell(int x, int y) {
        if (x >= 0 && x < griglia.length && y >= 0 && y < griglia[0].length) {
            
            Cell cell = griglia[x][y];
            
            if (cell != null && !cell.isFree()) {
                griglia[x][y] = null; // Svuota la casella
                return cell; // Restituisce l'oggetto per permettere il rimborso
            }
        }
        
        return null;
    }

    // Inserisce una cella nella posizione indicata se le coordinate sono valide
    // e la posizione è libera.
    // @return true se la cella è stata inserita, false altrimenti
    public boolean setCell(Cell cell,int x, int y){
        if(x>=0 && x<griglia.length && y>=0 && y<griglia[0].length){
            
            //verifica se la cella è già occupata o meno
            if(griglia[x][y]==null || griglia[x][y].isFree()) {
                griglia[x][y] = cell;
                
                // Aggiorna le coordinate memorizzate nella cella
                cell.setX(x);
                cell.setY(y);
                return true; //Posiziona cella
            }
        }

        return false; //Posizione non valida oppure cella occupata
    }

    // Restituisce la coda delle celle attualmente spente per mancanza di energia
    public Queue<Cell> getBlackoutQueue() {
        return blackoutQueue;
    }

    // Getter e setter utilizzati anche da Jackson per la serializzazione
    // e deserializzazione della matrice
    public Cell[][] getGriglia(){
        return griglia;
    }

    public void setGriglia(Cell[][] griglia)
    {
        this.griglia=griglia;
    }
}
