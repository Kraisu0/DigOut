package com.kraisu.digout.genertor;

import java.util.Random;

import static com.kraisu.digout.help.ConstantsGenerator.ConstructionResourcesDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EQDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.EquipmentDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.FoodDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.ProfessionDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.SurvivorsDropPercentages.*;
import static com.kraisu.digout.help.ConstantsGenerator.ToolsDropPercentages.*;

public class Generators {
    static long seed = System.nanoTime();
    private static final Random random = new Random(seed);

    private final int cr_range_drop = TRIPLE_CR_DROP + DOUBLE_CR_DROP + SINGLE_CR_DROP + NO_CR_DROP;
    private final int food_range_drop = FOOD_DROP + NO_FOOD_DROP;
    private final int tools_range_drop = TOOLS_DROP + NO_TOOLS_DROP;
    private final int survivors_range_drop = SURVIVOR_DROP + NO_SURVIVOR_DROP;
    private final int profession_range_drop = ENGINEER_DROP + COOK_DROP + WORKER_DROP + UNTRAINED_DROP;
    private final int equipment_range_drop = EQ_DROP + NO_EQ_DROP;
    private final int eq_range_drop = SEARCHLIGHT_DROP + KITCHEN_ROBOT + OXYGEN_MASK;


    public int generateRandomNumber(int min, int max) {
        if(min > max){
            throw new IllegalArgumentException("Zakres niepoprawny: (min > max)");
        }
        return random.nextInt((max - min) + 1) + min;
    }

    public int generateRandomCR(){
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

    public int generateRandomFood(){
        int nr = generateRandomNumber(1, food_range_drop);

        if(nr <= FOOD_DROP)
            return 1;
        else
            return 0;
    }

}
