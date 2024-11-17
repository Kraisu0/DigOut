package com.kraisu.digout.game;

import com.kraisu.digout.managers.*;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

public class Game {
    private UUID gameId;
    private Player player;
    private int round;
    private RoomManager roomManager;
    private SurvivorManager survivorManager;
    private EquipmentManager equipmentManager;
    private ResourceManager resourceManager;
    private DiaryManager diaryManager;

    public Game(UUID gameId, Player player, int round, RoomManager roomManager, SurvivorManager survivorManager,
                EquipmentManager equipmentManager, ResourceManager resourceManager, DiaryManager diaryManager) {
        this.gameId = gameId;
        this.player = player;
        this.round = round;
        this.roomManager = roomManager;
        this.survivorManager = survivorManager;
        this.equipmentManager = equipmentManager;
        this.resourceManager = resourceManager;
        this.diaryManager = diaryManager;
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

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public SurvivorManager getSurvivorManager() {
        return survivorManager;
    }

    public void setSurvivorManager(SurvivorManager survivorManager) {
        this.survivorManager = survivorManager;
    }

    public EquipmentManager getEquipmentManager() {
        return equipmentManager;
    }

    public void setEquipmentManager(EquipmentManager equipmentManager) {
        this.equipmentManager = equipmentManager;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public DiaryManager getDiaryManager() {
        return diaryManager;
    }

    public void setDiaryManager(DiaryManager diaryManager) {
        this.diaryManager = diaryManager;
    }

    //TODO zrobić volume
    public float getVolume() {
        return 0;
    }

    public void setVolume(float value) {
    }

    public void increaseRound(){
        this.round += 1;
    }
}
