package com.kraisu.digout.stuff;

public class EquipmentPrice {
    int materials;
    int tools;
    int food;
    int workingDays;
    //int survivorType;

    public EquipmentPrice(int materials, int tools, int food, int workingDays) {
        this.materials = materials;
        this.tools = tools;
        this.food = food;
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
