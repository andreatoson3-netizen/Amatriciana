package com.citylogic.model;
//Rappresenta l'insieme delle metriche globali e loali della simulazione

public class Stats {
    private int pollution;
    private int money;
    private int happiness;
    private int population;
    private int energy;

    //costruttore di valori di default, fondamentale per Jackson
    // perchè permette di ricostruire l'oggetto durante il caricamento del file JSON
    public Stats(){
        this.pollution=0;
        this.money=0;//budget iniziale di default per nuova partita
        this.happiness=0;
        this.population=0;
        this.energy=0;

    }

    //costruttore con parametri per inizializzare tutte le metriche
    public Stats(int pollution,int money,int happiness,int population,int energy){
        this.pollution=pollution;
        this.money=money;
        this.happiness=happiness;
        this.population=population;
        this.energy=energy;
    }

    //somma i valori di un altro oggetto Stats a quelli correnti.
    // Utile per accumulare le statstiche di tutti i blocchi presenti sulla griglia
    //@param other L'oggetto Stats da sommare
    public void add(Stats other){
        if(other!= null){
            this.pollution += other.pollution;
            this.money +=other.money;
            this.happiness += other.happiness;
            this.population += other.population;
            this.energy += other.energy;
        }
    }

    //moltiplica tutte le metriche per un fattore scalare (es. per calcoli percentuali delle policy)
    //restituisce un nuovo oggetto Stats modificato(avviene cast ad int nelle moltiplicazioni)

    public Stats multiply(double factor){
        return new Stats(
                (int) (this.pollution*factor),
                (int) (this.money*factor),
                (int) (this.happiness*factor),
                (int) (this.population*factor),
                (int) (this.energy*factor)
        );
    }

    //getter e setter necessae per l'accesso ai campi e alla serializzazione JSON

    public int getPollution() { return pollution; }
    public void setPollution(int pollution) { this.pollution = pollution; }

    public int getMoney() { return money; }
    public void setMoney(int money) { this.money = money; }

    public int getHappiness() { return happiness; }
    public void setHappiness(int happiness) { this.happiness = happiness; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }


}
