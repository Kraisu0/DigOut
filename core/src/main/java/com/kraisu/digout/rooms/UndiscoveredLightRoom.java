package com.kraisu.digout.rooms;

import com.kraisu.digout.help.Constants;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;

public class UndiscoveredLightRoom extends Room {
    public UndiscoveredLightRoom(Coordinate coordinate, UUID id) {
        super (id,
            coordinate,
            Constants.RoomType.LIGHT_ROOK_TYPE,
            false,
            true,
            NOTHING,
            false,
            1,
            "LIGHT_ROOM_U.0");
    }

}
