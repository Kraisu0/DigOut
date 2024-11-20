package com.kraisu.digout.managers;

import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.stuff.ReceivedStuff;
import com.kraisu.digout.survivor.Survivor;

import java.io.File;
import java.util.*;

import static com.kraisu.digout.genertor.Generators.generateRandomCR;
import static com.kraisu.digout.genertor.Generators.generateRandomNumber;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.descriptions;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.names;
import static com.kraisu.digout.logs.DateLogs.logs;

public class SurvivorManager {
    private static Map<String, Survivor> survivors;

    public SurvivorManager() {
        survivors = new HashMap<>();
    }

    public void addSurvivor(Survivor survivor) {
        survivors.put(survivor.getName(), survivor);
    }

    public void removeSurvivor(String name) {
        survivors.remove(name);
    }

    public Survivor getSurvivors(String name) {
        return survivors.get(name);
    }

    public Collection<Survivor> getAllSurvivors() {
        return survivors.values();
    }

    public static Map<String, Survivor> getSurvivors() {
        return survivors;
    }

    public static void setSurvivors(Map<String, Survivor> survivors) {
        SurvivorManager.survivors = survivors;
    }

    private static String generateUniqueName(List<String> names) {
        String baseName;
        int randomIndex = generateRandomNumber(0, names.size() - 1);
        baseName = names.get(randomIndex);

        if (!survivors.containsKey(baseName)) {
            return baseName;
        }


        int maxNumber = 0;
        for (String name : survivors.keySet()) {
            if (name.startsWith(baseName)) {
                String suffix = name.substring(baseName.length()).trim();
                if (suffix.matches("\\d+")) {
                    int number = Integer.parseInt(suffix);
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                }
            }
        }

        return baseName + " " + (maxNumber + 1);
    }



    public static Survivor generateNewSurvivors(Game game, Constants.Survivors survivorType){
        Survivor temp = null;

        temp = new Survivor(
            game.getGameId(),
            "name",
            4,
            survivorType,
            null,
            "profileInformation",
            0,
            "assets/avatars/temp.work.png"
        );
        temp = setRandomBio(
            temp,
            names,
            descriptions
        );

        logs(DateLogs.LogType.INFO, game.getGameId(), "create new Survivor. NAME: " + temp.getName() + ", ENERGY: "
            + temp.getEnergy() + ", SURVIVOR TYPE: " + survivorType + ", PI: " + temp.getProfileInformation() +
            ", AGE: " + temp.getAge(), null);

        if(temp.getProfession() == Constants.Survivors.MINER)
            temp.setEquipment(game.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE));

