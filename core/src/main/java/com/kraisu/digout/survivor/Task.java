package com.kraisu.digout.survivor;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.BuildingPrice;
import com.kraisu.digout.stuff.ReceivedStuff;

import java.io.Serializable;

public class Task implements Serializable {
    private Coordinate coordinateOfRoom;
    private Constants.Tasks task;
    private BuildingPrice cost;
    private ReceivedStuff receivedStuff;

    public Task(Coordinate coordinateOfRoom, Constants.Tasks task, BuildingPrice cost, ReceivedStuff receivedStuff) {
        this.coordinateOfRoom = coordinateOfRoom;
        this.task = task;
        this.cost = cost;
        this.receivedStuff = receivedStuff;
    }

    public Coordinate getCoordinateOfRoom() {
        return coordinateOfRoom;
    }

    public void setCoordinateOfRoom(Coordinate coordinateOfRoom) {
        this.coordinateOfRoom = coordinateOfRoom;
    }

    public Constants.Tasks getTask() {
        return task;
    }

    public void setTask(Constants.Tasks task) {
        this.task = task;
    }

    public BuildingPrice getCost() {
        if(cost == null)
        {
            return new BuildingPrice(0, 0, 0, 0,0, false);
        }
        return cost;
    }

    public void setCost(BuildingPrice cost) {
        this.cost = cost;
    }

    public ReceivedStuff getReceivedStuff() {
        return receivedStuff;
    }

    public void setReceivedStuff(ReceivedStuff receivedStuff) {
        this.receivedStuff = receivedStuff;
    }
}

