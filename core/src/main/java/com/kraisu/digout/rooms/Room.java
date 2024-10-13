package com.kraisu.digout.rooms;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

public abstract class Room {
    protected UUID gameId; //ID gry w jakiej znajduje się pokój
//    protected int coordinateX; //pozycja poziomia na mapie
//    protected int coordinateY; //pozycja pionowa na mapie
    protected Coordinate coordinates;
    protected Constants.RoomType type; //baza, kamienie, skały, wyjście
    protected boolean discovered; //czy pokój został odkopany
    protected boolean ableToDiscover; //czy pokój moze być odkopany
    protected Constants.Buildings buildUp; //nic, baza, kuchnia, restroom, winda, warsztat, elektrownia, pompa powietrza, majsterkowania
    protected boolean ableToBuild; //czy pokój można zabudować
    protected int amountOfSpace; //ile jest miejsca w pokoju

    public Room(UUID gameId, Coordinate coordinates, Constants.RoomType type, boolean discovered, boolean ableToDiscover,
                Constants.Buildings buildUp, boolean ableToBuild, int amountOfSpace ) {
        this.gameId = gameId;
        this.coordinates = coordinates;
        this.type = type;
        this.discovered = discovered;
        this.ableToDiscover = ableToDiscover;
        this.buildUp = buildUp;
        this.ableToBuild = ableToBuild;
        this.amountOfSpace = amountOfSpace;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
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

}