        return temp;
    }

    public static String getRandomAvatarPath() {
        File folder = new File("assets/avatars");
        File[] files = folder.listFiles();

        if (files != null && files.length > 1) {
            List<File> validFiles = new ArrayList<>();

            for (File file : files) {
                if (!file.getName().endsWith(".work.png")) {
                    validFiles.add(file);
                }
            }

            if (!validFiles.isEmpty()) {
                int randomIndex = new Random().nextInt(validFiles.size());
                return validFiles.get(randomIndex).getPath();
            }
        }
        return null;
    }

    public static int getAgeFromAvatar(String fileName) {
        String[] parts = fileName.split("_");
        int ageRange = Integer.parseInt(parts[1]);
        switch (ageRange) {
            case 20: return generateRandomNumber(18, 39);
            case 40: return generateRandomNumber(40, 64);
            case 65: return generateRandomNumber(65, 80);
            default: return generateRandomNumber(18, 80);
        }
    }

    public static String getRandomName(List<String> names) {
        int randomIndex = generateRandomNumber(0, names.size() - 1);
        return names.get(randomIndex);
    }

    public static String generateRandomBio(String name, List<String> beginnings, List<String> middles, List<String> ends) {
        String beginning = beginnings.get(generateRandomNumber(0, beginnings.size() - 1));
        String middle = middles.get(generateRandomNumber(0, middles.size() - 1));
        String end = ends.get(generateRandomNumber(0, ends.size() - 1)).replace("X", name);
        return beginning + " " + middle + " " + end;
    }

    public static Survivor setRandomBio(Survivor survivor, List<String> names, Map<String, List<String>> descriptions) {
        String avatarPath = getRandomAvatarPath();
        if (avatarPath == null)
            return survivor;

        File avatarFile = new File(avatarPath);
        int age = getAgeFromAvatar(avatarFile.getName());
        String name = generateUniqueName(names);
        String bio = generateRandomBio(
            name,
            descriptions.get("beginning"),
            descriptions.get("middle"),
            descriptions.get("end")
        );

        survivor.setAge(age);
        survivor.setName(name);
        survivor.setProfileInformation(bio);
        survivor.setImgPath(avatarPath);
        survivor.setImg(new Texture(avatarPath));

        return survivor;
    }

    public Survivor whoIsInTheRoom(Coordinate coordinate){
        Map<String, Survivor> survivorsMap = getSurvivors();
        for(Map.Entry<String, Survivor> entry : survivorsMap.entrySet()){
            if(entry.getValue().getTask() != null) {
                if (entry.getValue().getTask().getCoordinateOfRoom() == coordinate) {
                    return entry.getValue();
                }
            }else{
                continue;
            }
        }
        return null;
    }

    public boolean checkIfAllSurvivorHaveTask() {
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getTask() == null) {
                return false;
            }
        }
        return true;
    }

    private static ReceivedStuff survivorGotReceivedStuff(Constants.Tasks task){

        switch(task){
            case WAIT:
            case TRAIN_TO_COOK:
            case TRAIN_TO_ENGINEER:
            case BUILD_RESTROOM:
            case BUILD_KITCHEN:
            case BUILD_ELEVATOR:
            case BUILD_WORKSHOP:
            case BUILD_AIR_PUMP:
            case BUILD_TINKER_ROOM:
                return new ReceivedStuff(0,0,0,0,-1,0,0,0,0,null);
            case REST:
                return new ReceivedStuff(0,0,0,0,1,0,0,0,0,null);
            case CREAT_FOOD:
                return new ReceivedStuff(0,0,1,0,-1,0,0,0,0,null);
            case DIG_OUT:
                int eq = Generators.generateRandomEquipment();
                if(eq == 0){
                    return new ReceivedStuff(Generators.generateRandomCR(),Generators.generateRandomTools(),
                        Generators.generateRandomFood(),0,-1,
                        1,0,0,0, Generators.generateRandomSurvivor());
                }else if(eq == 1){
                    return new ReceivedStuff(Generators.generateRandomCR(),Generators.generateRandomTools(),
                        Generators.generateRandomFood(),0,-1,
                        0,0,1,0, Generators.generateRandomSurvivor());
                }else if(eq == 2){
                    return new ReceivedStuff(Generators.generateRandomCR(),Generators.generateRandomTools(),
                        Generators.generateRandomFood(),0,-1,
                        0,1,0,0, Generators.generateRandomSurvivor());
                }else{
                    return new ReceivedStuff(Generators.generateRandomCR(),Generators.generateRandomTools(),
                        Generators.generateRandomFood(),0,-1,
                        0,0,0,0, Generators.generateRandomSurvivor());
                }
            case EAT:
                return new ReceivedStuff(0,0,0,0,3,0,0,0,0,null);
            case CREAT_SEARCHLIGHT:
                return new ReceivedStuff(0,0,0,0,-1,1,0,0,0,null);
            case CREAT_KITCHEN_ROBOT:
                return new ReceivedStuff(0,0,0,0,-1,0,0,1,0,null);
            case CREAT_OXYGEN_MASK:
                return new ReceivedStuff(0,0,0,0,-1,0,1,0,0,null);
            case CREAT_PICKAXE:
                return new ReceivedStuff(0,0,0,0,-1,0,0,0,1,null);
            case BUILD_POWER_STATION:
                return new ReceivedStuff(0,0,0,4,-1,0,0,0,0,null);
            case CREAT_TOOLS:
                return new ReceivedStuff(0,1,0,0,-1,0,0,0,0,null);
        }
        return new ReceivedStuff(0,0,0,0,0,0,0,0,0,null);
    }

    public void allSurvivorGotReceivedStuff(){
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
                entry.getValue().getTask().setReceivedStuff(survivorGotReceivedStuff(entry.getValue().getTask().getTask()));
        }
    }

    public void clearSurvivorsTasks(Game game){
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if(entry.getValue().getTask() != null) {
                if(game.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).getAmountOfSurvivors() != 0) {
                    game.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).setAmountOfSurvivors(
                        game.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).getAmountOfSurvivors() - 1
                    );
                }
                game.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).updateSpace(game);
                game.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).updatePicture();
                entry.getValue().setTask(null);
                entry.getValue().changeImgForNotWork();
            }else{
                continue;
            }
        }
    }

    public void checkSurvivorStatus() {
        Map<String, Survivor> survivorsMap = getSurvivors();
        Iterator<Map.Entry<String, Survivor>> iterator = survivorsMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Survivor> entry = iterator.next();
            if (entry.getValue().getEnergy() <= 0) {
                iterator.remove(); // Bezpieczne usuwanie
            }
        }
    }

    public void adjustOxygenLevelsAtLevel(int yLevel) {
        List<Survivor> survivorsAtLevel = new ArrayList<>();

        for (Survivor survivor : survivors.values()) {
            if (survivor.getTask() != null && survivor.getTask().getCoordinateOfRoom().getY() == yLevel) {
                Equipment equipment = survivor.getEquipment();
                if (equipment == null || !equipment.getName().equals("Oxygen Mask")) {
                    survivorsAtLevel.add(survivor);
                }
            }
        }

        int survivorCount = survivorsAtLevel.size();

        int energyToRemove = 0;
        if (survivorCount == 4) {
            energyToRemove = 1;
        } else if (survivorCount == 5) {
            energyToRemove = 2;
        } else if (survivorCount == 6) {
            energyToRemove = 3;
        } else if (survivorCount >= 7) {
            energyToRemove = 4;
        }

        for (Survivor survivor : survivorsAtLevel) {
            survivor.reduceEnergy(energyToRemove);
        }
    }

    public void checkOxygenForAllLevels(){
        for(int i = 1; i <= 10; i++){
            adjustOxygenLevelsAtLevel(i);
        }
    }


    public void showDisable(){
        Map<String, Survivor> survivorsMap = getSurvivors();
        for(Map.Entry<String, Survivor> entry : survivorsMap.entrySet()){
            if(entry.getValue().getTask() != null) {
                System.out.println("Coordinate for " + entry.getValue().getName() + ": " +
                    entry.getValue().getTask().getCoordinateOfRoom().getX() + " ," +
                    entry.getValue().getTask().getCoordinateOfRoom().getY());
            }else{
                System.out.println("Coordinate for " + entry.getValue().getName() + ": null, null");
            }
        }
        System.out.println("\n\n");
    }

}
