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
            if (room.getBuildUp().equals(roomType)) {
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
        System.out.println("There is no exit room");
        return null;
    }

    public Room getBaseRoom() {
        for (Room room : rooms.values()) {
            if (room.getType().equals(Constants.RoomType.BASE_TYPE))
                return room;
        }
        System.out.println("There is no base room");
        return null;
    }

//    public Room getElevator() {
//        for (Room room : rooms.values()) {
//            if (room.getBuildUp().equals(Constants.Buildings.ELEVATOR))
//                return room;
//        }
//        System.out.println("There isn't any elevator");
//        return null;
//    }

    public Coordinate[] getRoomsByType(Constants.RoomType roomType, Constants.Buildings buildings) {
        List<Coordinate> coordinates = new ArrayList<>();
        Map<Coordinate, Room> rooms = getRooms();

        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if ((roomType != null && room.getType() == roomType && room.getBuildUp() == buildings) ||
                (buildings != null && room.getBuildUp() == buildings && buildings != Constants.Buildings.NOTHING)) {
                coordinates.add(entry.getKey());
            }
        }

        return coordinates.toArray(new Coordinate[0]);
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

    private Room ableToDiscoveredRoom(Coordinate coordinate, Game game) {
        Room exitRoom = getExitRoom();
        if(!rooms.containsKey(coordinate)) {
            if(coordinate.getY() > 8) {
                if(!exitRoom.getCoordinates().equals(coordinate)) {
                    Room temp = new UndiscoveredHardRoom(coordinate, game.getGameId());
                    rooms.put(coordinate, temp);
                    logs(DateLogs.LogType.INFO, game.getGameId(), "Open Hard room to discover on cord: ("
                        + temp.getCoordinates().getX() + ", " + temp.getCoordinates().getY() + ")" , null);
                    return temp;
                }
                else {
                    exitRoom.setAbleToDiscover(true);
                    return exitRoom;
                }
            } else {
                Room temp = new UndiscoveredLightRoom(coordinate, game.getGameId());
                rooms.put(coordinate, temp);
                logs(DateLogs.LogType.INFO, game.getGameId(), "Open Light room to discover on cord: ("
                    + temp.getCoordinates().getX() + ", " + temp.getCoordinates().getY() + ")" , null);
                return temp;

            }
        }
        return null;
    }

    public void makeAbleToDiscoveredNearestRooms(Coordinate coordinate, Game game) {
        Coordinate tempRight = new Coordinate(coordinate.getX() + 1, coordinate.getY());
        Coordinate tempUpRight = new Coordinate(coordinate.getX() + 1, coordinate.getY() + 1);
        Coordinate temp2Right = new Coordinate(coordinate.getX() + 2, coordinate.getY());
        Coordinate tempLeft = new Coordinate(coordinate.getX() - 1, coordinate.getY());
        Coordinate tempUpLeft = new Coordinate(coordinate.getX() - 1, coordinate.getY() + 1);
        Coordinate temp2Left = new Coordinate(coordinate.getX() - 2, coordinate.getY());
        Coordinate tempUp = new Coordinate(coordinate.getX(), coordinate.getY() + 1);
        Room roomL;
        Room roomR;
        Room roomU;

        //TODO jeżeli będą dodatkowe grafiki
//        if(coordinate.getY() != 10 && checkElevator(coordinate) && checkRoomToArrange(tempUpRight) && checkRoomToArrange(tempUpLeft) &&
//        checkIsInRooms(tempUp)){
//            String name = checkNewRoomType(tempUp);
//            roomU = ableToDiscoveredRoom(tempUp, game);
//            GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U_R_L.0");
//            roomU.setActualPicture(name + "_U_R_L.0");
//        }else if(coordinate.getY() != 10 && coordinate.getX() != 10 && checkElevator(coordinate) && checkRoomToArrange(tempUpRight) &&
//        checkIsInRooms(tempUp)){
//            String name = checkNewRoomType(tempUp);
//            roomU = ableToDiscoveredRoom(tempUp, game);
//            GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U_L.0");
//            roomU.setActualPicture(name + "_U_L.0");
//        }else if(coordinate.getY() != 10 && coordinate.getX() != 1 && checkElevator(coordinate) && checkRoomToArrange(tempUpLeft) &&
//            checkIsInRooms(tempUp)){
//            String name = checkNewRoomType(tempUp);
//            roomU = ableToDiscoveredRoom(tempUp, game);
//            GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U_R.0");
//            roomU.setActualPicture(name + "_U_R.0");
//        }else if(coordinate.getY() != 10 && checkElevator(coordinate) &&
//            checkIsInRooms(tempUp)){
//            String name = checkNewRoomType(tempUp);
//            roomU = ableToDiscoveredRoom(tempUp, game);
//            GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U.0");
//            roomU.setActualPicture(name + "_U.0");
//        }else if (checkRoomToArrange(temp2Right) && checkIsInRooms(tempRight)) {
//            String name = checkNewRoomType(tempRight);
//            roomR = ableToDiscoveredRoom(tempRight, game);
//            GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R_L.0");
//            roomR.setActualPicture(name + "_R_L.0");
//        }else if (checkRoomToArrange(temp2Left) && checkIsInRooms(tempLeft)) {
//            String name = checkNewRoomType(tempLeft);
//            roomL = ableToDiscoveredRoom(tempLeft, game);
//            GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_R_L.0");
//            roomL.setActualPicture(name + "_R_L.0");
//        }else if (coordinate.getX() != 1 && checkIsInRooms(tempLeft)) {
//            String name = checkNewRoomType(tempLeft);
//            roomL = ableToDiscoveredRoom(tempLeft, game);
//            GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_L.0");
//            roomL.setActualPicture(name + "_L.0");
//        }else if (coordinate.getX() != 10 && checkIsInRooms(tempRight)) {
//            String name = checkNewRoomType(tempRight);
//            roomR = ableToDiscoveredRoom(tempRight, game);
//            GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
//            roomR.setActualPicture(name + "_R.0");
//        }else if ((coordinate.getX() != 10 && checkIsInRooms(tempRight)) && (coordinate.getX() != 1 && checkIsInRooms(tempLeft))) {
//            String name = checkNewRoomType(tempRight);
//            String name1 = checkNewRoomType(tempLeft);
//            roomR = ableToDiscoveredRoom(tempRight, game);
//            roomL = ableToDiscoveredRoom(tempLeft, game);
//            GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
//            GameScreen.colorTileAtCoordinate(tempLeft, roomR, name + "_L.0");
//            roomR.setActualPicture(name + "_R.0");
//            roomL.setActualPicture(name + "_L.0");
//        }else{
//            System.out.println("There is no room to set able to discovered");
//        }

        if(rooms.get(coordinate) == getExitRoom() && !getExitRoom().isDiscovered()) {
            System.out.println("Check unable Exit room");
        }else{
            if(coordinate.getY() != 10 && checkElevator(coordinate) && !checkIsInRooms(tempUp) ||
                coordinate.getY() != 10 && checkElevator(coordinate) && checkRoomToArrange(tempUpRight) && checkRoomToArrange(tempUpLeft) &&
                    !checkIsInRooms(tempUp) ||
                coordinate.getY() != 10 && coordinate.getX() != 10 && checkElevator(coordinate) && checkRoomToArrange(tempUpRight) &&
                    !checkIsInRooms(tempUp) ||
                coordinate.getY() != 10 && coordinate.getX() != 1 && checkElevator(coordinate) && checkRoomToArrange(tempUpLeft) &&
                    !checkIsInRooms(tempUp)){
                String name = checkNewRoomType(tempUp);
                roomU = ableToDiscoveredRoom(tempUp, game);
                GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U.0");
                roomU.setActualPicture(name + "_U.0");
            }

            if (checkIsAbleToDiscovered(tempUp) && checkElevator(coordinate) && (checkRoomToArrange(tempUpLeft) ||
                checkRoomToArrange(tempUpRight)) && checkIsRookRoom(tempUp)){
                String name = checkNewRoomType(tempUp);
                GameScreen.colorTileAtCoordinate(tempRight, getRoom(tempUp), name + "_U.0");
                getRoom(tempUp).setActualPicture(name + "_U.0");
            }

            if (checkRoomToArrange(temp2Left) && !checkIsInRooms(tempLeft)) {
                String name = checkNewRoomType(tempLeft);
                roomL = ableToDiscoveredRoom(tempLeft, game);
                GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_R_L.0");
                roomL.setActualPicture(name + "_R_L.0");
            }

            if (checkRoomToArrange(temp2Left) && checkIsAbleToDiscovered(tempLeft) && checkIsRookRoom(tempLeft)) {
                String name = checkNewRoomType(tempLeft);
                GameScreen.colorTileAtCoordinate(tempLeft, getRoom(tempLeft), name + "_R_L.0");
                getRoom(tempLeft).setActualPicture(name + "_R_L.0");
            }

            if (checkRoomToArrange(temp2Right) && checkIsAbleToDiscovered(tempRight) && checkIsRookRoom(tempRight)) {
                String name = checkNewRoomType(tempRight);
                GameScreen.colorTileAtCoordinate(tempRight, getRoom(tempRight), name + "_R_L.0");
                getRoom(tempRight).setActualPicture(name + "_R_L.0");
            }

            if ((coordinate.getX() != 10 && !checkIsInRooms(tempRight)) && (coordinate.getX() != 1 && !checkIsInRooms(tempLeft))){
                String name = checkNewRoomType(tempRight);
                String name1 = checkNewRoomType(tempLeft);
                roomR = ableToDiscoveredRoom(tempRight, game);
                roomL = ableToDiscoveredRoom(tempLeft, game);
                GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
                GameScreen.colorTileAtCoordinate(tempLeft, roomR, name1 + "_L.0");
                roomR.setActualPicture(name + "_R.0");
                roomL.setActualPicture(name + "_L.0");
            }

            if (coordinate.getX() != 1 && !checkIsInRooms(tempLeft)) {
                String name = checkNewRoomType(tempLeft);
                roomL = ableToDiscoveredRoom(tempLeft, game);
                GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_L.0");
                roomL.setActualPicture(name + "_L.0");
            }

            if (coordinate.getX() != 10 && !checkIsInRooms(tempRight)) {
                String name = checkNewRoomType(tempRight);
                roomR = ableToDiscoveredRoom(tempRight, game);
                GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
                roomR.setActualPicture(name + "_R.0");
            }

        }

    }

    public String checkNewRoomType (Coordinate coordinate) {

        if(coordinate.getY() > 8){
            if(coordinate.getY() == 10 && coordinate.getX() == getExitRoom().getCoordinates().getX())
                return "EXIT_ROOM";
            else
                return "HARD_ROOM";
        }else
            return "LIGHT_ROOM";
    }

    public boolean checkElevator(Coordinate coordinate) {
        if(rooms.get(coordinate) == null)
            return false;
        else
            return rooms.get(coordinate).getBuildUp().equals(Constants.Buildings.ELEVATOR);
    }

    public boolean checkRoomToArrange(Coordinate coordinate) {
        if(rooms.get(coordinate) == null)
            return false;
        else
            return rooms.get(coordinate).getType().equals(Constants.RoomType.ROOM_TO_ARRANGE);
    }

    public boolean checkIsInRooms(Coordinate coordinate) {
        if(rooms.get(coordinate) == null)
            return false;
        else
            return rooms.containsKey(coordinate);
    }

    public boolean checkIsAbleToDiscovered(Coordinate coordinate) {
        if(rooms.get(coordinate) == null)
            return false;
        else
            return rooms.get(coordinate).isAbleToDiscover();
    }

    public boolean checkIsRookRoom(Coordinate coordinate) {
        if(rooms.get(coordinate).getType() == Constants.RoomType.HARD_ROOK_TYPE || rooms.get(coordinate).getType() == Constants.RoomType.LIGHT_ROOK_TYPE)
            return true;
        else
            return false;
    }

}
