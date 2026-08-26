package com.citylogic.persistence;

import com.citylogic.model.City;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

//gestisce la persistenza dei dati (I/O) della città sui file JSON usando libreria Jackson

public class CityPersistenceManager {

    private final ObjectMapper objectMapper;

    public CityPersistenceManager(){
        this.objectMapper= new ObjectMapper();
        //abilita la formattazione indentata(ovvero organizzazione del testo con mandate a capo,spazi e rientri)
        // del JSON per renderlo leggibile su file
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    //salva lo stato corrente della città su un file JSON
    //@param city come oggetto City da salvare
    //@param filePath come percorso del file di destinazione
    public void saveCity(City city, String filePath){
        if(city==null || filePath==null){
            return;
        }
        try{
            objectMapper.writeValue(new File(filePath),city);
        } catch (IOException e){
            e.printStackTrace();//stampa errore nella console

        }
    }

    //carica lo stato della città da un file JSON esistente
    //@param filePath percorso del file da cui caricare i dati
    //@return un'istanza di City ricostruita, oppure null in caso di errore
    public City loadCity(String filePath){
        if(filePath==null){
            return null;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            //Jackson legge il file JSON E lo riconverte nell'oggetto City,grazie a @JsonTypeInfo
            return objectMapper.readValue(file, City.class);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}