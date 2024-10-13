package com.kraisu.digout.game;

import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

public class Game {
    private UUID gameId;
    private Player player;
    private int round;
    private List<Room> rooms;
    private List<Survivor> survivors;
    private List<Equipment> equipments;
    private int constructionResources;
    private int tools;
    private int foods;

    public Game(UUID gameId, Player player, int round, List<Room> rooms, List<Survivor> survivors,
                List<Equipment> equipments, int constructionResources, int tools, int foods) {
        this.gameId = gameId;
        this.player = player;
        this.round = round;
        this.rooms = rooms;
        this.survivors = survivors;
        this.equipments = equipments;
        this.constructionResources = constructionResources;
        this.tools = tools;
        this.foods = foods;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Survivor> getSurvivors() {
        return survivors;
    }

    public void setSurvivors(List<Survivor> survivors) {
        this.survivors = survivors;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public int getConstructionResources() {
        return constructionResources;
    }

    public void setConstructionResources(int constructionResources) {
        this.constructionResources = constructionResources;
    }

    public int getTools() {
        return tools;
    }

    public void setTools(int tools) {
        this.tools = tools;
    }

    public int getFoods() {
        return foods;
    }

    public void setFoods(int foods) {
        this.foods = foods;
    }
}
