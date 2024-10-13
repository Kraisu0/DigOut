package com.kraisu.digout.rooms;

import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;
import static com.kraisu.digout.help.Constants.RoomType.HARD_ROOK_TYPE;

public class HardRockRoom extends Room {
    public HardRockRoom(int x, int y, UUID id, boolean atd) {
        super(id, x,y,HARD_ROOK_TYPE, false, atd, NOTHING, false, 0, 0, 0, 0, null, null);
    }
    //TODO powbijać generatory

}
