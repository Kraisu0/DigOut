package com.kraisu.digout.rooms;

import static com.kraisu.digout.help.Constants.Buildings.BASE;
import static com.kraisu.digout.help.Constants.RoomType.BASE_TYPE;

public class BaseRoom extends Room{
    public BaseRoom(int x, int id) {
        super(id, x,0,BASE_TYPE, true, false, BASE, false, 50, null, null, null);
    }
}
