package com.kraisu.digout.stuff;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.io.Serializable;
import java.util.Map;

public class DiaryEntry implements Serializable {

    static String entryByTasks(Survivor survivor){

        switch(survivor.getTask().getTask()){
            case WAIT:
                return survivor.getName() + " waited in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case REST:
                return survivor.getName() + " rested in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case TRAIN_TO_COOK:
                return survivor.getName() + " trained as COOK in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case CREAT_FOOD:
                return survivor.getName() + " created FOOD in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + "and he created " + survivor.getTask().getReceivedStuff().food + "-FOOD.\n";
            case TRAIN_TO_ENGINEER:
                return survivor.getName() + " trained as ENGINEER in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case DIG_OUT:
                return survivor.getName() + " dug out room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") " + howMuchStuffRevivedEntry(survivor.getTask().getReceivedStuff()) + ".\n";
            case EAT:
                return survivor.getName() + " ate in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case CREAT_SEARCHLIGHT:
                return survivor.getName() + " created SEARCHLIGHT in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") and he created " + survivor.getTask().getReceivedStuff().searchlight + "-SEARCHLIGHT.\n";
            case CREAT_KITCHEN_ROBOT:
                return survivor.getName() + " created KITCHEN ROBOT in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") and he created " + survivor.getTask().getReceivedStuff().kitchenRobot + "-KITCHEN ROBOT.\n";
            case CREAT_OXYGEN_MASK:
                return survivor.getName() + " created OXYGEN MASK in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") and he created " + survivor.getTask().getReceivedStuff().oxygenMask + "-OXYGEN MASK.\n";
            case CREAT_PICKAXE:
                return survivor.getName() + " created PICKAXE in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") and he created " + survivor.getTask().getReceivedStuff().pickaxe + "-PICKAXE.\n";
            case BUILD_RESTROOM:
                return survivor.getName() + " built RESTROOM in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case BUILD_KITCHEN:
                return survivor.getName() + " built KITCHEN in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case BUILD_ELEVATOR:
                return survivor.getName() + " built ELEVATOR in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case BUILD_WORKSHOP:
                return survivor.getName() + " built WORKSHOP in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case BUILD_POWER_STATION:
                return survivor.getName() + " built POWER STATION in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") now Survivor have " + survivor.getTask().getReceivedStuff().electricity +
                    " more units of ELECTRICITY.\n";
            case BUILD_AIR_PUMP:
                return survivor.getName() + " built AIR PUMP in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case BUILD_TINKER_ROOM:
                return survivor.getName() + " built TINKER ROOM in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ").\n";
            case CREAT_TOOLS:
                return survivor.getName() + " created TOOLS in room: (" + survivor.getTask().getCoordinateOfRoom().getX() +
                    ", " + survivor.getTask().getCoordinateOfRoom().getY() + ") and he created " + survivor.getTask().getReceivedStuff().tools + "-TOOLS.\n";
        }
        return survivor.getName() + " was founded in rooks.";
    }

    private static String howMuchStuffRevivedEntry(ReceivedStuff receivedStuff){
        String text = "";
        text += "and found: ";
        if(receivedStuff.materials != 0)
            text += receivedStuff.materials + "-MATERIALS,";
        if(receivedStuff.food != 0)
            text += receivedStuff.food + "-FOOD,";
        if(receivedStuff.tools != 0)
            text += receivedStuff.tools + "-TOOLS,";
        if(receivedStuff.searchlight != 0)
            text += receivedStuff.searchlight + "-SEARCHLIGHT,";
        if(receivedStuff.oxygenMask != 0)
            text += receivedStuff.oxygenMask + "-OXYGEN MASK,";
        if(receivedStuff.kitchenRobot != 0)
            text += receivedStuff.kitchenRobot + "-KITCHEN ROBOT,";
        if(receivedStuff.survivor != null)
            text += " and survivor: " + receivedStuff.survivor + ",";

        text = text.substring(0, text.length() - 1);

        return text;
    }

}
