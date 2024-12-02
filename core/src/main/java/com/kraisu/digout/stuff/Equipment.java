package com.kraisu.digout.stuff;

import java.io.Serializable;

public class Equipment implements Serializable {
    private String name;
    private String description;
    private String iconPath;
    private int totalAmount;
    private int allocatedAmount;

    public Equipment(String name, String description, String iconPath, int totalAmount) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.totalAmount = totalAmount;
        this.allocatedAmount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
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

    public boolean allocateEquipment(int amount) {
        if (amount <= totalAmount - allocatedAmount) {
            allocatedAmount += amount;
            return true;
        }
        return false;
    }

    public void consumeAllocatedEquipment() {
        totalAmount -= allocatedAmount;
        resetAllocatedAmount();
    }

    public void increaseEquipment(int amount) {
        this.totalAmount = this.totalAmount + amount;
    }


}
