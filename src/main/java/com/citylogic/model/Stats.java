package com.citylogic.model;

// Rappresenta l'insieme delle metriche globali della simulazione
public class Stats {
    private int pollution;
    private int money;
    private int happiness;
    private int population;
    private int energy;

    // Costruttore senza parametri necessario per Jackson.
    // Permette di ricostruire l'oggetto durante il caricamento del file JSON
    public Stats(){
        this.pollution=0;
        this.money=0; //Budget iniziale di default per nuova partita
        this.happiness=0;
        this.population=0;
        this.energy=0;

    }

    // Costruttore con parametri per inizializzare tutte le metriche
    public Stats(int pollution,int money,int happiness,int population,int energy){
        this.pollution=pollution;
        this.money=money;
        this.happiness=happiness;
        this.population=population;
        this.energy=energy;
    }

    // Somma i valori di un altro oggetto Stats a quelli correnti.
    // Viene utilizzato per aggregare le statistiche dei blocchi presenti nella griglia.
    // @param other oggetto Stats da sommare
    public void add(Stats other){
        if(other!= null){
            this.pollution += other.pollution;
            this.money +=other.money;
            this.happiness += other.happiness;
            this.population += other.population;
            this.energy += other.energy;
        }
    }

    // Moltiplica tutte le metriche per un fattore scalare.
    // Viene utilizzato, ad esempio, per applicare modificatori alle statistiche previsti dalle policy.
    // I risultati vengono convertiti in interi.
    // @param factor fattore di moltiplicazione.
    // @return un nuovo oggetto Stats contenente i valori modificati
    public Stats multiply(double factor){
        return new Stats(
                (int) (this.pollution*factor),
                (int) (this.money*factor),
                (int) (this.happiness*factor),
                (int) (this.population*factor),
                (int) (this.energy*factor)
        );
    }

    // Getter e setter necessari per accedere alle metriche
    // e per la serializzazione/deserializzazione JSON tramite Jackson

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
