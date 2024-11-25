package com.kraisu.digout.managers;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.stuff.Resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EquipmentManager {
    private Map<Constants.Equipment, Equipment> equipmentStuff;

    public EquipmentManager() {
        this.equipmentStuff = new LinkedHashMap<>();
        initializeEquipment();
    }

    private void initializeEquipment() {
        equipmentStuff.put(Constants.Equipment.SEARCHLIGHT, new Equipment("Searchlight", "Thanks to this item, Worker can find twice as many resources", "assets/equipment/SEARCHLIGHT_icon_64.png", 0 ));
        equipmentStuff.put(Constants.Equipment.KITCHEN_ROBOT, new Equipment("Kitchen Robot", "This robot can work for two in the kitchen with the Cook", "assets/equipment/KITCHEN_ROBOT_icon_64.png", 0 ));
        equipmentStuff.put(Constants.Equipment.OXYGEN_MASK, new Equipment("Oxygen mask", "Thanks to this item, the Survivor does not experience oxygen loss", "assets/equipment/OXYGEN_MASK_icon_64.png", 0 ));
        equipmentStuff.put(Constants.Equipment.PICKAXE, new Equipment("Pickaxe", "This Worker item creates a Miner, allowing it to mine hard rocks", "assets/equipment/PICKAXE_icon_64.png", 0 ));

    }

    public Equipment getEquipment(Constants.Equipment equipment) {
        return equipmentStuff.get(equipment);
    }

    public boolean allocateEquipment(String equipmentName, int amount) {
        Equipment equipment = equipmentStuff.get(equipmentName);
        return equipment != null && equipment.allocateEquipment(amount);
    }

    public void consumeAllocatedEquipment() {
        for (Equipment equipment : equipmentStuff.values()) {
            equipment.consumeAllocatedEquipment();
        }
    }

    public void resetAllocatedEquipment() {
        for (Equipment equipment : equipmentStuff.values()) {
            equipment.resetAllocatedAmount();
        }
    }

    public LinkedHashMap<Constants.Equipment, Integer> getEquipmentStatus() {
        LinkedHashMap<Constants.Equipment, Integer> status = new LinkedHashMap<>();
        for (Map.Entry<Constants.Equipment, Equipment> entry : equipmentStuff.entrySet()) {
            status.put(entry.getKey(), entry.getValue().getTotalAmount());
        }
        return status;
    }

    public LinkedHashMap<Constants.Equipment, Integer> getAllocatedEquipmentStatus() {
        LinkedHashMap<Constants.Equipment, Integer> allocatedStatus = new LinkedHashMap<>();
        for (Map.Entry<Constants.Equipment, Equipment> entry : equipmentStuff.entrySet()) {
            allocatedStatus.put(entry.getKey(), entry.getValue().getAllocatedAmount());
        }
        return allocatedStatus;
    }

    public LinkedHashMap<Constants.Equipment, String> getEquipmentNames() {
        LinkedHashMap<Constants.Equipment, String> names = new LinkedHashMap<>();
        for (Map.Entry<Constants.Equipment, Equipment> entry : equipmentStuff.entrySet()) {
            names.put(entry.getKey(), entry.getValue().getName());
        }
        return names;
    }

    public LinkedHashMap<Constants.Equipment, String> getEquipmentDescription() {
        LinkedHashMap<Constants.Equipment, String> description = new LinkedHashMap<>();
        for (Map.Entry<Constants.Equipment, Equipment> entry : equipmentStuff.entrySet()) {
            description.put(entry.getKey(), entry.getValue().getDescription());
        }
        return description;
    }

}
