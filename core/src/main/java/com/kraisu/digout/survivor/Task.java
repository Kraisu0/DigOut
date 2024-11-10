package com.kraisu.digout.survivor;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.rooms.Coordinate;

public class Task {
    private Coordinate coordinateOfRoom;
    private Constants.Tasks task;

    public Task(Coordinate coordinateOfRoom, Constants.Tasks task) {
        this.coordinateOfRoom = coordinateOfRoom;
        this.task = task;
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
}

