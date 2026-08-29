package com.citylogic.persistence;

import com.citylogic.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

// Gestisce la persistenza della città tramite operazioni di lettura e scrittura
// su file JSON utilizzando la libreria Jackson

public class CityPersistenceManager {

    private final ObjectMapper objectMapper;

    public CityPersistenceManager(){
        this.objectMapper= new ObjectMapper();
        // Abilita la formattazione indentata del JSON
        // per rendere il file salvato più leggibile
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Salva lo stato corrente della città in un file JSON.
    // @param city oggetto City da salvare.
    // @param filePath percorso del file di destinazione.
    // @return true se il salvataggio è avvenuto correttamente,
    //         false in caso di errore o parametri non validi
    public boolean saveCity(City city, String filePath){
    if(city == null || filePath == null){
        return false;
    }

    try{
        objectMapper.writeValue(new File(filePath), city);
        return true;

    } catch (IOException e){
        e.printStackTrace();
        return false;
    }
    }

    // Carica lo stato della città da un file JSON esistente.
    // @param filePath percorso del file da cui caricare i dati.
    // @return un'istanza di City ricostruita da Jackson,
    //         oppure null in caso di errore o file inesistente
    public City loadCity(String filePath){
        if(filePath==null){
            return null;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            
            // Jackson legge il file JSON e ricostruisce l'oggetto City.
            // Le annotazioni @JsonTypeInfo permettono di riconoscere
            // correttamente le diverse sottoclassi di Cell
            return objectMapper.readValue(file, City.class);
            
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
