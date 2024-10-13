package com.kraisu.digout.stuff;

public class Equipment {
    protected int gameID;
    protected String description;
    protected String name;

    public Equipment(int gameID, String description, String name) {
        this.gameID = gameID;
        this.description = description;
        this.name = name;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setType(String name) {
        this.name = name;
    }
}
