package com.kraisu.digout.managers;

import com.kraisu.digout.stuff.Resource;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private Map<String, Resource> resources;

    public ResourceManager() {
        this.resources = new HashMap<>();
        initializeResources();
    }

    private void initializeResources() {
        resources.put("constructionResources", new Resource("Construction Resources", "Materials for building", "assets/avatars/CR_icon_64.png", 0));
        resources.put("tools", new Resource("Tools", "Tools for workers and for building", "assets/avatars/TOOLS_icon_64.png", 0));
        resources.put("foods", new Resource("Food Supplies", "Food for survivors", "assets/avatars/FOODS_icon_64.png", 0));
        resources.put("electricity", new Resource("Electricity", "Power for power supply", "assets/avatars/ELECTRICITY_icon_64.png", 0));
    }

    public Resource getResource(String resourceName) {
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

    public Map<String, Integer> getResourceStatus() {
        Map<String, Integer> status = new HashMap<>();
        for (Map.Entry<String, Resource> entry : resources.entrySet()) {
            status.put(entry.getKey(), entry.getValue().getTotalAmount());
        }
        return status;
    }

    public Map<String, Integer> getAllocatedResourcesStatus() {
        Map<String, Integer> allocatedStatus = new HashMap<>();
        for (Map.Entry<String, Resource> entry : resources.entrySet()) {
            allocatedStatus.put(entry.getKey(), entry.getValue().getAllocatedAmount());
        }
        return allocatedStatus;
    }
}
