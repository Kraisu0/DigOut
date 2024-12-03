package com.kraisu.digout.managers;

import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.*;
import com.kraisu.digout.scenes.GameScreen;

import java.io.Serializable;
import java.util.*;

import static com.kraisu.digout.logs.DateLogs.logs;

public class RoomManager implements Serializable {
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

    public Coordinate[] getExitRoomToBuild() {
        List<Coordinate> coordinates = new ArrayList<>();

        coordinates.add(getExitRoom().getCoordinates());

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

    private Room ableToDiscoveredRoom(Coordinate coordinate, MyGame myGame) {
        Room exitRoom = getExitRoom();
        if(!rooms.containsKey(coordinate) || !exitRoom.isAbleToBuild()) {
            if(coordinate.getY() > 8) {
                if(!exitRoom.getCoordinates().equals(coordinate)) {
                    Room temp = new UndiscoveredHardRoom(coordinate, myGame.getGameId());
                    rooms.put(coordinate, temp);
                    logs(DateLogs.LogType.INFO, myGame.getGameId(), "Open Hard room to discover on cord: ("
                        + temp.getCoordinates().getX() + ", " + temp.getCoordinates().getY() + ")" , null);
                    return temp;
                }
                else {
                    exitRoom.setAbleToDiscover(true);
                    logs(DateLogs.LogType.INFO, myGame.getGameId(), "Open Exit room to discover on cord: ("
                        + exitRoom.getCoordinates().getX() + ", " + exitRoom.getCoordinates().getY() + ")" , null);
                    return exitRoom;
                }
            } else {
                Room temp = new UndiscoveredLightRoom(coordinate, myGame.getGameId());
                rooms.put(coordinate, temp);
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "Open Light room to discover on cord: ("
                    + temp.getCoordinates().getX() + ", " + temp.getCoordinates().getY() + ")" , null);
                return temp;

            }
        }
        return null;
    }

