package com.citylogic.model;

// Factory che centralizza la creazione delle diverse tipologie di Cell
public class CellFactory {

    // Crea e restituisce un'istanza concreta di Cell in base al tipo specificato.
    // @param cellType stringa che identifica il tipo di cella da creare.
    // @return un oggetto concreto che estende Cell.
    // @throws IllegalArgumentException se il tipo non è riconosciuto.
    public Cell createCell(String cellType) {
        if (cellType == null) {
            return null;
        }

        // Utilizza uno switch per creare la classe concreta corrispondente al tipo richiesto.
        // toLowerCase() rende il confronto indipendente dalle maiuscole/minuscole,
        // mentre trim() rimuove eventuali spazi all'inizio e alla fine
        return switch (cellType.toLowerCase().trim()) {
            case "residential" -> new Residential();
            case "factory" -> new Factory();
            case "commercial" -> new Commercial();
            case "powerplant" -> new PowerPlant();
            case "road" -> new Road();
            case "park" -> new Park();
            default -> throw new IllegalArgumentException("Tipo di cella sconosciuto: " + cellType);
        };
    }
}
