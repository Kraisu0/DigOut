package com.kraisu.digout.rooms;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;

public class DiscoveredRoom extends Room {
    public DiscoveredRoom(Coordinate coordinate, UUID id) {
        super(id,
            coordinate,
            Constants.RoomType.ROOM_TO_ARRANGE,
            true,
            false,
            NOTHING,
            true,
            1 );
    }
}
