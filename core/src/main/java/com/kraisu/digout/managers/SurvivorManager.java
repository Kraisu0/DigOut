package com.kraisu.digout.managers;

import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.scenes.GameScreen;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.stuff.ReceivedStuff;
import com.kraisu.digout.survivor.Survivor;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.kraisu.digout.genertor.Generators.generateRandomNumber;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.descriptions;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.names;
import static com.kraisu.digout.logs.DateLogs.logs;

public class SurvivorManager implements Serializable {
    private Map<String, Survivor> survivors;

    public SurvivorManager() {
        survivors = new HashMap<>();
    }

    public void addSurvivor(Survivor survivor) {
        survivors.put(survivor.getName(), survivor);
    }

    public void removeSurvivor(String name) {
        survivors.remove(name);
    }

    public Survivor getSurvivor(String name) {
        return survivors.get(name);
    }

    public Collection<Survivor> getAllSurvivors() {
        return survivors.values();
    }

    public Map<String, Survivor> getSurvivors() {
        return survivors;
    }

    public void setSurvivors(Map<String, Survivor> survivors) {
        this.survivors = survivors;
    }

    private String generateUniqueName(List<String> names) {
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

    public Survivor generateNewSurvivors(MyGame myGame, Constants.Survivors survivorType) {
        Survivor temp = null;

        temp = new Survivor(
                myGame.getGameId(),
                "name",
                4,
                survivorType,
                null,
                "profileInformation",
                0,
                "avatars/1_65_64.png");
        temp = setRandomBio(
                myGame,
                temp,
                names,
                descriptions);

        logs(DateLogs.LogType.INFO, myGame.getGameId(), "create new Survivor. NAME: " + temp.getName() + ", ENERGY: "
                + temp.getEnergy() + ", SURVIVOR TYPE: " + survivorType + ", PI: " + temp.getProfileInformation() +
                ", AGE: " + temp.getAge(), null);

        if (temp.getProfession() == Constants.Survivors.MINER)
            temp.setEquipment(myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE));

        return temp;
    }

    // TODO do Debugowania
    // public static String getRandomAvatarPath() {
    // try {
    // File folder = new File("assets/avatars");
    //
    // if (!folder.exists()) {
    // logs(DateLogs.LogType.ERROR, null, "The 'avatars' folder does not exist.",
    // null);
    // throw new IOException("The 'avatars' folder does not exist.");
    // }
    //
    // if (!folder.isDirectory()) {
    // logs(DateLogs.LogType.ERROR, null, "'avatars' is not a directory.", null);
    // throw new IOException("'avatars' is not a directory.");
    // }
    //
    // File[] files = folder.listFiles();
    // if (files == null || files.length == 0) {
    // logs(DateLogs.LogType.ERROR, null, "There are no files in the 'avatars'
    // folder.", null);
    // throw new IOException("There are no files in the 'avatars' folder.");
    // }
    //
    // List<File> validFiles = new ArrayList<>();
    // for (File file : files) {
    // if (!file.getName().endsWith(".work.png")) {
    // validFiles.add(file);
    // }
    // }
    // if (!validFiles.isEmpty()) {
    // int randomIndex = new Random().nextInt(validFiles.size());
    // return validFiles.get(randomIndex).getPath();
    // } else {
    // logs(DateLogs.LogType.ERROR, null, "There are no matching files in the
    // 'avatars' folder.", null);
    // throw new IOException("There are no matching files in the 'avatars'
    // folder.");
    // }
    // } catch (Exception e) {
    // logs(DateLogs.LogType.ERROR, null, "Error while getRandomAvatarPath()", e);
    // return null;
    // }
    // }

