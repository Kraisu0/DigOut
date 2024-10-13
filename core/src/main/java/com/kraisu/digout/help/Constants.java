package com.kraisu.digout.help;

import com.kraisu.digout.stuff.BuildingPrice;
import com.kraisu.digout.stuff.EquipmentPrice;

public class Constants {

    public enum Buildings{
         NOTHING,
         BASE,
         RESTROOM,
         KITCHEN,
         ELEVATOR,
         WORKSHOP,
         POWER_STATION,
         AIR_PUMP,
         TINKER_ROOM;
    }

    public enum RoomType{
        BASE_TYPE,
        LIGHT_ROOK_TYPE,
        HARD_ROOK_TYPE,
        EXIT_TYPE;
    }

    public enum Resources{
        CONSTRUCTION_RESOURCES,
        FOOD,
        TOOLS;
    }

    public enum Equipment{
        SEARCHLIGHT,
        KITCHEN_ROBOT,
        OXYGEN_MASK,
        PICKAXE;
    }

    public enum Survivors{
        UNTRAINED,
        WORKER,
        COOK,
        ENGINEER,
        MINER;
    }

    public static class BuildingPrices{
        public static final BuildingPrice RESTROOM_PRICE = new BuildingPrice(4,0,0,1,1,false);
        public static final BuildingPrice KITCHEN_PRICE = new BuildingPrice(2,0,2,1,1,false);
        public static final BuildingPrice ELEVATOR_PRICE = new BuildingPrice(3,0,0,1,1,false);
        public static final BuildingPrice WORKSHOP_PRICE = new BuildingPrice(3,1,0,2,1,false);
        public static final BuildingPrice POWER_STATION_PRICE = new BuildingPrice(4,3,0,2,1,false);
        public static final BuildingPrice AIR_PUMP_PRICE = new BuildingPrice(3,1,4,2,1,true);
        public static final BuildingPrice TINKER_ROOM_PRICE = new BuildingPrice(6,1,1,2,2,true);
    }

    public static class EquipmentPrices{
        public static final EquipmentPrice SEARCHLIGHT_PRICE = new EquipmentPrice(1,3,0,1);
        public static final EquipmentPrice KITCHEN_ROBOT_PRICE = new EquipmentPrice(0,3,2,1);
        public static final EquipmentPrice OXYGEN_MASK_PRICE = new EquipmentPrice(0,3,1,1);
        public static final EquipmentPrice PICKAXE_PRICE = new EquipmentPrice(2,4,0,1);
    }

    public enum GameState{
        GAME_IN_PROGRESS,
        GAME_OVER,
        GAME_WON;
    }
}
