package com.kraisu.digout.managers;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResourceManager {
    private Map<Constants.Resources, Resource> resources;

    public ResourceManager() {
        this.resources = new LinkedHashMap<>();
        initializeResources();
    }

    private void initializeResources() {
        resources.put(Constants.Resources.CONSTRUCTION_RESOURCES, new Resource("Construction Resources", "Materials for building", "assets/resources/CONSTRUCTION_RESOURCES_icon_64.png", 0));
        resources.put(Constants.Resources.FOOD, new Resource("Food Supplies", "Food for survivors", "assets/resources/FOODS_icon_64.png", 0));
        resources.put(Constants.Resources.TOOLS, new Resource("Tools", "Tools for workers and for building", "assets/resources/TOOLS_icon_64.png", 0));
        resources.put(Constants.Resources.ELECTRICITY, new Resource("Electricity", "Power for power supply", "assets/resources/ELECTRICITY_icon_64.png", 0));
    }

    public Resource getResource(Constants.Resources resourceName) {
        return resources.get(resourceName);
    }

    public boolean allocateResource(String resourceName, int amount) {
        Resource resource = resources.get(resourceName);
        return resource != null && resource.allocateResource(amount);
    }

    public void consumeAllocatedResources() {
        for (Resource resource : resources.values()) {
            resource.consumeAllocatedResources();
        }
    }

    public void resetAllocatedResources() {
        for (Resource resource : resources.values()) {
            resource.resetAllocatedAmount();
        }
    }

    public LinkedHashMap<Constants.Resources, Integer> getResourceStatus() {
        LinkedHashMap<Constants.Resources, Integer> status = new LinkedHashMap<>();
        for (Map.Entry<Constants.Resources, Resource> entry : resources.entrySet()) {
            status.put(entry.getKey(), entry.getValue().getTotalAmount());
        }
        return status;
    }

    public LinkedHashMap<Constants.Resources, Integer> getAllocatedResourcesStatus() {
        LinkedHashMap<Constants.Resources, Integer> allocatedStatus = new LinkedHashMap<>();
        for (Map.Entry<Constants.Resources, Resource> entry : resources.entrySet()) {
            allocatedStatus.put(entry.getKey(), entry.getValue().getAllocatedAmount());
        }
        return allocatedStatus;
    }

    public LinkedHashMap<Constants.Resources, String> getResourceNames() {
        LinkedHashMap<Constants.Resources, String> names = new LinkedHashMap<>();
        for (Map.Entry<Constants.Resources, Resource> entry : resources.entrySet()) {
            names.put(entry.getKey(), entry.getValue().getName());
        }
        return names;
    }

    public LinkedHashMap<Constants.Resources, String> getResourceDescription() {
        LinkedHashMap<Constants.Resources, String> description = new LinkedHashMap<>();
        for (Map.Entry<Constants.Resources, Resource> entry : resources.entrySet()) {
            description.put(entry.getKey(), entry.getValue().getDescription());
        }
        return description;
    }

}
