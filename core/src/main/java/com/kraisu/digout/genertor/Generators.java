package com.kraisu.digout.genertor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.kraisu.digout.survivor.Survivor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

    public int generateRandomTools(){
        int nr = generateRandomNumber(1, tools_range_drop);

        if(nr <= TOOLS_DROP)
            return 1;
        else
            return 0;
    }

    public int generateRandomSurvivors(){
        int nr = generateRandomNumber(1, survivors_range_drop);

        if(nr <= SURVIVOR_DROP){
            return generateRandomProfession();
        }else
            return -1;
    }

    public int generateRandomProfession(){
        int nr = generateRandomNumber(1, profession_range_drop);

        if(nr <= ENGINEER_DROP)
            return 3;
        else if(nr <= ENGINEER_DROP + COOK_DROP)
            return 2;
        else if(nr <= ENGINEER_DROP + COOK_DROP + WORKER_DROP)
            return 1;
        else
            return 0;
    }

    public int generateRandomEquipment(){
        int nr = generateRandomNumber(1, equipment_range_drop);

        if(nr <= EQ_DROP)
            return generateRandomEQ();
        else
            return -1;
    }

    private int generateRandomEQ() {
        int nr = generateRandomNumber(1, eq_range_drop);

        if(nr <= SEARCHLIGHT_DROP)
            return 0;
        else if(nr <= SEARCHLIGHT_DROP + KITCHEN_ROBOT)
            return 1;
        else
            return 2;
    }

    private List<Survivor> generateRandomSurvivals(UUID id, int loop){
        List<Survivor> list = new ArrayList<Survivor>();
        int survivor = 0;
        list = null;

        for(int i = 0; i < loop; i++){
            survivor = generateRandomSurvivors();

            if(survivor != -1) {
                Survivor temp = new Survivor(id, "name", 4, survivor, -1, "profileInformation", 0, null);
                temp = setRandomBio(temp);
                list.add(temp);
            }
        }
        return list;
    }

    private Map<String, String> generateNameForSurvivor(UUID gameID) {
        while(true) {
            String name = generateName();
            if(getGameById(gameID).getSurvivors.stream()
                .anyMatch(() -> survivor.getName().equals(name))) {
                return name;
            }
        }
    }

    private Map<String, String> generateName() {
        // pobieranie losowej linijki  z pliku i dodanie do mapy jako key - value
    //    key - name
      //      value - opis
    }

    private Survivor setRandomBio(Survivor survivor){
        Survivor tempSurvivor = survivor;
        Texture img = generateTexture();
        img.getTextureData();
        int age = generateAge(img);
        String name = generateName(img);
        String pi = generatePI(img);


        tempSurvivor.setAge(age);
        tempSurvivor.setName(name);
        tempSurvivor.setProfileInformation(pi);
        tempSurvivor.setImg(img);

        return tempSurvivor;
    }

    private Texture generateTexture() {
        Texture temp = new Texture("surv01_Bob_30_M.png");

        return temp;
    }


}
