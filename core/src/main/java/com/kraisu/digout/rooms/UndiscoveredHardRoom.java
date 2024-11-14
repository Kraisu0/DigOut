package com.kraisu.digout.rooms;

import com.kraisu.digout.help.Constants;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;

public class UndiscoveredHardRoom extends Room {
    public UndiscoveredHardRoom(Coordinate coordinate, UUID id) {
        super (id,
            coordinate,
            Constants.RoomType.HARD_ROOK_TYPE,
            false,
            true,
            NOTHING,
            false,
            1,
            "HARD_ROOM_U.0");
    }

}
