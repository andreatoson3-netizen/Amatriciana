package com.citylogic.simulation;

import com.citylogic.model.Stats;

public interface CityObserver {
    void update(Stats currentStats);
}
