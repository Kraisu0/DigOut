package com.kraisu.digout.managers;

import com.kraisu.digout.survivor.Survivor;

import java.util.HashMap;
import java.util.Map;

public class SurvivorManager {
    private Map<String, Survivor> survivors;

    public SurvivorManager() {
        survivors = new HashMap<>();
    }

    public void addSurvivor(Survivor survivor) {
        survivors.put(survivor.getName(), survivor);
    }

    public Survivor getSurvivors(String name) {
        return survivors.get(name);
    }

}
