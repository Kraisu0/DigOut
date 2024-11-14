package com.kraisu.digout.managers;

import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.*;
import com.kraisu.digout.scenes.GameScreen;

import java.util.*;

import static com.kraisu.digout.logs.DateLogs.logs;

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

    public Map<Coordinate, Room> getRooms() {
        return rooms;
    }

    public boolean hasBuildOfType(Constants.Buildings roomType) {
        for (Room room : rooms.values()) {
            if (room.getType().equals(roomType)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRoomOfType(Constants.RoomType roomType) {
        for (Room room : rooms.values()) {
            if (room.getType().equals(roomType)) {
                return true;
            }
        }
        return false;
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

    public Coordinate[] getRoomsByType(Constants.RoomType roomType, Constants.Buildings buildings) {
        List<Coordinate> coordinates = new ArrayList<>();
        Map<Coordinate, Room> rooms = getRooms();

        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if ((roomType != null && room.getType() == roomType) ||
                (buildings != null && room.getBuildUp() == buildings)) {
                coordinates.add(entry.getKey());
            }
        }

        return coordinates.toArray(new Coordinate[0]);
    }


    private Room ableToDiscoveredRoom(Coordinate coordinate, UUID id) {
        Room exitRoom = getExitRoom();
        if(!rooms.containsKey(coordinate)) {
            if(coordinate.getY() > 8) {
                if(!exitRoom.getCoordinates().equals(coordinate)) {
                    Room temp = new UndiscoveredHardRoom(coordinate, id);
                    rooms.put(coordinate, temp);
                    return temp;
                }
                else {
                    exitRoom.setAbleToDiscover(true);
                    return exitRoom;
                }
            } else {
                Room temp = new UndiscoveredLightRoom(coordinate, id);
                rooms.put(coordinate, temp);
                return temp;
            }
        }
        return null;
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
        Coordinate tempLeft = new Coordinate(coordinate.getX() - 1, coordinate.getY());
        Room roomL;
        Room roomR;


        if (coordinate.getX() == 1 && !rooms.containsKey(tempRight)) {
            roomR = ableToDiscoveredRoom(tempRight, id);
            GameScreen.colorTileAtCoordinate(tempRight, roomR, "LIGHT_ROOM_R.0");
            roomR.setActualPicture("LIGHT_ROOM_R.0");
        }

        if (coordinate.getX() == 10 && !rooms.containsKey(tempLeft)) {
            roomL = ableToDiscoveredRoom(tempLeft, id);
            GameScreen.colorTileAtCoordinate(tempLeft, roomL, "LIGHT_ROOM_L.0");
            roomL.setActualPicture("LIGHT_ROOM_L.0");
        }

        if (!rooms.containsKey(tempRight) && !rooms.containsKey(tempLeft))
        {
            roomR = ableToDiscoveredRoom(tempRight, id);
            GameScreen.colorTileAtCoordinate(tempRight, roomR, "LIGHT_ROOM_R.0");
            roomR.setActualPicture("LIGHT_ROOM_R.0");
            roomL = ableToDiscoveredRoom(tempLeft, id);
            GameScreen.colorTileAtCoordinate(tempLeft, roomL, "LIGHT_ROOM_L.0");
            roomL.setActualPicture("LIGHT_ROOM_L.0");
        }

    }

    public void makeAbleToDiscoveredUpperRooms(Coordinate coordinate, UUID id) {
        Coordinate tempUp = new Coordinate(coordinate.getY() + 1, coordinate.getY());
        Room room;

        if(!rooms.containsKey(tempUp) && coordinate.getY() != 10) {
            room = ableToDiscoveredRoom(tempUp, id);

        }
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