    public void makeAbleToDiscoveredNearestRooms(Coordinate coordinate, MyGame myGame) {
        logs(DateLogs.LogType.INFO, myGame.getGameId(), "Make able to discovered nearest room for room: "
            + coordinate.getX() + ", " + coordinate.getY(), null);
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

        boolean isElevator = checkElevator(coordinate);
        boolean isValidCoordinateTopSide = coordinate.getY() != 10;
        boolean isValidCoordinateRightSide = coordinate.getX() != 10;
        boolean isValidCoordinateLeftSide = coordinate.getX() != 1;
        boolean isUpRoomValid = !checkIsInRooms(tempUp) || tempUp.equals(getExitRoom().getCoordinates());


            if(isValidCoordinateTopSide && isElevator && !checkIsInRooms(tempUp) ||
                isValidCoordinateTopSide && isElevator && checkRoomToArrange(tempUpRight) && checkRoomToArrange(tempUpLeft) &&
                    (!checkIsInRooms(tempUp) || tempUp.equals(getExitRoom().getCoordinates())) ||
                isValidCoordinateTopSide && isValidCoordinateRightSide && isElevator && checkRoomToArrange(tempUpRight) &&
                    (!checkIsInRooms(tempUp) || tempUp.equals(getExitRoom().getCoordinates())) ||
                isValidCoordinateTopSide && isValidCoordinateLeftSide && isElevator && checkRoomToArrange(tempUpLeft) &&
                    (!checkIsInRooms(tempUp) || tempUp.equals(getExitRoom().getCoordinates()))){
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room Up at: " + tempUp.getX() + ", " + tempUp.getY(), null);
                String name = checkNewRoomType(tempUp);
                roomU = ableToDiscoveredRoom(tempUp, myGame);
                GameScreen.colorTileAtCoordinate(tempUp, roomU, name + "_U.0");
                roomU.setActualPicture(name + "_U.0");
            }

            if (checkIsAbleToDiscovered(tempUp) && isElevator && (checkRoomToArrange(tempUpLeft) ||
                checkRoomToArrange(tempUpRight)) && (checkIsRookRoom(tempUp) && (tempUp.equals(getExitRoom().getCoordinates()) && !getExitRoom().isAbleToDiscover()))){
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room Up from other side at: " + tempUp.getX() + ", " + tempUp.getY(), null);
                String name = checkNewRoomType(tempUp);
                GameScreen.colorTileAtCoordinate(tempRight, getRoom(tempUp), name + "_U.0");
                getRoom(tempUp).setActualPicture(name + "_U.0");
            }

            if (checkRoomToArrange(temp2Left) && (!checkIsInRooms(tempLeft) || (tempLeft.equals(getExitRoom().getCoordinates()) && !getExitRoom().isAbleToDiscover()))) {
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room RL at: " + tempLeft.getX() + ", " + tempLeft.getY(), null);
                String name = checkNewRoomType(tempLeft);
                roomL = ableToDiscoveredRoom(tempLeft, myGame);
                GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_R_L.0");
                roomL.setActualPicture(name + "_R_L.0");
            }

            if (checkRoomToArrange(temp2Left) && checkIsAbleToDiscovered(tempLeft) && (checkIsRookRoom(tempLeft) || tempLeft.equals(getExitRoom().getCoordinates()))) {
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room RL from left side at: " + tempLeft.getX() + ", " + tempLeft.getY(), null);
                String name = checkNewRoomType(tempLeft);
                GameScreen.colorTileAtCoordinate(tempLeft, getRoom(tempLeft), name + "_R_L.0");
                getRoom(tempLeft).setActualPicture(name + "_R_L.0");
            }

            if (checkRoomToArrange(temp2Right) && checkIsAbleToDiscovered(tempRight) && (checkIsRookRoom(tempRight) || tempRight.equals(getExitRoom().getCoordinates()))) {
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room RL from right at: " + tempRight.getX() + ", " + tempRight.getY(), null);
                String name = checkNewRoomType(tempRight);
                GameScreen.colorTileAtCoordinate(tempRight, getRoom(tempRight), name + "_R_L.0");
                getRoom(tempRight).setActualPicture(name + "_R_L.0");
            }

            if ((isValidCoordinateRightSide && (!checkIsInRooms(tempRight) || tempRight.equals(getExitRoom().getCoordinates()))) &&
                (isValidCoordinateLeftSide && (!checkIsInRooms(tempLeft) || (tempLeft.equals(getExitRoom().getCoordinates()) && !getExitRoom().isAbleToDiscover())))){
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room R at: " + tempRight.getX() + ", " + tempRight.getY(), null);
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room L at: " + tempLeft.getX() + ", " + tempLeft.getY(), null);
                String name = checkNewRoomType(tempRight);
                String name1 = checkNewRoomType(tempLeft);
                roomR = ableToDiscoveredRoom(tempRight, myGame);
                roomL = ableToDiscoveredRoom(tempLeft, myGame);
                GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
                GameScreen.colorTileAtCoordinate(tempLeft, roomL, name1 + "_L.0");
                roomR.setActualPicture(name + "_R.0");
                roomL.setActualPicture(name1 + "_L.0");
            }

            if (isValidCoordinateLeftSide && (!checkIsInRooms(tempLeft) || (tempLeft.equals(getExitRoom().getCoordinates()) && !getExitRoom().isAbleToDiscover()))) {
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room L at: " + tempUp.getX() + ", " + tempUp.getY(), null);
                String name = checkNewRoomType(tempLeft);
                roomL = ableToDiscoveredRoom(tempLeft, myGame);
                GameScreen.colorTileAtCoordinate(tempLeft, roomL, name + "_L.0");
                roomL.setActualPicture(name + "_L.0");
            }

            if (isValidCoordinateRightSide && (!checkIsInRooms(tempRight) || tempRight.equals(getExitRoom().getCoordinates()) && !getExitRoom().isAbleToDiscover())) {
                logs(DateLogs.LogType.INFO, myGame.getGameId(), "discovered nearest room R at: " + tempUp.getX() + ", " + tempUp.getY(), null);
                String name = checkNewRoomType(tempRight);
                roomR = ableToDiscoveredRoom(tempRight, myGame);
                GameScreen.colorTileAtCoordinate(tempRight, roomR, name + "_R.0");
                roomR.setActualPicture(name + "_R.0");
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
        if(rooms.get(coordinate).equals(Constants.RoomType.HARD_ROOK_TYPE) || rooms.get(coordinate).equals(Constants.RoomType.LIGHT_ROOK_TYPE))
            return true;
        else
            return false;
    }

    public void checkWinGame(){
        if(getExitRoom().getBuildUp().equals(Constants.Buildings.ELEVATOR))
            GameScreen.setGameWin(true);
    }

    public int countBuildingsWinGame(){
        int i = 1;
        List<Coordinate> coordinates = new ArrayList<>();
        Map<Coordinate, Room> rooms = getRooms();

        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if (entry.getValue().getType().equals(Constants.RoomType.ROOM_TO_ARRANGE) && !entry.getValue().getBuildUp().equals(Constants.Buildings.NOTHING)) {
                i++;
            }
        }
        return i;
    }

    public int countAirPumpsAtLevel(int yLevel) {
        int count = 0;
        Map<Coordinate, Room> rooms = getRooms();
        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if (room.getCoordinates().getY() == yLevel && room.getBuildUp().equals(Constants.Buildings.AIR_PUMP)) {
                count++;
            }
        }
        return count;
    }

}
