package com.kraisu.digout.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.scenes.GameScreen;
import com.kraisu.digout.stuff.Diary;
import com.kraisu.digout.survivor.Survivor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DiaryManager implements Serializable {

    private Map<Integer, String> diaries;

    public DiaryManager() {
        diaries = new HashMap<Integer, String>();
    }

    public void makeDiaryEntryPerDay(MyGame myGame, Diary diary){
        String text = "";

        text = "[BROWN]Day: " + myGame.getRound() + "[BLACK]\n\n";

        Map<Survivor, String> diaryMap = diary.getDiaryEntries();
        for(Map.Entry<Survivor, String> entry : diaryMap.entrySet()){
            text += entry.getValue();
        }

        text +=diary.writeCostAndStuff();

        text += diary.writeDeadSurvivors(myGame);

        text += "\n\n\n";

        diaries.put(myGame.getRound(), text);
    }

    public String updateDiaryBox(){
        String text = "";
        Map<Integer, String> diariesMap = diaries;
        for(Map.Entry<Integer, String> entry : diaries.entrySet()){
            text += entry.getValue();
        }
        return text;
    }

    public static void displaySumBox(Stage stage, Diary diary, MyGame myGame) {
        Table table = new Table();
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.remove();
                GameScreen.setXWasClicked(true);
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().row();

        Label name = new Label("What was found and created during this day:", DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        name.setAlignment(Align.center);

        table.add(name).pad(10).expandX().fillX().center().row();

        if(diary.getAllReceivedStuff().getMaterials() > 0){
            Table materialsTable = new Table();
            Image materialsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));
            Label materialsValue = new Label(diary.getAllReceivedStuff().getMaterials() + " - MATERIALS", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            materialsTable.add(materialsImage).size(32, 32).padRight(2).center();
            materialsTable.add(materialsValue).padRight(5).center();
            table.add(materialsTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getFood() > 0){
            Table foodTable = new Table();
            Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
            Label foodValue = new Label(diary.getAllReceivedStuff().getFood() + " - FOOD", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            foodTable.add(foodImage).size(32, 32).padRight(2).center();
            foodTable.add(foodValue).padRight(5).center();
            table.add(foodTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getTools() > 0){
            Table toolsTable = new Table();
            Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));
            Label toolsValue = new Label(diary.getAllReceivedStuff().getTools() + " - TOOLS", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            toolsTable.add(toolsImage).size(32, 32).padRight(2).center();
            toolsTable.add(toolsValue).padRight(5).center();
            table.add(toolsTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getElectricity() > 0){
            Table electricityTable = new Table();
            Image electricityImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.ELECTRICITY + "_icon_64.png")));
            Label electricityValue = new Label(diary.getAllReceivedStuff().getElectricity() + " - ELECTRICITY", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            electricityTable.add(electricityImage).size(32, 32).padRight(2).center();
            electricityTable.add(electricityValue).padRight(5).center();
            table.add(electricityTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getSearchlight() > 0){
            Table searchlightTable = new Table();
            Image searchlightImage = new Image(new Texture(Gdx.files.internal("assets/equipment/" + Constants.Equipment.SEARCHLIGHT + "_icon_64.png")));
            Label searchlightValue = new Label(diary.getAllReceivedStuff().getSearchlight() + " - SEARCHLIGHT", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            searchlightTable.add(searchlightImage).size(32, 32).padRight(2).center();
            searchlightTable.add(searchlightValue).padRight(5).center();
            table.add(searchlightTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getKitchenRobot() > 0){
            Table kitchenRobotTable = new Table();
            Image kitchenRobotImage = new Image(new Texture(Gdx.files.internal("assets/equipment/" + Constants.Equipment.KITCHEN_ROBOT + "_icon_64.png")));
            Label kitchenRobotValue = new Label(diary.getAllReceivedStuff().getKitchenRobot() + " - KITCHEN_ROBOT", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            kitchenRobotTable.add(kitchenRobotImage).size(32, 32).padRight(2).center();
            kitchenRobotTable.add(kitchenRobotValue).padRight(5).center();
            table.add(kitchenRobotTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getOxygenMask() > 0){
            Table oxygenMaskTable = new Table();
            Image oxygenMaskImage = new Image(new Texture(Gdx.files.internal("assets/equipment/" + Constants.Equipment.OXYGEN_MASK + "_icon_64.png")));
            Label oxygenMaskValue = new Label(diary.getAllReceivedStuff().getOxygenMask() + " - OXYGEN_MASK", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            oxygenMaskTable.add(oxygenMaskImage).size(32, 32).padRight(2).center();
            oxygenMaskTable.add(oxygenMaskValue).padRight(5).center();
            table.add(oxygenMaskTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getAllReceivedStuff().getPickaxe() > 0){
            Table pickaxeTable = new Table();
            Image pickaxeImage = new Image(new Texture(Gdx.files.internal("assets/equipment/" + Constants.Equipment.PICKAXE + "_icon_64.png")));
            Label pickaxeValue = new Label(diary.getAllReceivedStuff().getPickaxe() + " - PICKAXE", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            pickaxeTable.add(pickaxeImage).size(32, 32).padRight(2).center();
            pickaxeTable.add(pickaxeValue).padRight(5).center();
            table.add(pickaxeTable).pad(10).expandX().fillX().center().row();
        }

        if(diary.getNewSurvivors() > 0){
            if(diary.getNewSurvivors() == 1) {
                Table survivorsTable = new Table();
                Label survivorLabel = new Label("FOUNDED: " + diary.getNewSurvivors() + " NEW SURVIVOR", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
                survivorsTable.add(survivorLabel).padRight(5).center();
                table.add(survivorsTable).pad(10).expandX().fillX().center().row();
            }else{
                Table survivorsTable = new Table();
                Label survivorLabel = new Label("FOUNDED: " + diary.getNewSurvivors() + " NEW SURVIVORS", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
                survivorsTable.add(survivorLabel).padRight(5).center();
                table.add(survivorsTable).pad(10).expandX().fillX().center().row();
            }
        }

        String text = "";
        int s = 0;
        Map<String, Survivor> survivorsMap = myGame.getSurvivorManager().getSurvivors();
        for (Map.Entry<String, Survivor> entry : survivorsMap.entrySet()) {
            if (entry.getValue().getEnergy() == 0) {
                text += " " + entry.getValue().getName() + ",";
                s++;
            }
        }


        if(s > 0){
            Table deadSurvivorsTable = new Table();
            text = text.substring(0, text.length() - 1);
            Label deadSurvivorLabel = new Label("DIED:" + text, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            deadSurvivorsTable.add(deadSurvivorLabel).padRight(5).center();
            table.add(deadSurvivorsTable).pad(10).expandX().fillX().center().colspan(2).row();
        }

        table.pack();
        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth()/2, Gdx.graphics.getHeight() / 3f + table.getHeight()/2);
    }




}
