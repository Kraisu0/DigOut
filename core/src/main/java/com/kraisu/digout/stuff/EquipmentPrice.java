package com.kraisu.digout.stuff;

import java.io.Serializable;

public class EquipmentPrice implements Serializable {
    int materials;
    int tools;
    int food;
    int electricity;
    int workingDays;
    //int survivorType;

    public EquipmentPrice(int materials, int tools, int food, int electricity, int workingDays) {
        this.materials = materials;
        this.tools = tools;
        this.food = food;
        this.electricity = electricity;
        this.workingDays = workingDays;
        //this.survivorType = survivorType;
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

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

//    public int getSurvivorType() {
//        return survivorType;
//    }
//
//    public void setSurvivorType(int survivorType) {
//        this.survivorType = survivorType;
//    }
}
