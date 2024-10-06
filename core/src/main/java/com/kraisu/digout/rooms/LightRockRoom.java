package com.kraisu.digout.rooms;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;
import static com.kraisu.digout.help.Constants.RoomType.LIGHT_ROOK_TYPE;

public class LightRockRoom extends Room {
    public LightRockRoom(int x, int y, int id, boolean atd) {
        super(id, x,y,LIGHT_ROOK_TYPE, false, atd, NOTHING, false, 0, null, null, null);
    }


}
