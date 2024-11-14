package com.kraisu.digout.managers;

import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.Equipment;
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
