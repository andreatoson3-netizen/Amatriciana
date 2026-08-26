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
    //@param il blocco in quella posizione
    public Cell getCell(int x, int y){
        if(x>=0 && x<griglia.length && y>=0 && y<griglia[0].length) {
            return griglia[x][y];
        }
        return null;
    }

    //calcola le statistiche grezze complessive della griglia
    //@return un oggetto Stats con i valori aggregati di tutti i blocchi occupati
    public Stats calculateRawStats(){
        Stats totalStats = new Stats();

        for (int x=0;x<griglia.length;x++){
            for(int y=0;y<griglia[x].length;y++){
                Cell currentBlock = griglia[x][y];
                if(currentBlock != null && !currentBlock.isFree()){
                    totalStats.add(currentBlock.returnStat());
                }
            }
        }
        return totalStats;
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

