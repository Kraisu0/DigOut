package com.kraisu.digout.genertor;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.BaseRoom;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.ExitRoom;

import java.util.*;

import static com.kraisu.digout.help.ConstantsGenerator.MaterialsDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EQDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EquipmentDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.FoodDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.ProfessionDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.SurvivorsDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.ToolsDropPercentages.*;
import static com.kraisu.digout.logs.DateLogs.logs;

public class Generators {
    static long seed = System.nanoTime();
    private static final Random random = new Random(seed);

    private static final int cr_range_drop = TRIPLE_CR_DROP + DOUBLE_CR_DROP + SINGLE_CR_DROP + NO_CR_DROP;
    private static final int food_range_drop = FOOD_DROP + NO_FOOD_DROP;
    private static final int tools_range_drop = TOOLS_DROP + NO_TOOLS_DROP;
    private static final int survivors_range_drop = SURVIVOR_DROP + NO_SURVIVOR_DROP;
    private static final int profession_range_drop = ENGINEER_DROP + COOK_DROP + WORKER_DROP + UNTRAINED_DROP;
    private static final int equipment_range_drop = EQ_DROP + NO_EQ_DROP;
    private static final int eq_range_drop = SEARCHLIGHT_DROP + KITCHEN_ROBOT + OXYGEN_MASK;


    public static int generateRandomNumber(int min, int max) {
        if(min > max){
            throw new IllegalArgumentException("Error Range: (min > max)");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public static int generateRandomCR(){
        int nr = generateRandomNumber(1, cr_range_drop);

        if(nr <= TRIPLE_CR_DROP)
            return 3;
        else if(nr <= TRIPLE_CR_DROP + DOUBLE_CR_DROP)
            return 2;
        else if(nr <= TRIPLE_CR_DROP + DOUBLE_CR_DROP + SINGLE_CR_DROP)
            return 1;
        else
            return 0;
    }

    public static int generateRandomFood(){
        int nr = generateRandomNumber(1, food_range_drop);

        if(nr <= FOOD_DROP)
            return 1;
        else
            return 0;
    }

    public static int generateRandomTools(){
        int nr = generateRandomNumber(1, tools_range_drop);

        if(nr <= TOOLS_DROP)
            return 1;
        else
            return 0;
    }

    public static Constants.Survivors generateRandomSurvivor(){
        int nr = generateRandomNumber(1, survivors_range_drop);

        if(nr <= SURVIVOR_DROP){
            return generateRandomProfession();
        }else
            return null;
    }

    public static Constants.Survivors generateRandomProfession(){
        int nr = generateRandomNumber(1, profession_range_drop);

        if(nr <= ENGINEER_DROP)
            return Constants.Survivors.ENGINEER;
        else if(nr <= ENGINEER_DROP + COOK_DROP)
            return Constants.Survivors.COOK;
        else if(nr <= ENGINEER_DROP + COOK_DROP + WORKER_DROP)
            return Constants.Survivors.WORKER;
        else
            return Constants.Survivors.UNTRAINED;
    }

    public static int generateRandomEquipment(){
        int nr = generateRandomNumber(1, equipment_range_drop);

        if(nr <= EQ_DROP)
            return generateRandomEQ();
        else
            return -1;
    }

    private static int generateRandomEQ() {
        int nr = generateRandomNumber(1, eq_range_drop);

        if(nr <= SEARCHLIGHT_DROP)
            return 0;
        else if(nr <= SEARCHLIGHT_DROP + KITCHEN_ROBOT)
            return 1;
        else
            return 2;
    }

    public static BaseRoom generateBaseRoom(UUID gameId){
        Coordinate coordinate = new Coordinate(generateRandomNumber(1,10), 1);
        logs(DateLogs.LogType.INFO, gameId, "Coordinates of Base room [" + coordinate.getX() + ", " + coordinate.getY() + "]" , null);
        return new BaseRoom(coordinate,gameId);
    }

    public static ExitRoom generateExitRoom(UUID gameId) {
        Coordinate coordinate = new Coordinate(generateRandomNumber(1,10), 10);
        logs(DateLogs.LogType.INFO, gameId, "Coordinates of Exit room [" + coordinate.getX() + ", " + coordinate.getY() + "]" , null);
        return new ExitRoom(coordinate,gameId);
    }

}
