package com.kraisu.digout.stuff;

public class BuildingPrice {
    int materials;
    int tools;
    int food;
    int requiredNumberOfWorkers;
    int workingDays;
    boolean isElectricityRequired;

    public BuildingPrice(int materials, int tools, int food, int requiredNumberOfWorkers, int workingDays, boolean isElectricityRequired) {
        this.materials = materials;
        this.tools = tools;
        this.food = food;
        this.requiredNumberOfWorkers = requiredNumberOfWorkers;
        this.workingDays = workingDays;
        this.isElectricityRequired = isElectricityRequired;

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

    public int getRequiredNumberOfWorkers() {
        return requiredNumberOfWorkers;
    }

    public void setRequiredNumberOfWorkers(int requiredNumberOfWorkers) {
        this.requiredNumberOfWorkers = requiredNumberOfWorkers;
    }

    public int getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }

    public boolean isElectricityRequired() {
        return isElectricityRequired;
    }

    public void setElectricityRequired(boolean electricityRequired) {
        this.isElectricityRequired = electricityRequired;
    }

}
