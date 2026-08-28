package com.citylogic.strategy;

import com.citylogic.model.Stats;

//Interfaccia per il pattern Strategy, utilizzata per applicare
//diverse politiche di gestione alla città (es. tasse ambientali o espansione industriale).
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

    // Calcola e modifica le statistiche grezze in base alla politica attiva.
    //@param rawStats le statistiche di partenza (grezzo)
    //@return le nuove statistiche modificate dalla policy
    Stats calculateStats(Stats rawStats);
}
