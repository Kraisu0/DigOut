package com.kraisu.digout.rooms;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;
import static com.kraisu.digout.help.Constants.RoomType.EXIT_TYPE;

public class ExitRoom extends Room {
    public ExitRoom(Coordinate coordinates, UUID id) {
        super (id,
            coordinates,
            EXIT_TYPE,
            false,
            false,
            NOTHING,
            false,
            1);
    }

}
