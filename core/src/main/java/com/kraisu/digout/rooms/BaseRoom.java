package com.kraisu.digout.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
            50,
            new Skin(Gdx.files.internal("rooms/BaseRoomTexture.json")));
    }
}
