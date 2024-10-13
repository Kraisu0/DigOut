package com.kraisu.digout.rooms;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

public abstract class Room {
    protected UUID gameId; //ID gry w jakiej znajduje się pokój
    protected int coordinateX; //pozycja poziomia na mapie
    protected int coordinateY; //pozycja pionowa na mapie
    protected Constants.RoomType type; //baza, kamienie, skały, wyjście
    protected boolean discovered; //czy pokój został odkopany
    protected boolean ableToDiscover; //czy pokój moze być odkopany
    protected Constants.Buildings buildUp; //nic, baza, kuchnia, restroom, winda, warsztat, elektrownia, pompa powietrza, majsterkowania
    protected boolean ableToBuild; //czy pokój można zabudować
    protected int amountOfSpace; //ile jest miejsca w pokoju
    protected int constructionResources;
    protected int food;
    protected int tools;
    protected List<Equipment> Equipments; //ekwipunek z odkopania
    protected List<Survivor> Survivors; //ocaleńcy z odkopania

    public Room(UUID gameId, int coordinateX, int coordinateY, Constants.RoomType type, boolean discovered, boolean ableToDiscover,
                Constants.Buildings buildUp, boolean ableToBuild, int amountOfSpace, int constructionResources, int food, int tools,
                List<Equipment> equipments, List<Survivor> survivors ) {
        this.gameId = gameId;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.type = type;
        this.discovered = discovered;
        this.ableToDiscover = ableToDiscover;
        this.buildUp = buildUp;
        this.ableToBuild = ableToBuild;
        this.amountOfSpace = amountOfSpace;
        this.constructionResources = constructionResources;
        this.food = food;
        this.tools = tools;
        this.Equipments = equipments;
        this.Survivors = survivors;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public Constants.RoomType getType() {
        return type;
    }

    public void setType(Constants.RoomType type) {
        this.type = type;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public boolean isAbleToDiscover() {
        return ableToDiscover;
    }

    public void setAbleToDiscover(boolean ableToDiscover) {
        this.ableToDiscover = ableToDiscover;
    }

    public Constants.Buildings getBuildUp() {
        return buildUp;
    }

    public void setBuildUp(Constants.Buildings buildUp) {
        this.buildUp = buildUp;
    }

    public boolean isAbleToBuild() {
        return ableToBuild;
    }

    public void setAbleToBuild(boolean ableToBuild) {
        this.ableToBuild = ableToBuild;
    }

    public int getAmountOfSpace() {
        return amountOfSpace;
    }

    public void setAmountOfSpace(int amountOfSpace) {
        this.amountOfSpace = amountOfSpace;
    }

    public int getConstructionResources() {
        return constructionResources;
    }

    public void setConstructionResources(int constructionResources) {
        this.constructionResources = constructionResources;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getTools() {
        return tools;
    }

    public void setTools(int tools) {
        this.tools = tools;
    }

    public List<Equipment> getEquipments() {
        return Equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        Equipments = equipments;
    }

    public List<Survivor> getSurvivors() {
        return Survivors;
    }

    public void setSurvivors(List<Survivor> survivors) {
        Survivors = survivors;
    }
}
