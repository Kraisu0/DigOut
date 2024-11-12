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
        EXIT_TYPE,
        ROOM_TO_ARRANGE;
    }

    public enum Resources{
        MATERIALS,
        FOOD,
        TOOLS,
        ELECTRICITY;
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
        public static BuildingPrice RESTROOM_PRICE = new BuildingPrice(4,0,0,1,1,false);
        public static BuildingPrice KITCHEN_PRICE = new BuildingPrice(2,0,2,1,1,false);
        public static BuildingPrice ELEVATOR_PRICE = new BuildingPrice(3,0,0,1,1,false);
        public static BuildingPrice WORKSHOP_PRICE = new BuildingPrice(3,1,0,2,1,false);
        public static BuildingPrice POWER_STATION_PRICE = new BuildingPrice(4,3,0,2,1,false);
        public static BuildingPrice AIR_PUMP_PRICE = new BuildingPrice(3,1,4,2,1,true);
        public static BuildingPrice TINKER_ROOM_PRICE = new BuildingPrice(6,1,1,2,2,true);
    }

    public static class EquipmentPrices{
        public static final EquipmentPrice SEARCHLIGHT_PRICE = new EquipmentPrice(1,3,0,1);
        public static final EquipmentPrice KITCHEN_ROBOT_PRICE = new EquipmentPrice(0,3,2,1);
        public static final EquipmentPrice OXYGEN_MASK_PRICE = new EquipmentPrice(0,3,1,1);
        public static final EquipmentPrice PICKAXE_PRICE = new EquipmentPrice(2,4,0,1);
        public static final EquipmentPrice TOOLS_PRICE = new EquipmentPrice(1,0,0,1);
    }

    public enum GameState{
        GAME_IN_PROGRESS,
        GAME_OVER,
        GAME_WON;
    }

    public static class SurvivorLimitations{
        public static final int MAX_SURVIVOR_ENERGY = 4;
        public static final int MIN_SURVIVOR_ENERGY = 0;
    }

    public enum Tasks{
        WAIT,
        TRAIN,
        TRAIN_TO_COOK,
        TRAIN_TO_ENGINEER,
        REST,
        EAT,
        CREAT,
        CREAT_FOOD,
        CREAT_TOOLS,
        CREAT_SEARCHLIGHT,
        CREAT_KITCHEN_ROBOT,
        CREAT_OXYGEN_MASK,
        CREAT_PICKAXE,
        BUILD,
        BUILD_KITCHEN,
        BUILD_RESTROOM,
        BUILD_ELEVATOR,
        BUILD_WORKSHOP,
        BUILD_POWER_STATION,
        BUILD_AIR_PUMP,
        BUILD_TINKER_ROOM,
        DIG_OUT;
    }
}
