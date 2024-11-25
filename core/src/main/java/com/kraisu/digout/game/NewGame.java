package com.kraisu.digout.game;

import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.help.Instruction;
import com.kraisu.digout.loaders.JsonLoader;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.managers.*;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.util.UUID;

import static com.kraisu.digout.logs.DateLogs.logs;

public class NewGame {
    private MyGame myGame;
    private Player player;
    private Room baseRoom, exitRoom;
    private Survivor firstSurvivor;
    private RoomManager roomManager;
    private SurvivorManager survivorManager;
    private EquipmentManager equipmentManager;
    private ResourceManager resourceManager;
    private DiaryManager diaryManager;


    public NewGame(String gameName){
        JsonLoader.mainLoader(); //załadowanie plików JSON
        generateNewGame(gameName);

        Instruction.showInstruction();
    }

    private void generateNewGame(String gameName){
        roomManager = new RoomManager();
        survivorManager = new SurvivorManager();
        equipmentManager = new EquipmentManager();
        resourceManager = new ResourceManager();
        diaryManager = new DiaryManager();


        UUID uuid = generateUUID();
        generatePlayer(gameName);

        myGame = new MyGame(UUID.fromString("11111111-1111-1111-1111-111111111111"), null, 1, null, null, null, null, null);

        generateStartRooms(uuid); //wylosowanie lokalizacji bazy i wyjścia
        generateFirstSurvivor(myGame); //wygenerowanie pierwszego ocalałego

        roomManager.addRoom(baseRoom);
        roomManager.addRoom(exitRoom);

        survivorManager.addSurvivor(firstSurvivor);


        myGame = new MyGame(uuid, player, 1, roomManager,survivorManager,equipmentManager,resourceManager,diaryManager);


        //TODO stowrznie pliku JSON do zapisu gry

    }

    private void generatePlayer(String gameName){
        player = new Player(gameName, 0);
    }

    private UUID generateUUID(){
        UUID uuid = UUID.randomUUID();
        logs(DateLogs.LogType.INFO, uuid, "Generate new ID for new MyGame: " + uuid, null);
        return uuid;
    }

    private void generateStartRooms(UUID gameId){
        baseRoom = Generators.generateBaseRoom(gameId);
        exitRoom = Generators.generateExitRoom(gameId);
    }

    private void generateFirstSurvivor(MyGame myGame){
        firstSurvivor = survivorManager.generateNewSurvivors(myGame, Constants.Survivors.WORKER);
    }

    public MyGame getGame() {
        return myGame;
    }
}
