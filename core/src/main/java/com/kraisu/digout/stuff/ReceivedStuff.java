package com.kraisu.digout.stuff;

import com.kraisu.digout.help.Constants;

public class ReceivedStuff {
    int materials;
    int tools;
    int food;
    int electricity;
    int energyForSurvivor;
    int searchlight;
    int oxygenMask;
    int kitchenRobot;
    int pickaxe;
    Constants.Survivors survivor;

    public ReceivedStuff(int materials, int tools, int food, int electricity, int energyForSurvivor, int searchlight,
                         int oxygenMask, int kitchenRobot, int pickaxe, Constants.Survivors survivor) {
        this.materials = materials;
        this.tools = tools;
        this.food = food;
        this.electricity = electricity;
        this.energyForSurvivor = energyForSurvivor;
        this.searchlight = searchlight;
        this.oxygenMask = oxygenMask;
        this.kitchenRobot = kitchenRobot;
        this.pickaxe = pickaxe;
        this.survivor = survivor;
    }

    public int getMaterials() {
        return materials;
    }

    public void setMaterials(int materials) {
        this.materials = materials;
    }

    public int getTools() {
        return tools;
    }

    public void setTools(int tools) {
        this.tools = tools;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getEnergyForSurvivor() {
        return energyForSurvivor;
    }

    public void setEnergyForSurvivor(int energyForSurvivor) {
        this.energyForSurvivor = energyForSurvivor;
    }

    public int getSearchlight() {
        return searchlight;
    }

    public void setSearchlight(int searchlight) {
        this.searchlight = searchlight;
    }

    public int getOxygenMask() {
        return oxygenMask;
    }

    public void setOxygenMask(int oxygenMask) {
        this.oxygenMask = oxygenMask;
    }

    public int getKitchenRobot() {
        return kitchenRobot;
    }

    public void setKitchenRobot(int kitchenRobot) {
        this.kitchenRobot = kitchenRobot;
    }

    public int getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(int pickaxe) {
        this.pickaxe = pickaxe;
    }

    public Constants.Survivors getSurvivor() {
        return survivor;
    }

    public void setSurvivor(Constants.Survivors survivor) {
        this.survivor = survivor;
    }

}
