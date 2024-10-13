package com.kraisu.digout.stuff;

public class Resource {
    private String name;
    private String description;
    private String iconPath;
    private int totalAmount;
    private int allocatedAmount;

    public Resource(String name, String description, String iconPath, int initialAmount) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.totalAmount = initialAmount;
        this.allocatedAmount = 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(int allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public void resetAllocatedAmount() {
        this.allocatedAmount = 0;
    }

    public boolean allocateResource(int amount) {
        if (amount <= totalAmount - allocatedAmount) {
            allocatedAmount += amount;
            return true;
        }
        return false;
    }

    public void consumeAllocatedResources() {
        totalAmount -= allocatedAmount;
        resetAllocatedAmount();
    }
}
