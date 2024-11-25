package com.kraisu.digout.stuff;

import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.survivor.Survivor;

import java.util.HashMap;
import java.util.Map;

import static com.kraisu.digout.stuff.DiaryEntry.entryByTasks;

public class Diary {

    private Map<Survivor, String> diaryEntries;

    private String diary;
    private ReceivedStuff allReceivedStuff;
    private ReceivedStuff allCosts;
    private int newSurvivors;


    public Diary() {
        this.diaryEntries = new HashMap<Survivor, String>();
        this.allCosts = new ReceivedStuff(0,0,0,0,0,0,0,0,0,null);
        this.allReceivedStuff = new ReceivedStuff(0,0,0,0,0,0,0,0,0,null);
        this.newSurvivors = 0;
    }

    public Map<Survivor, String> getDiaryEntries() {
        return diaryEntries;
    }

    public void setDiaryEntries(Map<Survivor, String> diaryEntries) {
        this.diaryEntries = diaryEntries;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

    public ReceivedStuff getAllReceivedStuff() {
        return allReceivedStuff;
    }

    public void setAllReceivedStuff(ReceivedStuff allReceivedStuff) {
        this.allReceivedStuff = allReceivedStuff;
    }

    public ReceivedStuff getAllCosts() {
        return allCosts;
    }

    public void setAllCosts(ReceivedStuff allCosts) {
        this.allCosts = allCosts;
    }

    public int getNewSurvivors() {
        return newSurvivors;
    }

    public void setNewSurvivors(int newSurvivors) {
        this.newSurvivors = newSurvivors;
    }

    public void makeEntryForAllSurvivors(MyGame myGame){
        Map<String, Survivor> survivorsMap = myGame.getSurvivorManager().getSurvivors();
        for(Map.Entry<String, Survivor> entry : survivorsMap.entrySet()){
            if(entry.getValue().getTask() != null) {
                makeEntry(entry.getValue());
            }else{
                continue;
            }
        }
    }

    public void makeEntry(Survivor survivor){
        String entry = entryByTasks(survivor);
        diaryEntries.put(survivor, entry);
        addCosts(survivor);
        addReceivedStuff(survivor);
    }

    public void addCosts(Survivor survivor){
        getAllCosts().materials += survivor.getTask().getCost().materials;
        getAllCosts().tools += survivor.getTask().getCost().tools;
        getAllCosts().food += survivor.getTask().getCost().food;
        getAllCosts().electricity += (survivor.getTask().getCost().isElectricityRequired? 1 : 0) ;
    }

    public void addReceivedStuff(Survivor survivor){
        getAllReceivedStuff().materials += survivor.getTask().getReceivedStuff().materials;
        getAllReceivedStuff().tools += survivor.getTask().getReceivedStuff().tools;
        getAllReceivedStuff().food += survivor.getTask().getReceivedStuff().food;
        getAllReceivedStuff().electricity += survivor.getTask().getReceivedStuff().electricity;
        getAllReceivedStuff().searchlight += survivor.getTask().getReceivedStuff().searchlight;
        getAllReceivedStuff().kitchenRobot += survivor.getTask().getReceivedStuff().kitchenRobot;
        getAllReceivedStuff().oxygenMask += survivor.getTask().getReceivedStuff().oxygenMask;
        getAllReceivedStuff().pickaxe += survivor.getTask().getReceivedStuff().pickaxe;

        if(survivor.getTask().getReceivedStuff().survivor != null){
            newSurvivors += 1;
        }
    }

    public String writeCostAndStuff(){
        String text = "";
        if(areThereAnyCosts()){
            text += "\nCosts of the day: ";
            if(getAllCosts().materials != 0)
                text += getAllCosts().materials + "-MATERIALS,";
            if(getAllCosts().food != 0)
                text += getAllCosts().food + "-FOOD,";
            if(getAllCosts().tools != 0)
                text += getAllCosts().tools + "-TOOLS,";
            if(getAllCosts().searchlight != 0)
                text += getAllCosts().searchlight + "-SEARCHLIGHT,";
            if(getAllCosts().oxygenMask != 0)
                text += getAllCosts().oxygenMask + "-OXYGEN MASK,";
            if(getAllCosts().kitchenRobot != 0)
                text += getAllCosts().kitchenRobot + "-KITCHEN ROBOT,";

            text = text.substring(0, text.length() - 1);
        }
        if(areThereAnyReceivedStuff()){
            text += "\nReceived stuff of the day: ";
            if(getAllReceivedStuff().materials != 0)
                text += getAllReceivedStuff().materials + "-MATERIALS,";
            if(getAllReceivedStuff().food != 0)
                text += getAllReceivedStuff().food + "-FOOD,";
            if(getAllReceivedStuff().tools != 0)
                text += getAllReceivedStuff().tools + "-TOOLS,";
            if(getAllReceivedStuff().searchlight != 0)
                text += getAllReceivedStuff().searchlight + "-SEARCHLIGHT,";
            if(getAllReceivedStuff().oxygenMask != 0)
                text += getAllReceivedStuff().oxygenMask + "-OXYGEN MASK,";
            if(getAllReceivedStuff().kitchenRobot != 0)
                text += getAllReceivedStuff().kitchenRobot + "-KITCHEN ROBOT,";
            if(newSurvivors != 0) {
                text += " and found " + newSurvivors;
                if(newSurvivors == 1)
                    text += " Survivor,";
                else
                    text += " Survivors,";
            }

            text = text.substring(0, text.length() - 1);
            text += ".\n";
        }

        return text;
    }

    private boolean areThereAnyCosts(){
        if(getAllCosts().materials == 0 && getAllCosts().food == 0 &&
            getAllCosts().tools == 0 && getAllCosts().electricity == 0 &&
            getAllCosts().searchlight == 0 && getAllCosts().kitchenRobot == 0 &&
            getAllCosts().oxygenMask == 0 && getAllCosts().pickaxe == 0)
            return false;
        else
            return true;
    }

    private boolean areThereAnyReceivedStuff(){
        if(getAllReceivedStuff().materials == 0 && getAllReceivedStuff().food == 0 &&
            getAllReceivedStuff().tools == 0 && getAllReceivedStuff().electricity == 0 &&
            getAllReceivedStuff().searchlight == 0 && getAllReceivedStuff().kitchenRobot == 0 &&
            getAllReceivedStuff().oxygenMask == 0 && getAllReceivedStuff().pickaxe == 0 && getNewSurvivors() == 0)
            return false;
        else
            return true;
    }

    public String writeDeadSurvivors(MyGame myGame) {
        String text = "";
        int s = 0;
        text = "\n\nUnfortunately, they did not survive that day:";
        Map<String, Survivor> survivorsMap = myGame.getSurvivorManager().getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getEnergy() == 0) {
                text += " " + entry.getValue().getName() + ",";
                s++;
            }
        }

        if(s == 0)
            text = "\n\nFortunately, no one died that day.";
        else
            text = text.substring(0, text.length() - 1);

        return text;
    }

}
