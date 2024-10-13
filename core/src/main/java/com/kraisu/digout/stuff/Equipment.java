package com.kraisu.digout.stuff;

public class Equipment {
    protected int gameID;
    protected String description;
    protected int type;

    public Equipment(int gameID, String description, int type) {
        this.gameID = gameID;
        this.description = description;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
