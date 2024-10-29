package com.kraisu.digout.genertor;

import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.BaseRoom;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.ExitRoom;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.io.File;
import java.util.*;

import static com.kraisu.digout.help.ConstantsGenerator.ConstructionResourcesDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EQDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EquipmentDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.FoodDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.descriptions;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.names;
import static com.kraisu.digout.help.ConstantsGenerator.ProfessionDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.SurvivorsDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.ToolsDropPercentages.*;
import static com.kraisu.digout.logs.DateLogs.logs;

public class Generators {
    static long seed = System.nanoTime();
    private static final Random random = new Random(seed);

    private static final int cr_range_drop = TRIPLE_CR_DROP + DOUBLE_CR_DROP + SINGLE_CR_DROP + NO_CR_DROP;
    private static final int food_range_drop = FOOD_DROP + NO_FOOD_DROP;
    private static final int tools_range_drop = TOOLS_DROP + NO_TOOLS_DROP;
    private static final int survivors_range_drop = SURVIVOR_DROP + NO_SURVIVOR_DROP;
    private static final int profession_range_drop = ENGINEER_DROP + COOK_DROP + WORKER_DROP + UNTRAINED_DROP;
    private static final int equipment_range_drop = EQ_DROP + NO_EQ_DROP;
    private static final int eq_range_drop = SEARCHLIGHT_DROP + KITCHEN_ROBOT + OXYGEN_MASK;


    public static int generateRandomNumber(int min, int max) {
        if(min > max){
            throw new IllegalArgumentException("Error Range: (min > max)");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static int generateRandomCR(){
        int nr = generateRandomNumber(1, cr_range_drop);

        if(nr <= TRIPLE_CR_DROP)
            return 3;
        else if(nr <= TRIPLE_CR_DROP + DOUBLE_CR_DROP)
            return 2;
        else if(nr <= TRIPLE_CR_DROP + DOUBLE_CR_DROP + SINGLE_CR_DROP)
            return 1;
        else
            return 0;
    }

    public static int generateRandomFood(){
        int nr = generateRandomNumber(1, food_range_drop);

        if(nr <= FOOD_DROP)
            return 1;
        else
            return 0;
    }

    public static int generateRandomTools(){
        int nr = generateRandomNumber(1, tools_range_drop);

        if(nr <= TOOLS_DROP)
            return 1;
        else
            return 0;
    }

    public static Constants.Survivors generateRandomSurvivor(){
        int nr = generateRandomNumber(1, survivors_range_drop);

        if(nr <= SURVIVOR_DROP){
            return generateRandomProfession();
        }else
            return null;
    }

    public static Constants.Survivors generateRandomProfession(){
        int nr = generateRandomNumber(1, profession_range_drop);

        if(nr <= ENGINEER_DROP)
            return Constants.Survivors.ENGINEER;
        else if(nr <= ENGINEER_DROP + COOK_DROP)
            return Constants.Survivors.COOK;
        else if(nr <= ENGINEER_DROP + COOK_DROP + WORKER_DROP)
            return Constants.Survivors.WORKER;
        else
            return Constants.Survivors.UNTRAINED;
    }

    public static int generateRandomEquipment(){
        int nr = generateRandomNumber(1, equipment_range_drop);

        if(nr <= EQ_DROP)
            return generateRandomEQ();
        else
            return -1;
    }

    private static int generateRandomEQ() {
        int nr = generateRandomNumber(1, eq_range_drop);

        if(nr <= SEARCHLIGHT_DROP)
            return 0;
        else if(nr <= SEARCHLIGHT_DROP + KITCHEN_ROBOT)
            return 1;
        else
            return 2;
    }

   //TODO DOROBIĆ FUNKCJE BY NIE POWTARZAŁY SIE IMIONA

//    private Map<String, String> generateNameForSurvivor(UUID gameID) {
//        //Game game = game.getGameId();
//        while(true) {
//            String name = getRandomName(names);
//            if(game..getSurvivors.stream()
//                .anyMatch(() -> survivor.getName().equals(name))) {
//                return name;
//            }
//        }
//    }

//    private Map<String, String> generateName() {
//        // pobieranie losowej linijki  z pliku i dodanie do mapy jako key - value
//    //    key - name
//      //      value - opis
//    }

    //TODO przeneiść do survivor managera
    public static Survivor generateNewSurvivors(UUID id, Constants.Survivors survivorType){
        Survivor temp = null;

            temp = new Survivor(
                id,
                "name",
                4,
                survivorType,
                null,
                "profileInformation",
                0,
                null
            );
            temp = setRandomBio(
                temp,
                names,
                descriptions
            );

        logs(DateLogs.LogType.INFO, id, "create new Survivor. NAME: " + temp.getName() + ", ENERGY: "
            + temp.getEnergy() + ", SURVIVOR TYPE: " + survivorType + ", PI: " + temp.getProfileInformation() +
            ", AGE: " + temp.getAge(), null);

        return temp;
    }

    public static String getRandomAvatarPath() {
        File folder = new File("assets/avatars");
        File[] files = folder.listFiles();
        if (files != null && files.length > 1) {
            int randomIndex = generateRandomNumber(0, files.length - 1);
            return files[randomIndex].getPath();
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
        String name = getRandomName(names);
        String bio = generateRandomBio(
            name,
            descriptions.get("beginning"),
            descriptions.get("middle"),
            descriptions.get("end")
        );

        survivor.setAge(age);
        survivor.setName(name);
        survivor.setProfileInformation(bio);
        survivor.setImg(new Texture(avatarPath));

        return survivor;
    }

    public static BaseRoom generateBaseRoom(UUID gameId){
        Coordinate coordinate = new Coordinate(generateRandomNumber(1,10), 1);
        return new BaseRoom(coordinate,gameId);
    }

    public static ExitRoom generateExitRoom(UUID gameId) {
        Coordinate coordinate = new Coordinate(generateRandomNumber(1,10), 10);
        return new ExitRoom(coordinate,gameId);
    }

}