    // TODO do Jarowania
    public static List<String> listFilesInJar(String folderPath) throws IOException {
        List<String> filePaths = new ArrayList<>();

        // Pobierz JAR, w którym jest uruchomiona aplikacja
        String jarPath = DigOutGame.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        // Dekoduj ścieżkę, aby obsłużyć spacje i znaki specjalne
        jarPath = URLDecoder.decode(jarPath, "UTF-8");

        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                // Sprawdź, czy plik należy do folderu avatars i pomiń katalogi
                if (entryName.startsWith(folderPath) && !entryName.endsWith("/")) {
                    filePaths.add(entryName);
                }
            }
        }

        return filePaths;
    }

    public static String getRandomAvatarPath() {
        try {
            List<String> avatarFiles = listFilesInJar("avatars");

            List<String> validFiles = new ArrayList<>();
            for (String file : avatarFiles) {
                if (!file.endsWith(".work.png") && !file.endsWith(".atlas") && !file.endsWith(".json")
                        && !file.endsWith("avatars.png")) {
                    // logs(DateLogs.LogType.INFO, null, "Valid file path:" + file, null);
                    validFiles.add(file);
                }
            }

            if (!validFiles.isEmpty()) {
                int randomIndex = new Random().nextInt(validFiles.size());
                String avatarPath = validFiles.get(randomIndex);
                logs(DateLogs.LogType.INFO, null,
                        "Random index: " + randomIndex + ", Size: " + validFiles.size() + ", AvatarPath: " + avatarPath,
                        null);
                return avatarPath;
            } else {
                System.err.println("Brak dostępnych avatarów (wszystkie są .work.png).");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getAgeFromAvatar(String fileName) {
        String[] parts = fileName.split("_");
        int ageRange = Integer.parseInt(parts[1]);
        switch (ageRange) {
            case 20:
                return generateRandomNumber(18, 39);
            case 40:
                return generateRandomNumber(40, 64);
            case 65:
                return generateRandomNumber(65, 80);
            default:
                return generateRandomNumber(18, 80);
        }
    }

    public static String getRandomName(List<String> names) {
        int randomIndex = generateRandomNumber(0, names.size() - 1);
        return names.get(randomIndex);
    }

    public static String generateRandomBio(String name, List<String> beginnings, List<String> middles,
            List<String> ends) {
        String beginning = beginnings.get(generateRandomNumber(0, beginnings.size() - 1));
        String middle = middles.get(generateRandomNumber(0, middles.size() - 1));
        String end = ends.get(generateRandomNumber(0, ends.size() - 1)).replace("X", name);
        return beginning + " " + middle + " " + end;
    }

    public Survivor setRandomBio(MyGame myGame, Survivor survivor, List<String> names,
            Map<String, List<String>> descriptions) {
        String avatarPath = getRandomAvatarPath();
        logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor new path: " + avatarPath, null);
        if (avatarPath == null)
            return survivor;

        File avatarFile = new File(avatarPath);
        int age = getAgeFromAvatar(avatarFile.getName());
        String name = generateUniqueName(names);
        String bio = generateRandomBio(
                name,
                descriptions.get("beginning"),
                descriptions.get("middle"),
                descriptions.get("end"));

        survivor.setAge(age);
        survivor.setName(name);
        survivor.setProfileInformation(bio);

        // BORBO
        if (avatarPath.endsWith("37_22_64.png")) {
            List<String> names1 = new ArrayList<>();
            names1.add("BORIA");
            survivor.setAge(22);
            survivor.setName(getRandomName(names1));
            survivor.setProfileInformation("He has no idea what he's doing here, he was dragged here by accident.");
        }

        survivor.setImgPath(avatarPath);
        survivor.setImg(new Texture(avatarPath));

        return survivor;
    }

    public Survivor whoIsInTheRoom(Coordinate coordinate) {
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getTask() != null) {
                if (entry.getValue().getTask().getCoordinateOfRoom() == coordinate) {
                    return entry.getValue();
                }
            } else {
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

    private static ReceivedStuff survivorGotReceivedStuff(Constants.Tasks task) {

        switch (task) {
            case WAIT:
            case TRAIN_TO_COOK:
            case TRAIN_TO_ENGINEER:
            case BUILD_RESTROOM:
            case BUILD_KITCHEN:
            case BUILD_ELEVATOR:
            case BUILD_WORKSHOP:
            case BUILD_AIR_PUMP:
            case BUILD_TINKER_ROOM:
                return new ReceivedStuff(0, 0, 0, 0, -1, 0, 0, 0, 0, null);
            case REST:
                return new ReceivedStuff(0, 0, 0, 0, 1, 0, 0, 0, 0, null);
            case CREATE_FOOD:
                return new ReceivedStuff(0, 0, 1, 0, -1, 0, 0, 0, 0, null);
            case DIG_OUT:
                int eq = Generators.generateRandomEquipment();
                if (eq == 0) {
                    return new ReceivedStuff(Generators.generateRandomCR(), Generators.generateRandomTools(),
                            Generators.generateRandomFood(), 0, -1,
                            1, 0, 0, 0, Generators.generateRandomSurvivor());
                } else if (eq == 1) {
                    return new ReceivedStuff(Generators.generateRandomCR(), Generators.generateRandomTools(),
                            Generators.generateRandomFood(), 0, -1,
                            0, 0, 1, 0, Generators.generateRandomSurvivor());
                } else if (eq == 2) {
                    return new ReceivedStuff(Generators.generateRandomCR(), Generators.generateRandomTools(),
                            Generators.generateRandomFood(), 0, -1,
                            0, 1, 0, 0, Generators.generateRandomSurvivor());
                } else {
                    return new ReceivedStuff(Generators.generateRandomCR(), Generators.generateRandomTools(),
                            Generators.generateRandomFood(), 0, -1,
                            0, 0, 0, 0, Generators.generateRandomSurvivor());
                }
            case EAT:
                return new ReceivedStuff(0, 0, 0, 0, 3, 0, 0, 0, 0, null);
            case CREATE_SEARCHLIGHT:
                return new ReceivedStuff(0, 0, 0, 0, -1, 1, 0, 0, 0, null);
            case CREATE_KITCHEN_ROBOT:
                return new ReceivedStuff(0, 0, 0, 0, -1, 0, 0, 1, 0, null);
            case CREATE_OXYGEN_MASK:
                return new ReceivedStuff(0, 0, 0, 0, -1, 0, 1, 0, 0, null);
            case CREATE_PICKAXE:
                return new ReceivedStuff(0, 0, 0, 0, -1, 0, 0, 0, 1, null);
            case BUILD_POWER_STATION:
                return new ReceivedStuff(0, 0, 0, 4, -1, 0, 0, 0, 0, null);
            case CREATE_TOOLS:
                return new ReceivedStuff(0, 1, 0, 0, -1, 0, 0, 0, 0, null);
        }
        return new ReceivedStuff(0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    }

    public void allSurvivorGotReceivedStuff() {
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            entry.getValue().getTask().setReceivedStuff(survivorGotReceivedStuff(entry.getValue().getTask().getTask()));
        }
    }

    public void clearSurvivorsTasks(MyGame myGame) {
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getTask() != null) {
                if (myGame.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom())
                        .getAmountOfSurvivors() != 0) {
                    myGame.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom())
                            .setAmountOfSurvivors(
                                    myGame.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom())
                                            .getAmountOfSurvivors() - 1);
                }
                myGame.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).updateSpace(myGame);
                myGame.getRoomManager().getRoom(entry.getValue().getTask().getCoordinateOfRoom()).updatePicture();
                entry.getValue().setTask(null);
                entry.getValue().changeImgForNotWork();
            } else {
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
                iterator.remove();
            }
        }
    }

    public void adjustOxygenLevelsAtLevel(MyGame myGame, int yLevel) {
        List<Survivor> survivorsAtLevel = new ArrayList<>();

        for (Survivor survivor : survivors.values()) {
            if (survivor.getTask() != null && survivor.getTask().getCoordinateOfRoom().getY() == yLevel) {
                Equipment equipment = survivor.getEquipment();
                if (equipment == null || !equipment.getName().equals("Oxygen Mask")) {
                    survivorsAtLevel.add(survivor);
                }
            }
        }

        int airPumps = myGame.getRoomManager().countAirPumpsAtLevel(yLevel);
        int survivorCount = survivorsAtLevel.size();

        int energyToRemove = 0;

        survivorCount -= airPumps;

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

    public void checkOxygenForAllLevels(MyGame myGame) {
        for (int i = 1; i <= 10; i++) {
            adjustOxygenLevelsAtLevel(myGame, i);
        }
    }

    public boolean hasLevelWithMoreThanFourSurvivors(MyGame myGame) {
        for (int yLevel = 1; yLevel <= 10; yLevel++) {
            int survivorCount = 0;

            for (Survivor survivor : survivors.values()) {
                if (survivor.getTask() != null && survivor.getTask().getCoordinateOfRoom().getY() == yLevel) {
                    Equipment equipment = survivor.getEquipment();
                    if (equipment == null || !equipment.getName().equals("Oxygen Mask")) {
                        survivorCount++;
                    }
                }
            }

            if (survivorCount >= 4) {
                return true;
            }
        }

        return false;
    }

    public void checkLoseGame() {
        if (survivors.isEmpty())
            GameScreen.setGameLose(true);
    }

    public void showDisable() {
        Map<String, Survivor> survivorsMap = getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getTask() != null) {
                System.out.println("Coordinate for " + entry.getValue().getName() + ": " +
                        entry.getValue().getTask().getCoordinateOfRoom().getX() + " ," +
                        entry.getValue().getTask().getCoordinateOfRoom().getY());
            } else {
                System.out.println("Coordinate for " + entry.getValue().getName() + ": null, null");
            }
        }
        System.out.println("\n\n");
    }

}
