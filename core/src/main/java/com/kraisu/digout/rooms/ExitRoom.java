package com.kraisu.digout.rooms;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;
import static com.kraisu.digout.help.Constants.RoomType.EXIT_TYPE;

public class ExitRoom extends Room {
    public ExitRoom(int x, UUID id) {
        super (id,
            x,
            9,
            EXIT_TYPE,
            false,
            false,
            NOTHING,
            false,
            0,
            0,
            0,
            0,
            null,
            null
            );
    }

}
