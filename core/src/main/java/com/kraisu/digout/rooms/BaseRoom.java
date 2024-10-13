package com.kraisu.digout.rooms;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.BASE;
import static com.kraisu.digout.help.Constants.RoomType.BASE_TYPE;

public class BaseRoom extends Room{
    public BaseRoom(Coordinate coordinate, UUID id) {
        super(id,
            coordinate,
            BASE_TYPE,
            true,
            false,
            BASE,
            false,
            50);
    }
}
