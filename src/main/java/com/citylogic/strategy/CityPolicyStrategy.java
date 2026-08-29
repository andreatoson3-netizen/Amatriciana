package com.citylogic.strategy;

import com.citylogic.model.Stats;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Interfaccia per il pattern Strategy, utilizzata per applicare
// diverse politiche di gestione alla città (es. tasse ambientali o espansione industriale).
@JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class"
)
    
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnvironmentalTax.class),
        @JsonSubTypes.Type(value = IndustrialExpansion.class)
})
    
public interface CityPolicyStrategy {

    // Calcola le nuove statistiche della città applicando
    // le regole definite dalla politica attualmente attiva.
    // @param rawStats statistiche grezze calcolate dalla griglia
    // @return statistiche modificate dalla policy
    Stats calculateStats(Stats rawStats);
}
