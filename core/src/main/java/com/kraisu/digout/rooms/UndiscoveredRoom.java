package com.kraisu.digout.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.survivor.Survivor;

import java.util.List;
import java.util.UUID;

import static com.kraisu.digout.help.Constants.Buildings.NOTHING;
import static com.kraisu.digout.help.Constants.RoomType.EXIT_TYPE;

public class UndiscoveredRoom extends Room {
    public UndiscoveredRoom(Coordinate coordinate, UUID id, Constants.RoomType type) {
        super (id,
            coordinate,
            type,
            false,
            true,
            NOTHING,
            false,
            1,
            new Skin(Gdx.files.internal("rooms/LightRoomTexture.json")));
    }

}
