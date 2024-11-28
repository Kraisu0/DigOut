package com.kraisu.digout.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantsGenerator {

    public static class MaterialsDropPercentages{
        public static final int TRIPLE_CR_DROP = 10;
        public static final int DOUBLE_CR_DROP = 30;
        public static final int SINGLE_CR_DROP = 40;
        public static final int NO_CR_DROP = 20;
    }

    public static class FoodDropPercentages{
        public static final int FOOD_DROP = 20;
        public static final int NO_FOOD_DROP = 80;
    }

    public static class ToolsDropPercentages{
        public static final int TOOLS_DROP = 20;
        public static final int NO_TOOLS_DROP = 80;
    }

    public static class SurvivorsDropPercentages{ //TODO zmienić na jakieś 30/70 przed wypuszczeniem gry
        public static final int SURVIVOR_DROP = 5;
        public static final int NO_SURVIVOR_DROP = 95;
    }

    public static class ProfessionDropPercentages{
        public static final int ENGINEER_DROP = 10;
        public static final int COOK_DROP = 15;
        public static final int WORKER_DROP = 25;
        public static final int UNTRAINED_DROP = 50;
    }

    public static class EquipmentDropPercentages{
        public static final int EQ_DROP = 2;
        public static final int NO_EQ_DROP = 98;
    }

    public static class EQDropPercentages{
        public static final int SEARCHLIGHT_DROP = 33;
        public static final int KITCHEN_ROBOT = 33;
        public static final int OXYGEN_MASK = 33;
    }

    public static class JsonData {
        public static final List<String> names = new ArrayList<>();
        public static final Map<String, List<String>> descriptions = new HashMap<>();
    }

}
