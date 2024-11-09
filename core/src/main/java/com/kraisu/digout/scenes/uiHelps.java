package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.managers.EquipmentManager;
import com.kraisu.digout.managers.ResourceManager;
import com.kraisu.digout.survivor.Survivor;

import java.util.LinkedHashMap;
import java.util.Map;


public class uiHelps {

    public enum Mark{
        INFO,
        ERROR,
        WARNING;
    }

    public static void displayInfoBox(Stage stage, String message, Mark mark) {
        Table table = new Table();
        float boxWidth = Gdx.graphics.getWidth() / 3f;
        float boxHeight = Gdx.graphics.getHeight() / 7f;
        table.setSize(boxWidth, boxHeight);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        Label description = new Label(message, DigOutGame.skin.get(getFontStyleForMark(mark), Label.LabelStyle.class));
        description.setWrap(true);
        description.setAlignment(Align.center);

        table.add(description).expand().fill().pad(10f);

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.removeActor()));
            }
        });

        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth() / 2f, Gdx.graphics.getHeight() / 11f);

        table.addAction(Actions.sequence(Actions.delay(5), Actions.fadeOut(0.5f), Actions.removeActor()));

    }


    private static String getFontStyleForMark(Mark mark) {
        switch (mark) {
            case INFO:
                return "infoFont";
            case ERROR:
                return "errorFont";
            case WARNING:
                return "warnFont";
            default:
                return "infoFont";
        }
    }

    public interface ConfirmCallback {
        void onResult(boolean confirmed);
    }

    public static void displayConfirmBox(Stage stage, String message, ConfirmCallback callback) {
        Table table = new Table();
        float boxWidth = Gdx.graphics.getWidth() / 5f;
        float boxHeight = Gdx.graphics.getHeight() / 6f;
        table.setSize(boxWidth, boxHeight);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        Label description = new Label(message, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        description.setWrap(true);
        description.setAlignment(Align.center);

        table.add(description).expand().fill().pad(10f).colspan(2).center().row();

        Button okButton = new TextButton("Confirm", DigOutGame.skinButton.get("small-green", TextButton.TextButtonStyle.class));
        Button cancelButton = new TextButton("Cancel", DigOutGame.skinButton.get("small-red", TextButton.TextButtonStyle.class));

        table.add(okButton).width(100).height(30).pad(5);
        table.add(cancelButton).width(100).height(30).pad(5);

        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onResult(true);
                table.remove();
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onResult(false);
                table.remove();
            }
        });

        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - table.getHeight() / 2f);
    }



    public static Table tableAddEq(Survivor survivor, EquipmentManager equipmentManager, Stage stage, Button addEq, ResourceManager resourceManager) {
        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.setVisible(false);  // Ukrycie tabeli po kliknięciu "X"
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().colspan(5);
        table.row();

        LinkedHashMap<Constants.Equipment, Integer> equipmentStatus = equipmentManager.getEquipmentStatus();
        LinkedHashMap<Constants.Equipment, String> equipmentNames = equipmentManager.getEquipmentNames();

        for (Map.Entry<Constants.Equipment, Integer> entry : equipmentStatus.entrySet()) {
            Constants.Equipment equipment = entry.getKey();
            Integer amount = entry.getValue();

            Image equipmentIcon = new Image(new Texture(Gdx.files.internal("assets/equipment/" + equipment.toString() + "_icon_64.png")));
            equipmentIcon.setSize(64, 64);
            table.add(equipmentIcon).size(64, 64).pad(5);
        }

        Image toolIcon = new Image(new Texture(Gdx.files.internal("assets/resources/TOOLS_icon_64.png")));
        toolIcon.setSize(64,64);
        table.add(toolIcon).size(64,64).pad(5);

        table.row();

        for (Map.Entry<Constants.Equipment, Integer> entry : equipmentStatus.entrySet()) {
            Constants.Equipment equipment = entry.getKey();
            Integer amount = entry.getValue();

            TextButton addButton = new TextButton("Add", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));

            if (amount == 0 || survivor.getEquipment() != null) {
                addButton.setDisabled(true);
            } else {
                addButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        displayConfirmBox(stage, "Do you want to assign this equipment?", confirmed -> {
                            if (confirmed) {
                                equipmentManager.getEquipment(equipment).setAllocatedAmount(1);
                                equipmentManager.getEquipment(equipment).consumeAllocatedEquipment();
                                survivor.setEquipment(equipmentManager.getEquipment(equipment));
                                addButton.setDisabled(true);
                                GameScreen.addEqTable.clear();
                                GameScreen.needsRefreshAfterAddEQ = true;
                            }
                        });
                    }
                });
            }

            table.add(addButton).height(32).width(64).pad(5);
        }

        TextButton addToolButton = new TextButton("Add", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));

        if (resourceManager.getResource(Constants.Resources.TOOLS).getTotalAmount() == 0 || survivor.getProfession() == Constants.Survivors.WORKER || survivor.getProfession() == Constants.Survivors.MINER) {
            addToolButton.setDisabled(true);
        } else {
            addToolButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    displayConfirmBox(stage, "Do you want to rebrand a survivor as a Worker?", confirmed -> {
                        if (confirmed) {
                            resourceManager.getResource(Constants.Resources.TOOLS).setAllocatedAmount(1);
                            resourceManager.getResource(Constants.Resources.TOOLS).consumeAllocatedResources();
                            survivor.setProfession(Constants.Survivors.WORKER);
                            addToolButton.setDisabled(true);
                            GameScreen.addEqTable.clear();
                            GameScreen.needsRefreshAfterAddEQ = true;
                        }
                    });
                }
            });
        }

        table.add(addToolButton).height(32).width(64).pad(5);

        addEq.setDisabled(false);
        return table;
    }



}
