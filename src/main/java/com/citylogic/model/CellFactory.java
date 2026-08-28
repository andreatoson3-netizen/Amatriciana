package com.citylogic.model;

//factory class per la creazione centralizzata delle celle (edifici e infrastrutture).
public class CellFactory {

    //crea e restituisce un'istanza concreta di Cell in base al tipo specificato
     //@param cellType stringa che identifica il tipo di cella da creare
     //@return un oggetto che estende Cell, null, oppure un'eccezione se il tipo non è riconosciuto

    public Cell createCell(String cellType) {
        if (cellType == null) {
            return null;
        }

        // Utilizziamo un blocco switch per restituire la classe concreta corrispondente
        //metodo trim serve ad eliminare gli spazi vuoti accidentali mentre scriviamo la stringa
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
