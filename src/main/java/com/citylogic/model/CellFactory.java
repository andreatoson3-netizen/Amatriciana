package com.citylogic.model;

/**
 * Factory class per la creazione centralizzata delle celle (edifici e infrastrutture).
 */
public class CellFactory {

    //crea e restituisce un'istanza concreta di Cell in base al tipo specificato
     //@param cellType stringa che identifica il tipo di cella da creare
     //@return un oggetto che estende Cell, oppure null se il tipo non è riconosciuto

    public Cell createCell(String cellType) {
        if (cellType == null) {
            return null;
        }

        // Utilizziamo un blocco switch per restituire la classe concreta corrispondente
        //metodo trim serve ad eliminare gli spazi vuoti accidentali mentre scriviamo la stringa
        switch (cellType.toLowerCase().trim()){
            case "residential":
                return new Residential();
            case "factory":
                return new Factory();
            case "commercial":
                return new Commercial();
            case "powerplant":
                return new PowerPlant();
            case "road":
                return new Road();
            case "park":
                return new Park();
            default:
                throw new IllegalArgumentException("Tipo di cella sconosciuto: " + cellType);
        }
    }
}
