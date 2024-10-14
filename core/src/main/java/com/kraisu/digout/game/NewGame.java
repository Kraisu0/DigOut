package com.kraisu.digout.game;

import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.help.Instruction;
import com.kraisu.digout.loaders.JsonLoader;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewGame {
    private Game game;
    private Player player;
    private Room baseRoom, exitRoom;
    private Survivor firstSurvivor;

    public void startNewGame(UUID gameId,String gamerName){
        List<Room> rooms = new ArrayList<>();
        List<Survivor> survivors = new ArrayList<>();

        this.player = createNewPlayer(gamerName); //zapisanie ustawień gracza

        JsonLoader.mainLoader(); //załadowanie plików JSON

        generateStartRooms(gameId); //wylosowanie lokalizacji bazy i wyjścia
        rooms.add(baseRoom);
        rooms.add(exitRoom);

        generateFirstSurvivor(gameId); //wygenerowanie pierwszego ocalałego

        //this.game = createNewGame(gameId, rooms, survivors); //stworzenie nowej gry

        //TODO stowrznie pliku JSON do zapisu gry

        Instruction.showInstruction();
    }

    private Player createNewPlayer(String gamerName){
        return new Player(gamerName, 0);
    }

//    private Game createNewGame(UUID gameId, List<Room> rooms, List<Survivor> survivors){
//        return new Game(gameId,this.player,0,rooms,survivors,null,);
//    }

    private void generateStartRooms(UUID gameId){
        baseRoom = Generators.generateBaseRoom(gameId);
        exitRoom = Generators.generateExitRoom(gameId);
    }

    private void generateFirstSurvivor(UUID gameId){
        firstSurvivor = Generators.generateNewSurvivors(gameId, Constants.Survivors.WORKER);
    }

}
