package com.kraisu.digout.managers;

import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.DiscoveredRoom;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.rooms.UndiscoveredRoom;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RoomManager {
    private Map<Coordinate, Room> rooms;

    public RoomManager() {
        rooms = new HashMap<Coordinate, Room>();
    }

    public void addRoom(Room room) {
        rooms.put(room.getCoordinates(), room);
    }

    public Room getRoom(Coordinate coord) {
        return rooms.get(coord);
    }

    public Room getExitRoom() {
        for (Room room : rooms.values()) {
            if (room.getType().equals(Constants.RoomType.EXIT_TYPE))
                return room;
        }
        System.out.println("Brak pokoju wyjscia");
        return null;
    }

    public Room getBaseRoom() {
        for (Room room : rooms.values()) {
            if (room.getType().equals(Constants.RoomType.BASE_TYPE))
                return room;
        }
        System.out.println("Brak pokoju wejścia");
        return null;
    }


    private void ableToDiscoveredRoom(Coordinate coordinate, UUID id) {
        Room exitRoom = getExitRoom();
        if(!rooms.containsKey(coordinate)) {
            if(coordinate.getY() > 8) {
                if(!exitRoom.getCoordinates().equals(coordinate))
                    rooms.put(coordinate, new UndiscoveredRoom(coordinate, id, Constants.RoomType.HARD_ROOK_TYPE));
                else
                    exitRoom.setAbleToDiscover(true);
            } else
                rooms.put(coordinate, new UndiscoveredRoom(coordinate, id, Constants.RoomType.LIGHT_ROOK_TYPE));
        }
    }

    public void discoveredRoom(Room room, UUID id) {
        if(room != getExitRoom())
            room = new DiscoveredRoom(room.getCoordinates(), id);
        else{
            room.setAbleToDiscover(true);
            room.setAbleToDiscover(false);
            room.setAbleToBuild(true);
        }
    }

    public void makeAbleToDiscoveredNearestRooms(Coordinate coordinate, UUID id) {
        Coordinate tempRight = new Coordinate(coordinate.getX() + 1, coordinate.getY());
        Coordinate tempLeft = new Coordinate(coordinate.getX() + 1, coordinate.getY());

        if (coordinate.getX() == 1 && !rooms.containsKey(tempRight))
            ableToDiscoveredRoom(tempRight, id);
        if (coordinate.getY() == 10 && !rooms.containsKey(tempLeft))
            ableToDiscoveredRoom(tempLeft, id);
        if (!rooms.containsKey(tempRight) && !rooms.containsKey(tempLeft))
        {
            ableToDiscoveredRoom(tempRight, id);
            ableToDiscoveredRoom(tempLeft, id);
        }
    }

    public void makeAbleToDiscoveredUpperRooms(Coordinate coordinate, UUID id) {
        Coordinate tempUp = new Coordinate(coordinate.getY() + 1, coordinate.getY());

        if(!rooms.containsKey(tempUp) && coordinate.getY() != 10)
            ableToDiscoveredRoom(tempUp, id);
    }

    public void buildBuilding(Room room, Constants.Buildings type, UUID id) {
        //TODO zrobić to
        switch (type) {
            case KITCHEN:

                break;
            case RESTROOM:

                break;
            case ELEVATOR:

                break;
            case WORKSHOP:

                break;
            case POWER_STATION:

                break;
            case AIR_PUMP:

                break;
            case TINKER_ROOM:

                break;
            default:
                System.out.println("Błąd w budowaniu buildBuliding w RoomManager");
                break;
        }
    }

}
