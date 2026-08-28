package com.citylogic.model;

import java.util.LinkedList;
import java.util.Queue;
import com.fasterxml.jackson.annotation.JsonIgnore; // gestione test blackout

//Gestisce la mappa logica della città sotto forma di matrice bidimensionale(griglia)
public class Grid {

    //Matrice bidimensionale che rappresenta la di gioco(righe e colonne)
    private Cell[][] griglia;

    // Coda per memorizzare le strutture che consumano più energia di quella disponibile
    @JsonIgnore
    private final Queue<Cell> blackoutQueue = new LinkedList<>();

    //costruttore vuoto per Jackson ed inizializzazione di default(griglia 20x20)
    public Grid(){
        this.griglia=new Cell[20][20];
    }


    //restituisce il blocco alla posizione (x,y)
    //@param x coordinata X
    //@param y coordinata Y
    public Cell getCell(int x, int y){
        if(x>=0 && x<griglia.length && y>=0 && y<griglia[0].length) {
            return griglia[x][y];
        }
        return null;
    }

    //metodo di supporto booleano per verificare la presenza di un PowerPlant
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

    private boolean hasNearbyPowerPlant(int x, int y) {

        for (int i = 0; i < griglia.length; i++) {
            for (int j = 0; j < griglia[i].length; j++) {

                if (griglia[i][j] instanceof PowerPlant) {

                    int distanceX = Math.abs(i - x);
                    int distanceY = Math.abs(j - y);

                    // Mantenuto il raggio a 8 impostato dalla tua compagna
                    if (distanceX + distanceY <= 8) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

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

    // Accende e spegne le celle in base alla disponibilità di corrente e gestisce la coda di blackout
    public void distributeEnergy() {
        int availableEnergy = 0;
        blackoutQueue.clear();

        // FASE 1: Raccolta energia totale dalle Centrali Elettriche
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

        // FASE 2: Distribuzione ai soli consumatori (Factory, Commercial, Residential)
        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell cell = griglia[x][y];

                if (cell != null && !cell.isFree()) {

                    Stats s = cell.returnStat();

                    // Se è un produttore (centrale), lo saltiamo (già gestito nella Fase 1)
                    if (s.getEnergy() > 0) continue;

                    // Regola spaziale per le case residenziali
                    if (cell instanceof Residential && !hasNearbyPowerPlant(x, y)) {
                        cell.setOperative(false);
                        continue;
                    }

                    // Se è un consumatore (energia < 0: Fabbriche, Negozi, Case collegate)
                    if (s.getEnergy() < 0) {

                        int requiredEnergy = Math.abs(s.getEnergy());

                        if (availableEnergy >= requiredEnergy) {
                            availableEnergy -= requiredEnergy; // Scala dal serbatoio
                            cell.setOperative(true);            // Accesa e funzionante
                        } else {
                            cell.setOperative(false);           // Ibernata
                            blackoutQueue.offer(cell);          // Aggiunta alla coda di attesa corrente
                        }
                    } else {
                        // Per tutto il resto (Strade, Parchi con energia = 0), restano invariate
                        cell.setOperative(true);
                    }
                }
            }
        }
    }

    //calcola le statistiche grezze complessive della griglia (Sola Lettura)
    //@return un oggetto Stats con i valori aggregati di tutti i blocchi occupati
    public Stats calculateRawStats(){
        Stats totalStats = new Stats(0,0,0,0,0);//inizializza a zero

        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell currentBlock = griglia[x][y];

                if (currentBlock != null && !currentBlock.isFree()) {

                    // Somma i valori SOLO se l'edificio è stato acceso da distributeEnergy()
                    if (currentBlock.isOperative()) {
                        totalStats.add(currentBlock.returnStat());
                    }
                }
            }
        }

        return totalStats;
    }


    // Rimuove la cella e la restituisce per poterne leggere il costo
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

    //posiziona una cella nella griglia verificandone i confini
    public boolean setCell(Cell cell,int x, int y){
        if(x>=0 && x<griglia.length && y>=0 && y<griglia[0].length){
            //verifica se la cella è già occupata o meno
            if(griglia[x][y]==null || griglia[x][y].isFree()) {
                griglia[x][y] = cell;
                //se necessario, aggiorna le coordinate interne alla cella
                cell.setX(x);
                cell.setY(y);
                return true;//posiziona cella
            }
        }

        return false;//posizione non valida oppure cella occupata
    }

    // Restituisce la coda delle celle attualmente spente per mancanza di corrente
    public Queue<Cell> getBlackoutQueue() {
        return blackoutQueue;
    }

    //getter e setter per consentire a Jackson la serializzazione della matrice
    public Cell[][] getGriglia(){
        return griglia;
    }

    public void setGriglia(Cell[][] griglia)
    {
        this.griglia=griglia;
    }
}