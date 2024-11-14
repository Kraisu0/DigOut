package com.kraisu.digout.rooms;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

import static com.kraisu.digout.logs.DateLogs.logs;

public abstract class Room {
    private UUID gameId; //ID gry w jakiej znajduje się pokój
    private Coordinate coordinates;
    private Constants.RoomType type; //baza, kamienie, skały, wyjście
    private boolean discovered; //czy pokój został odkopany
    private boolean ableToDiscover; //czy pokój moze być odkopany
    private Constants.Buildings buildUp; //nic, baza, kuchnia, restroom, winda, warsztat, elektrownia, pompa powietrza, majsterkowania
    private boolean ableToBuild; //czy pokój można zabudować
    private int amountOfSpace; //ile jest miejsca w pokoju
    private int amountOfSurvivors;
    private boolean isFull; //Czy pokój jest w pełni wypełniony
    private String actualPicture;

    public Room(UUID gameId, Coordinate coordinates, Constants.RoomType type, boolean discovered, boolean ableToDiscover,
                Constants.Buildings buildUp, boolean ableToBuild, int amountOfSpace, String actualPicture) {
        this.gameId = gameId;
        this.coordinates = coordinates;
        this.type = type;
        this.discovered = discovered;
        this.ableToDiscover = ableToDiscover;
        this.buildUp = buildUp;
        this.ableToBuild = ableToBuild;
        this.amountOfSpace = amountOfSpace;
        this.isFull = false;
        this.amountOfSurvivors = 0;
        this.actualPicture = actualPicture;
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

    public int getAmountOfSurvivors() {
        return amountOfSurvivors;
    }

    public void setAmountOfSurvivors(int amountOfSurvivors) {
        this.amountOfSurvivors = amountOfSurvivors;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public String getActualPicture() {
        return actualPicture;
    }

    public void setActualPicture(String actualPicture) {
        this.actualPicture = actualPicture;
    }

    public void updatePicture(){
        String[] parts = this.actualPicture.split("\\.", 2);
        this.actualPicture = parts[0] + "." + getAmountOfSurvivors();
    }

    public void updateSpace(Game game){
        if(getAmountOfSpace() == getAmountOfSurvivors())
            setFull(true);
        else if (getAmountOfSpace() > getAmountOfSurvivors())
            setFull(false);
        else
            logs(DateLogs.LogType.ERROR, game.getGameId(), "Something happened that shouldn't have" +
                " happened. There are more survivors in the room than there can be.", null);
    }

}
