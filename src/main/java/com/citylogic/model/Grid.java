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
        for(int x=0;x<griglia.length;x++){
            for(int y=0;y<griglia[x].length;y++){
                if(griglia[x][y] instanceof PowerPlant){
                    return true;
                }
            }
        }
        return false;
    }

    //calcola le statistiche grezze complessive della griglia
    //@return un oggetto Stats con i valori aggregati di tutti i blocchi occupati
    public Stats calculateRawStats(){
        Stats totalStats = new Stats(0,0,0,0,0);//inizializza a zero

        //1)verifica preliminare:esiste almeno una centrale elettrica nella griglia?
        boolean powerPlantExists=hasPowerPlant();//metodo di supporto booleano della classe Grid


        for (int x=0;x<griglia.length;x++){
            for(int y=0;y<griglia[x].length;y++){
                Cell currentBlock = griglia[x][y];
                if(currentBlock != null && !currentBlock.isFree()){
                    //2)APPLICAZIONE DELLA RULE ENFORCEMENT:
                    //Se il blocco è una zona residenziale e NON c'è alcuna PowerPlant,
                    //saltiamo l'aggiunta delle sue statistiche(la zona non cresce)
                    if(currentBlock instanceof Residential && !powerPlantExists){
                        continue;
                    }
                    //3)aggiunge le statistiche del blocco corrente
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

