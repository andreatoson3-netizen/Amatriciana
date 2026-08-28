package com.citylogic.model;

//Gestisce la mappa logica della città sotto forma di matrice bidimensionale(griglia)
public class Grid {

    //Matrice bidimensionale che rappresenta la di gioco(righe e colonne)
    private Cell[][] griglia;

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

    //calcola le statistiche grezze complessive della griglia
    //@return un oggetto Stats con i valori aggregati di tutti i blocchi occupati
    public Stats calculateRawStats(){
        Stats totalStats = new Stats(0,0,0,0,0);//inizializza a zero
        
        for (int x = 0; x < griglia.length; x++) {
            for (int y = 0; y < griglia[x].length; y++) {

                Cell currentBlock = griglia[x][y];

                if (currentBlock != null && !currentBlock.isFree()) {

                    // RULE ENFORCEMENT:
                    // una zona residenziale contribuisce alle metriche
                    // solo se c'è una PowerPlant nelle vicinanze
                    if (currentBlock instanceof Residential &&
                            !hasNearbyPowerPlant(x, y)) {
                        continue;
                    }

                    // aggiunge le statistiche del blocco corrente
                    totalStats.add(currentBlock.returnStat());
                }
            }
        }

        return totalStats;
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



    //getter e setter per consentire a Jackson la serializzazione della matrice
    public Cell[][] getGriglia(){
        return griglia;
    }

    public void setGriglia(Cell[][] griglia)
    {
        this.griglia=griglia;
    }
}

