package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.loaders.CoordinateAdapter;
import com.kraisu.digout.loaders.JsonLoader;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.managers.SurvivorManager;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.BuildingPrice;
import com.kraisu.digout.stuff.EquipmentPrice;
import com.kraisu.digout.survivor.Survivor;
import com.kraisu.digout.survivor.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.kraisu.digout.logs.DateLogs.logs;
import static com.kraisu.digout.scenes.GameScreen.*;


public class uiHelps {

    public enum Mark{
        INFO,
        ERROR,
        WARNING;
    }

    public static void displayInfoBox(Stage stage, String message, Mark mark, Runnable onCloseCallback) {
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
                table.addAction(Actions.sequence(Actions.fadeOut(0.5f),  Actions.run(onCloseCallback), Actions.removeActor()));
            }
        });

        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth() / 2f, Gdx.graphics.getHeight() / 11f);

        table.addAction(Actions.sequence(Actions.delay(5), Actions.fadeOut(0.5f), Actions.run(onCloseCallback), Actions.removeActor()));

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

        table.add(okButton).width(100).height(40).pad(5);
        table.add(cancelButton).width(100).height(40).pad(5);

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



    public static Table tableAddEq(Survivor survivor, Stage stage, MyGame myGame) {
        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().colspan(5).row();

        LinkedHashMap<Constants.Equipment, Integer> equipmentStatus = myGame.getEquipmentManager().getEquipmentStatus();
        LinkedHashMap<Constants.Equipment, String> equipmentNames = myGame.getEquipmentManager().getEquipmentNames();

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
                                myGame.getEquipmentManager().getEquipment(equipment).setAllocatedAmount(1);
                                myGame.getEquipmentManager().getEquipment(equipment).consumeAllocatedEquipment();
                                survivor.setEquipment(myGame.getEquipmentManager().getEquipment(equipment));
                                logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + survivor.getName() + " got a " + survivor.getEquipment().getName(), null);

                                if(equipment == Constants.Equipment.PICKAXE) {
                                    survivor.setProfession(Constants.Survivors.MINER);
                                    logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + survivor.getName() + " was trained as a Miner.", null);
                                }

                                addButton.setDisabled(true);
                                getAddEqAndTaskTable().clear();
                                setNeedsRefreshAfterAddEQ(true);
                                setTempSurvivor(survivor);
                            }
                        });
                    }
                });
            }

            table.add(addButton).height(32).width(64).pad(5);
        }

        TextButton addToolButton = new TextButton("Add", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));

        if (myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() == 0 || survivor.getProfession() == Constants.Survivors.WORKER || survivor.getProfession() == Constants.Survivors.MINER) {
            addToolButton.setDisabled(true);
        } else {
            addToolButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    displayConfirmBox(stage, "Do you want to rebrand a survivor as a Worker?", confirmed -> {
                        if (confirmed) {
                            myGame.getResourceManager().getResource(Constants.Resources.TOOLS).setTotalAmount(myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() - 1);
                            survivor.setProfession(Constants.Survivors.WORKER);
                            addToolButton.setDisabled(true);
                            getAddEqAndTaskTable().clear();
                            setNeedsRefreshAfterAddEQ(true);
                            setTempSurvivor(survivor);

                            logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + survivor.getName() + "was trained as a Worker.", null);
                        }
                    });
                }
            });
        }

        table.add(addToolButton).height(32).width(64).pad(5);

        return table;
    }

    public static Table addChoseTask (Stage stage, Survivor survivor, MyGame myGame) {
        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        Label info = new Label("What should the chosen survivor do?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton waitingButton = new TextButton("Waiting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip waitingTooltip = new TextTooltip(" The survivor will do nothing. He'll just sit on his ass and wait. \n\n This action can only be performed in the base or restroom.", DigOutGame.skin);
        taskBlocker(waitingButton, waitingTooltip, myGame, survivor.getProfession(), Constants.Tasks.WAIT);
        waitingTooltip.setInstant(true);
        waitingButton.addListener(waitingTooltip);

        waitingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.WAIT);
            }
        });

        TextButton trainingButton = new TextButton("Training to", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingTooltip = new TextTooltip(" The survivor will be trained for another profession. To make he more useful. \n\n You can train: in the Kitchen to become a cook or in the Workshop to become an engineer.", DigOutGame.skin);
        taskBlocker(trainingButton, trainingTooltip, myGame, survivor.getProfession(), Constants.Tasks.TRAIN);
        trainingTooltip.setInstant(true);
        trainingButton.addListener(trainingTooltip);

        trainingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                trainingTasksTable(table, stage, survivor, myGame);
            }
        });

        TextButton restingButton = new TextButton("Resting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip restingTooltip = new TextTooltip(" The survivor will rest. He needs to get some sleep before the next hard days. \n\n Sleeping restores 1 energy point. \n\n This action can only be performed in the restroom.", DigOutGame.skin);
        taskBlocker(restingButton, restingTooltip, myGame, survivor.getProfession(), Constants.Tasks.REST);
        restingTooltip.setInstant(true);
        restingButton.addListener(restingTooltip);


        restingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();

                showRoomChooser(stage, survivor, myGame, Constants.Tasks.REST);
            }
        });

        TextButton eatingButton = new TextButton("Eating", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip eatingTooltip = new TextTooltip(" The survivor must feed. These breads are probably lembas. \n\n Eating restores full energy point. \n\n For this action you will need food and a base or a free restroom.", DigOutGame.skin);
        taskBlocker(eatingButton, eatingTooltip, myGame, survivor.getProfession(), Constants.Tasks.EAT);
        eatingTooltip.setInstant(true);
        eatingButton.addListener(eatingTooltip);

        eatingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.EAT);
            }
        });

        TextButton creatingStuffButton = new TextButton("Create something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingStuffTooltip = new TextTooltip(" The survivor will create some stuff: food, tools or extra EQ. \n\n The Cook can create food, the Worker can create tools, and the Engineer can create additional EQ.", DigOutGame.skin);
        taskBlocker(creatingStuffButton, creatingStuffTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT);
        creatingStuffTooltip.setInstant(true);
        creatingStuffButton.addListener(creatingStuffTooltip);

        creatingStuffButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                creatingTasksTable(table, stage, survivor, myGame);
            }
        });

        TextButton buildingButton = new TextButton("Build something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTooltip = new TextTooltip(" The survivor will build. Let's hope nothing collapses. \n\n These actions can only be performed by the Worker and, of course, the additional resources it needs." , DigOutGame.skin);
        taskBlocker(buildingButton, buildingTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD);
        buildingTooltip.setInstant(true);
        buildingButton.addListener(buildingTooltip);

        buildingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                buildingTasksTable(table, stage, survivor, myGame);
            }
        });

        TextButton digOutingButton = new TextButton("DigOuting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip digOutingTooltip = new TextTooltip(" The survivor will search for stuff and at the same time excavate a new room. \n\n These actions can only be performed by the Worker at the lower levels, and at the two highest levels only the Miner can mine.", DigOutGame.skin);
        taskBlocker(digOutingButton, digOutingTooltip, myGame, survivor.getProfession(), Constants.Tasks.DIG_OUT);
        digOutingTooltip.setInstant(true);
        digOutingButton.addListener(digOutingTooltip);

        digOutingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.DIG_OUT);
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().row();
        table.add(info).pad(10).expandX().fillX().center().row();
        table.add(waitingButton).pad(10).expandX().fillX().center().row();
        table.add(trainingButton).pad(10).expandX().fillX().center().row();
        table.add(restingButton).pad(10).expandX().fillX().center().row();
        table.add(eatingButton).pad(10).expandX().fillX().center().row();
        table.add(creatingStuffButton).pad(10).expandX().fillX().center().row();
        table.add(buildingButton).pad(10).expandX().fillX().center().row();
        table.add(digOutingButton).pad(10).expandX().fillX().center().row();

        return table;
    }

    private static void trainingTasksTable (Table table, Stage stage, Survivor survivor, MyGame myGame) {
        table.clear();
        table.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 4f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        Label info = new Label("What profession do you want to train a survivor for?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton trainingToCookButton = new TextButton("Training to Cook", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingToCookTooltip = new TextTooltip(" The survivor will be trained to be a cook so that he can contribute to the community by cooking. \n\n This action can be performed in the Kitchen.", DigOutGame.skin);
        taskBlocker(trainingToCookButton, trainingToCookTooltip, myGame, survivor.getProfession(), Constants.Tasks.TRAIN_TO_COOK);
        trainingToCookTooltip.setInstant(true);
        trainingToCookButton.addListener(trainingToCookTooltip);

        trainingToCookButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.TRAIN_TO_COOK);
            }
        });

        TextButton trainingToEngineerButton = new TextButton("Training to Engineer", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingToEngineerTooltip = new TextTooltip(" The survivor will be trained as an engineer so that he can create useful tools. \n\n This action can be performed in the Tinker room.", DigOutGame.skin);
        taskBlocker(trainingToEngineerButton, trainingToEngineerTooltip, myGame, survivor.getProfession(), Constants.Tasks.TRAIN_TO_ENGINEER);
        trainingToEngineerTooltip.setInstant(true);
        trainingToEngineerButton.addListener(trainingToEngineerTooltip);

        trainingToEngineerButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.TRAIN_TO_ENGINEER);
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().row();
        table.add(info).pad(10).expandX().fillX().center().row();
        table.add(trainingToCookButton).pad(10).expandX().fillX().center().row();
        table.add(trainingToEngineerButton).pad(10).expandX().fillX().center().row();
    }

    private static void buildingTasksTable (Table table, Stage stage, Survivor survivor, MyGame myGame) {
        table.clear();
        table.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        Label info = new Label("What should the survivor build?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton buildingRestroomButton = new TextButton("Building Restroom", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingRestroomTooltip = new TextTooltip(" In the Resstroom, survivors can rest and eat.", DigOutGame.skin);
        taskBlocker(buildingRestroomButton, buildingRestroomTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_RESTROOM);
        buildingRestroomTooltip.setInstant(true);
        buildingRestroomButton.addListener(buildingRestroomTooltip);

        buildingRestroomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_RESTROOM);
            }
        });

        TextButton buildingKitchenButton = new TextButton("Building Kitchen", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingKitchenTooltip = new TextTooltip(" In the Kitchen, the cook can make food and survivors can train to be cooks.", DigOutGame.skin);
        taskBlocker(buildingKitchenButton, buildingKitchenTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_KITCHEN);
        buildingKitchenTooltip.setInstant(true);
        buildingKitchenButton.addListener(buildingKitchenTooltip);

        buildingKitchenButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_KITCHEN);
            }
        });

        TextButton buildingElevatorButton = new TextButton("Building Elevator", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingElevatorTooltip = new TextTooltip(" The elevator allows you to get to the next level.", DigOutGame.skin);
        taskBlocker(buildingElevatorButton, buildingElevatorTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_ELEVATOR);
        buildingElevatorTooltip.setInstant(true);
        buildingElevatorButton.addListener(buildingElevatorTooltip);

        buildingElevatorButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_ELEVATOR);
            }
        });

        TextButton buildingWorkshopButton = new TextButton("Building Workshop", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingWorkshopTooltip = new TextTooltip(" In the Workshop, Workers can create tools.", DigOutGame.skin);
        taskBlocker(buildingWorkshopButton, buildingWorkshopTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_WORKSHOP);
        buildingWorkshopTooltip.setInstant(true);
        buildingWorkshopButton.addListener(buildingWorkshopTooltip);

        buildingWorkshopButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_WORKSHOP);
            }
        });

        TextButton buildingPowerStationButton = new TextButton("Building Power Station", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingPowerStationTooltip = new TextTooltip(" The power station allows you to connect 4 buildings to electricity. \n\n Power station increases electricity level by 4.", DigOutGame.skin);
        taskBlocker(buildingPowerStationButton, buildingPowerStationTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_POWER_STATION);
        buildingPowerStationTooltip.setInstant(true);
        buildingPowerStationButton.addListener(buildingPowerStationTooltip);

        buildingPowerStationButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_POWER_STATION);
            }
        });

        TextButton buildingAirPumpButton = new TextButton("Building Air pump", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingAirPumpTooltip = new TextTooltip(" Placing the air pump on a level reduces the air requirement by 1 level." , DigOutGame.skin);
        taskBlocker(buildingAirPumpButton, buildingAirPumpTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_AIR_PUMP);
        buildingAirPumpTooltip.setInstant(true);
        buildingAirPumpButton.addListener(buildingAirPumpTooltip);

        buildingAirPumpButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_AIR_PUMP);
            }
        });

        TextButton buildingTinkerRoomButton = new TextButton("Building Tinker Room", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTinkerRoomTooltip = new TextTooltip(" In the Tinker Room, an engineer can create additional EQ and a survivor can be trained to be an engineer.", DigOutGame.skin);
        taskBlocker(buildingTinkerRoomButton, buildingTinkerRoomTooltip, myGame, survivor.getProfession(), Constants.Tasks.BUILD_TINKER_ROOM);
        buildingTinkerRoomTooltip.setInstant(true);
        buildingTinkerRoomButton.addListener(buildingTinkerRoomTooltip);

        buildingTinkerRoomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.BUILD_TINKER_ROOM);
            }
        });


        table.add(closeButton).size(30, 30).pad(5).colspan(9).top().right().row();
        table.add(info).pad(5).colspan(9).expandX().fillX().center().row();
        table.add(buildingRestroomButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.RESTROOM_PRICE, myGame);
        table.add(buildingKitchenButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.KITCHEN_PRICE, myGame);
        table.add(buildingElevatorButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.ELEVATOR_PRICE, myGame);
        table.add(buildingWorkshopButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.WORKSHOP_PRICE, myGame);
        table.add(buildingPowerStationButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.POWER_STATION_PRICE, myGame);
        table.add(buildingAirPumpButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.AIR_PUMP_PRICE, myGame);
        table.add(buildingTinkerRoomButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.TINKER_ROOM_PRICE, myGame);
        table.row().space(10);

    }

    private static void creatingTasksTable (Table table, Stage stage, Survivor survivor, MyGame myGame) {
        table.clear();
        table.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        Label info = new Label("What should the survivor create?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton creatingFoodButton = new TextButton("Creating Food", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingFoodTooltip = new TextTooltip(" Food can be made in the kitchen, from some rat or spring water.", DigOutGame.skin);
        taskBlocker(creatingFoodButton, creatingFoodTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_FOOD);
        creatingFoodTooltip.setInstant(true);
        creatingFoodButton.addListener(creatingFoodTooltip);

        creatingFoodButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_FOOD);
            }
        });

        TextButton creatingToolsButton = new TextButton("Creating Tools", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingToolsTooltip = new TextTooltip(" Tools can be made from materials.", DigOutGame.skin);
        taskBlocker(creatingToolsButton, creatingToolsTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_TOOLS);
        creatingToolsTooltip.setInstant(true);
        creatingToolsButton.addListener(creatingToolsTooltip);

        creatingToolsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_TOOLS);
            }
        });

        TextButton creatingSearchlightButton = new TextButton("Creating Searchlight", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingSearchlightTooltip = new TextTooltip(" " + myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).getDescription(), DigOutGame.skin);
        taskBlocker(creatingSearchlightButton, creatingSearchlightTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_SEARCHLIGHT);
        creatingSearchlightTooltip.setInstant(true);
        creatingSearchlightButton.addListener(creatingSearchlightTooltip);

        creatingSearchlightButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_SEARCHLIGHT);
            }
        });

        TextButton creatingKitchenRobotButton = new TextButton("Creating Kitchen Robot", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingKitchenRobotTooltip = new TextTooltip(" " + myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).getDescription(), DigOutGame.skin);
        taskBlocker(creatingKitchenRobotButton, creatingKitchenRobotTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_KITCHEN_ROBOT);
        creatingKitchenRobotTooltip.setInstant(true);
        creatingKitchenRobotButton.addListener(creatingKitchenRobotTooltip);

        creatingKitchenRobotButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_KITCHEN_ROBOT);
            }
        });

        TextButton creatingOxygenMaskButton = new TextButton("Creating Oxygen Mask", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingOxygenMaskTooltip = new TextTooltip(" " + myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).getDescription(), DigOutGame.skin);
        taskBlocker(creatingOxygenMaskButton, creatingOxygenMaskTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_OXYGEN_MASK);
        creatingOxygenMaskTooltip.setInstant(true);
        creatingOxygenMaskButton.addListener(creatingOxygenMaskTooltip);

        creatingOxygenMaskButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_OXYGEN_MASK);
            }
        });

        TextButton creatingPickaxeButton = new TextButton("Creating Pickaxe", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingPickaxeTooltip = new TextTooltip(" " + myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).getDescription(), DigOutGame.skin);
        taskBlocker(creatingPickaxeButton, creatingPickaxeTooltip, myGame, survivor.getProfession(), Constants.Tasks.CREAT_PICKAXE);
        creatingPickaxeTooltip.setInstant(true);
        creatingPickaxeButton.addListener(creatingPickaxeTooltip);

        creatingPickaxeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                getAddEqAndTaskTable().clear();
                showRoomChooser(stage, survivor, myGame, Constants.Tasks.CREAT_PICKAXE);
            }
        });


        table.add(closeButton).size(30, 30).pad(5).colspan(79).top().right().row();
        table.add(info).pad(5).colspan(7).expandX().fillX().center().row();
        table.add(creatingFoodButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        table.add(creatingToolsButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateToolsCost(table, Constants.EquipmentPrices.TOOLS_PRICE, myGame);
        table.add(creatingSearchlightButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, myGame);
        table.add(creatingKitchenRobotButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.KITCHEN_ROBOT_PRICE, myGame);
        table.add(creatingOxygenMaskButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.OXYGEN_MASK_PRICE, myGame);
        table.add(creatingPickaxeButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.PICKAXE_PRICE, myGame);
        table.row().space(10);

    }

    public static void createBuildCost(Table table, BuildingPrice buildingPrice, MyGame myGame){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));
        Image electricityImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.ELECTRICITY + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(buildingPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(buildingPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(buildingPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label electricityCost = new Label(buildingPrice.isElectricityRequired() ? "Required" : "Not Required", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(buildingPrice.getMaterials() > myGame.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(buildingPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(buildingPrice.getFood() > myGame.getResourceManager().getResource( Constants.Resources.FOOD).getTotalAmount())
            foodCost = new Label(String.valueOf(buildingPrice.getFood()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(buildingPrice.getTools() > myGame.getResourceManager().getResource( Constants.Resources.TOOLS).getTotalAmount())
            toolsCost = new Label(String.valueOf(buildingPrice.getTools()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if((buildingPrice.isElectricityRequired() ? 1 : 0) > myGame.getResourceManager().getResource( Constants.Resources.ELECTRICITY).getTotalAmount())
            electricityCost = new Label(buildingPrice.isElectricityRequired() ? "Required" : "Not Required", DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        table.add(cost).padRight(5).padLeft(10).center();
        table.add(CRImage).size(32, 32).padRight(2).center();
        table.add(CRCost).padRight(5).center();

        table.add(foodImage).size(32, 32).padRight(2).center();
        table.add(foodCost).padRight(5).center();

        table.add(toolsImage).size(32, 32).padRight(2).center();
        table.add(toolsCost).padRight(5).center();

        table.add(electricityImage).size(32, 32).padRight(2).center();
        table.add(electricityCost).padRight(10).center();

        table.row();

    }

    public static void createCreateCost(Table table, EquipmentPrice equipmentPrice, MyGame myGame){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(equipmentPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(equipmentPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getMaterials() > myGame.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(equipmentPrice.getFood() > myGame.getResourceManager().getResource( Constants.Resources.FOOD).getTotalAmount())
            foodCost = new Label(String.valueOf(equipmentPrice.getFood()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(equipmentPrice.getTools() > myGame.getResourceManager().getResource( Constants.Resources.TOOLS).getTotalAmount())
            toolsCost = new Label(String.valueOf(equipmentPrice.getTools()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        table.add(cost).padRight(5).padLeft(10).center();
        table.add(CRImage).size(32, 32).padRight(2).center();
        table.add(CRCost).padRight(5).center();

        table.add(foodImage).size(32, 32).padRight(2).center();
        table.add(foodCost).padRight(5).center();

        table.add(toolsImage).size(32, 32).padRight(2).center();
        table.add(toolsCost).padRight(5).center();

        table.row();

    }

    public static void createCreateToolsCost(Table table, EquipmentPrice equipmentPrice, MyGame myGame){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getMaterials() > myGame.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));


        table.add(cost).padRight(5).padLeft(10).center();
        table.add(CRImage).size(32, 32).padRight(2).center();
        table.add(CRCost).padRight(5).center();

        table.row();

    }

    public static void taskBlocker(TextButton button, TextTooltip tooltip, MyGame myGame, Constants.Survivors profession, Constants.Tasks tasks){
        boolean hasKitchen = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.KITCHEN);
        boolean hasRestroom = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.RESTROOM);
        boolean hasWorkshop = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.WORKSHOP);
        //boolean hasAirPump = MyGame.getRoomManager().hasBuildOfType(Constants.Buildings.AIR_PUMP);
        boolean hasTinkerRoom = myGame.getRoomManager().hasBuildOfType(Constants.Buildings.TINKER_ROOM);
        //boolean hasPowerStation = MyGame.getRoomManager().hasBuildOfType(Constants.Buildings.POWER_STATION);

        boolean hasLightRock = myGame.getRoomManager().hasRoomOfType(Constants.RoomType.LIGHT_ROOK_TYPE);
        boolean hasHardRock = myGame.getRoomManager().hasRoomOfType(Constants.RoomType.HARD_ROOK_TYPE);
        boolean hasRoomToArrange = myGame.getRoomManager().hasRoomOfType(Constants.RoomType.ROOM_TO_ARRANGE);

        switch(profession){
            case UNTRAINED:
                switch(tasks){
                    case TRAIN_TO_COOK:
                        if(!hasKitchen) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Kitchen to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case EAT:
                        if(myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can build.");
                        button.setDisabled(true);
                        break;
                    case CREAT:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers, Cooks and Engineers can create.");
                        button.setDisabled(true);
                        break;
                    case DIG_OUT:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers and Miners can dig out.");
                        button.setDisabled(true);
                        break;
                }
                break;
            case WORKER:
                switch(tasks){
                    case TRAIN_TO_COOK:
                        if(!hasKitchen) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Kitchen to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case EAT:
                        if(myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD:
                        if(!hasRoomToArrange) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no room to build infrastructure here.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_RESTROOM:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.RESTROOM_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Restroom.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_KITCHEN:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.KITCHEN_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Kitchen.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_ELEVATOR:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.ELEVATOR_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Elevator.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_WORKSHOP:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.WORKSHOP_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Workshop.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_POWER_STATION:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.POWER_STATION_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Power Station.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_AIR_PUMP:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.AIR_PUMP_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Air Pump.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_TINKER_ROOM:
                        if(isEnoughResources(myGame, null, Constants.BuildingPrices.TINKER_ROOM_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Tinker Room.");
                            button.setDisabled(true);
                        }
                        break;
                    case CREAT_FOOD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Cooks can create food.");
                        button.setDisabled(true);
                        break;
                    case CREAT_TOOLS:
                        if(!hasWorkshop){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Workshop to do this action.");
                            button.setDisabled(true);
                        }else {
                            if (isEnoughResources(myGame, Constants.EquipmentPrices.TOOLS_PRICE, null)) {
                                tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to create tools");
                                button.setDisabled(true);
                            }
                        }
                        break;
                    case CREAT_SEARCHLIGHT:
                    case CREAT_KITCHEN_ROBOT:
                    case CREAT_OXYGEN_MASK:
                    case CREAT_PICKAXE:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Engineers can create additional Equipment.");
                        button.setDisabled(true);
                        break;
                    case DIG_OUT:
                        if(!hasLightRock) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not light rocks to dig out.");
                            button.setDisabled(true);
                        }
                        break;
                }
                break;
            case COOK:
                switch(tasks){
                    case TRAIN_TO_COOK:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]A cook cannot be trained as a cook.");
                        button.setDisabled(true);
                        break;
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case EAT:
                        if(myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can build.");
                        button.setDisabled(true);
                        break;
                    case CREAT_FOOD:
                        if(!hasKitchen){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Kitchen to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case CREAT_TOOLS:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can create tools.");
                        button.setDisabled(true);
                        break;
                    case CREAT_SEARCHLIGHT:
                    case CREAT_KITCHEN_ROBOT:
                    case CREAT_OXYGEN_MASK:
                    case CREAT_PICKAXE:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Engineers can create additional Equipment.");
                        button.setDisabled(true);
                        break;
                    case DIG_OUT:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers and Miners can dig out.");
                        button.setDisabled(true);
                        break;
                }
                break;
            case ENGINEER:
                switch(tasks){
                    case TRAIN_TO_COOK:
                        if(!hasKitchen) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Kitchen to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case TRAIN_TO_ENGINEER:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]A engineer cannot be trained as a engineer.");
                        button.setDisabled(true);
                        break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case EAT:
                        if(myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can build.");
                        button.setDisabled(true);
                        break;
                    case CREAT_FOOD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Cooks can create food.");
                        button.setDisabled(true);
                        break;
                    case CREAT_TOOLS:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can create tools.");
                        button.setDisabled(true);
                        break;
                    case CREAT_SEARCHLIGHT:
                        if(!hasTinkerRoom){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }else {
                            if (isEnoughResources(myGame, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
                                tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to create Searchlight");
                                button.setDisabled(true);
                            }
                        }
                        break;
                    case CREAT_KITCHEN_ROBOT:
                        if(!hasTinkerRoom){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }else {
                            if (isEnoughResources(myGame, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
                                tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to create Kitchen Roobot");
                                button.setDisabled(true);
                            }
                        }
                        break;
                    case CREAT_OXYGEN_MASK:
                        if(!hasTinkerRoom){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }else {
                            if (isEnoughResources(myGame, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
                                tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to create Oxygen mask");
                                button.setDisabled(true);
                            }
                        }
                        break;
                    case CREAT_PICKAXE:
                        if(!hasTinkerRoom){
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }else {
                            if (isEnoughResources(myGame, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
                                tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to create Pickaxe");
                                button.setDisabled(true);
                            }
                        }
                        break;
                    case DIG_OUT:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers and Miners can dig out.");
                        button.setDisabled(true);
                        break;
                }
                break;
            case MINER:
                switch(tasks){
                    case TRAIN_TO_COOK:
                        if(!hasKitchen) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Kitchen to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                        }
                        break;
                    case EAT:
                        if(myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers can build.");
                        button.setDisabled(true);
                        break;
                    case CREAT:
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Only Workers, Cooks and Engineers can create.");
                        button.setDisabled(true);
                        break;
                    case DIG_OUT:
                        if(!hasHardRock) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not Hard rocks to dig out.");
                            button.setDisabled(true);
                        }
                        break;
                }
                break;
        }

    }

    public static boolean isEnoughResources(MyGame myGame, EquipmentPrice equipmentPrice, BuildingPrice buildingPrice){

        if (equipmentPrice != null)
        {
            if(myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() -
                myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() < equipmentPrice.getTools() ||
                myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() -
                    myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() < equipmentPrice.getFood() ||
                myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getTotalAmount() -
                    myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount() < equipmentPrice.getMaterials()
            ) return true;
        }else{
            if(myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() -
                myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() < buildingPrice.getTools() ||
                myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() -
                    myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() < buildingPrice.getFood() ||
                myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getTotalAmount() -
                    myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount() < buildingPrice.getMaterials() ||
                myGame.getResourceManager().getResource( Constants.Resources.ELECTRICITY).getTotalAmount() -
                    myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount() < (buildingPrice.isElectricityRequired() ? 1 : 0)
            ) return true;
        }
        return false;
    }

    private static void reloadResources(Survivor survivor, MyGame myGame) {
        myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
                + survivor.getTask().getCost().getMaterials());

        myGame.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount()
                + survivor.getTask().getCost().getTools());

        myGame.getResourceManager().getResource(Constants.Resources.FOOD).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount()
                + survivor.getTask().getCost().getFood());

        myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount()
                + (survivor.getTask().getCost().isElectricityRequired() ? 1 : 0));
    }

    private static void showRoomChooser(Stage stage, Survivor survivor, MyGame myGame, Constants.Tasks task){
        setIsChooseRoomVisible(true);
        getSurvivorsTable().setTouchable(Touchable.disabled);
        Table table = new Table();
        table.setSize(getCenterTable().getWidth(), Gdx.graphics.getHeight()/7f);
        table.left().bottom();
        table.setPosition(0,0);

        createBorderRooms(stage, survivor, myGame, task);

        table.setBackground(DigOutGame.skin.getDrawable("box"));
        Label label = new Label("Select the room on the map above to which you want to assign the survivor.\n" +
            "[GRAY] Rooms marked with [GREEN]GREEN[GRAY] border can be selected for this task, there is no one inside so the survivor can complete the task here.\n" +
            "Rooms marked with [ORANGE]ORANGE[GRAY] border mean that someone is already assigned to that room, you can assign the current survivor here, but the previous one will lose the assigned task.\n" +
            "The room marked with [BLUE]BLUE[GRAY] border is the BASE, you can assign more than one survivor here.\n" +
            "Rooms marked with NO border mean that the tasks the survivor has selected cannot be completed in that room.", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        label.setWrap(true);
        label.setAlignment(1);
        label.setWidth(Gdx.graphics.getWidth()*3/4f);

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
                setIsChooseRoomVisible(false);
            }
        });

        table.add(label).expandX().fillX().left().bottom().padLeft(5).padBottom(5);
        table.add(closeButton).size(30, 30).top().right();
        getOuterPinRoomTable().addActor(table);

    }

    private static void createBorderRooms(Stage stage, Survivor survivor, MyGame myGame, Constants.Tasks task){

        switch (task){
            case WAIT:
            case EAT:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(null, Constants.Buildings.RESTROOM), task);
                drawBaseRoom(myGame, stage, survivor, task);
                break;
            case REST:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(null, Constants.Buildings.RESTROOM), task);
                break;
            case TRAIN_TO_COOK:
            case CREAT_FOOD:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(null, Constants.Buildings.KITCHEN), task);
                break;
            case TRAIN_TO_ENGINEER:
            case CREAT_SEARCHLIGHT:
            case CREAT_KITCHEN_ROBOT:
            case CREAT_OXYGEN_MASK:
            case CREAT_PICKAXE:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(null, Constants.Buildings.TINKER_ROOM), task);
                break;
            case BUILD_RESTROOM:
            case BUILD_KITCHEN:
            case BUILD_WORKSHOP:
            case BUILD_POWER_STATION:
            case BUILD_AIR_PUMP:
            case BUILD_TINKER_ROOM:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(Constants.RoomType.ROOM_TO_ARRANGE, Constants.Buildings.NOTHING), task);
                break;
            case BUILD_ELEVATOR:
                if(myGame.getRoomManager().getExitRoom().isAbleToBuild())
                    drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getExitRoomToBuild(), task);
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(Constants.RoomType.ROOM_TO_ARRANGE, Constants.Buildings.NOTHING), task);
                break;
            case CREAT_TOOLS:
                drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(null, Constants.Buildings.WORKSHOP), task);
                break;
            case DIG_OUT:
                if(survivor.getProfession() == Constants.Survivors.WORKER)
                    drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(Constants.RoomType.LIGHT_ROOK_TYPE, Constants.Buildings.NOTHING), task);
                if(survivor.getProfession() == Constants.Survivors.MINER) {
                    if (myGame.getRoomManager().getExitRoom().isAbleToDiscover()) {
                        drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getExitRoomToBuild(), task);
                    }
                    drawAvailableRooms(myGame, stage, survivor, myGame.getRoomManager().getRoomsByType(Constants.RoomType.HARD_ROOK_TYPE, Constants.Buildings.NOTHING), task);
                }
                break;
        }
    }

    private static void drawAvailableRooms(MyGame myGame, Stage stage, Survivor survivor, Coordinate[] c1, Constants.Tasks task){
        for (Coordinate entry : c1) {
            String color;

            if(myGame.getRoomManager().getRoom(entry).isFull()){
                color = "ORANGE_BORDER";
            }else{
                color = "GREEN_BORDER";
            }

            Table table = new Table();
            table.setSize(roomWidth, roomHeight);
            table.setPosition(getGameTable().get(entry).getX() - 2, getGameTable().get(entry).getY() + Gdx.graphics.getHeight()/7f + 1);
            table.setBackground(DigOutGame.borderSkin.getDrawable(color));
            table.setTouchable(Touchable.enabled);

            table.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(color.equals("GREEN_BORDER")){
                        displayConfirmBox(stage, "Do you want " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                            if (confirmed) {
                                survivorGotTask(survivor, entry, task, table, myGame);
                                setIsChooseRoomVisible(false);
                            }
                        });
                    }else {
                        Survivor temp = null;
                        try {
                            temp = myGame.getSurvivorManager().whoIsInTheRoom(entry);
                            Survivor finalTemp = temp;
                            displayConfirmBox(stage, "Do you want " + temp.getName() + " to be disconnected from his task and " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                                if (confirmed) {
                                    logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + finalTemp.getName() + ", Unpin task: " + finalTemp.getTask().getTask(), null);
                                    backResources(finalTemp, myGame);


                                    finalTemp.setTask(null);
                                    finalTemp.changeImgForNotWork();
                                    myGame.getRoomManager().getRoom(entry).setAmountOfSurvivors(myGame.getRoomManager().getRoom(entry).getAmountOfSurvivors() - 1);
                                    survivorGotTask(survivor, entry, task, table, myGame);
                                    setIsChooseRoomVisible(false);
                                }
                            });
                        } catch (NullPointerException e) {
                            logs(DateLogs.LogType.ERROR, myGame.getGameId(), "Survivor that should be unpinned returns NULL", e);
                        }
                    }
                }
            });

            table.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    table.setBackground(DigOutGame.borderSkin.getDrawable("WHITELIGHT_ROOM"));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    table.setBackground(DigOutGame.borderSkin.getDrawable(color));
                }
            });


            getOuterPinRoomTable().addActor(table);
        }
    }

    private static void drawBaseRoom(MyGame myGame, Stage stage, Survivor survivor, Constants.Tasks task){
        Coordinate cb = myGame.getRoomManager().getBaseRoom().getCoordinates();
        Table table = new Table();
        table.setSize(roomWidth, roomHeight);
        table.setPosition(getGameTable().get(cb).getX() + - 2, getGameTable().get(cb).getY() + Gdx.graphics.getHeight()/7f + 1);
        table.setBackground(DigOutGame.borderSkin.getDrawable("BLUE_BORDER"));
        table.setTouchable(Touchable.enabled);
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                displayConfirmBox(stage, "Do you want " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                    if (confirmed) {
                        survivorGotTask(survivor, cb, task, table, myGame);
                        setIsChooseRoomVisible(false);
                    }
                });
            }
        });

        table.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                table.setBackground(DigOutGame.borderSkin.getDrawable("WHITELIGHT_ROOM"));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                table.setBackground(DigOutGame.borderSkin.getDrawable("BLUE_BORDER"));
            }
        });


        getOuterPinRoomTable().addActor(table);

    }

    private static BuildingPrice costOfTask(Constants.Tasks task){
        BuildingPrice blank = new BuildingPrice(0,0,0,0, 1, false);
        switch(task){
            case WAIT:
            case REST:
            case TRAIN_TO_COOK:
            case CREAT_FOOD:
            case TRAIN_TO_ENGINEER:
            case DIG_OUT:
                return blank;
            case EAT:
                return new BuildingPrice(0,0,1,0, 1, false);
            case CREAT_SEARCHLIGHT:
                return eqPriceToBuildPrice(Constants.EquipmentPrices.SEARCHLIGHT_PRICE);
            case CREAT_KITCHEN_ROBOT:
                return eqPriceToBuildPrice(Constants.EquipmentPrices.KITCHEN_ROBOT_PRICE);
            case CREAT_OXYGEN_MASK:
                return eqPriceToBuildPrice(Constants.EquipmentPrices.OXYGEN_MASK_PRICE);
            case CREAT_PICKAXE:
                return eqPriceToBuildPrice(Constants.EquipmentPrices.PICKAXE_PRICE);
            case BUILD_RESTROOM:
                return Constants.BuildingPrices.RESTROOM_PRICE;
            case BUILD_KITCHEN:
                return Constants.BuildingPrices.KITCHEN_PRICE;
            case BUILD_ELEVATOR:
                return Constants.BuildingPrices.ELEVATOR_PRICE;
            case BUILD_WORKSHOP:
                return Constants.BuildingPrices.WORKSHOP_PRICE;
            case BUILD_POWER_STATION:
                return Constants.BuildingPrices.POWER_STATION_PRICE;
            case BUILD_AIR_PUMP:
                return Constants.BuildingPrices.AIR_PUMP_PRICE;
            case BUILD_TINKER_ROOM:
                return Constants.BuildingPrices.TINKER_ROOM_PRICE;
            case CREAT_TOOLS:
                return eqPriceToBuildPrice(Constants.EquipmentPrices.TOOLS_PRICE);
        }
        return blank;
    }

    private static BuildingPrice eqPriceToBuildPrice(EquipmentPrice equipmentPrice){
        return new BuildingPrice(equipmentPrice.getMaterials(), equipmentPrice.getTools(), equipmentPrice.getFood(), equipmentPrice.getWorkingDays(), equipmentPrice.getWorkingDays(), false);
    }

    private static void survivorGotTask(Survivor survivor, Coordinate coordinate, Constants.Tasks task, Table table, MyGame myGame){
        survivor.setTask(new Task(coordinate, task, costOfTask(task), null));
        reloadResources(survivor, myGame);
        setNeedsRefreshAfterAddTask(true);
        setTempSurvivor(survivor);
        survivor.changeImgForWork();

        myGame.getRoomManager().getRoom(coordinate).setAmountOfSurvivors(myGame.getRoomManager().getRoom(coordinate).getAmountOfSurvivors() + 1);
        myGame.getRoomManager().getRoom(coordinate).updateSpace(myGame);
        myGame.getRoomManager().getRoom(coordinate).updatePicture();

        getAddEqAndTaskTable().clear();
        setNeedsRefreshAfterAddEQ(true);

        table.setTouchable(Touchable.disabled);
        getOuterPinRoomTable().clear();

        logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + survivor.getName() + " got a task: " + survivor.getTask().getTask() + " , in room: " + myGame.getRoomManager().getRoom(coordinate) + " , cords: " + coordinate.getX() + ", " + coordinate.getY(), null);
    }

    private static void actionAfterCloseByX(Table table) {
        table.setVisible(false);
        getAddEqAndTaskTable().clear();
        getOuterPinRoomTable().clear();
        getSurvivorsTable().setTouchable(Touchable.enabled);
    }

    public void doTheTasks(MyGame myGame){
        Map<String, Survivor> survivorsMap = new HashMap<>(myGame.getSurvivorManager().getSurvivors());
        for(Map.Entry<String, Survivor> entry : survivorsMap.entrySet()){
            if(entry.getValue().getTask() != null) {
                doSurvivorTask(myGame, entry.getValue());
            } else {
                continue;
            }
        }
    }


    public void doSurvivorTask(MyGame myGame, Survivor survivor){
        switch(survivor.getTask().getTask()){
            case WAIT:
            case REST:
            case EAT:
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case TRAIN_TO_COOK:
                survivor.setProfession(Constants.Survivors.COOK);
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case CREAT_FOOD:
                myGame.getResourceManager().getResource(Constants.Resources.FOOD).increaseResource(
                    survivor.getTask().getReceivedStuff().getFood()
                );

                if(survivor.getEquipment() == myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT)){
                    myGame.getResourceManager().getResource(Constants.Resources.FOOD).increaseResource(
                        survivor.getTask().getReceivedStuff().getFood()
                    );
                }

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case TRAIN_TO_ENGINEER:
                survivor.setProfession(Constants.Survivors.ENGINEER);
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case DIG_OUT:
                myGame.getResourceManager().getResource(Constants.Resources.FOOD).increaseResource(survivor.getTask().getReceivedStuff().getFood());
                myGame.getResourceManager().getResource(Constants.Resources.TOOLS).increaseResource(survivor.getTask().getReceivedStuff().getTools());
                myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).increaseResource(survivor.getTask().getReceivedStuff().getMaterials());
                myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).increaseResource(survivor.getTask().getReceivedStuff().getElectricity());
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).increaseEquipment(survivor.getTask().getReceivedStuff().getSearchlight());
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).increaseEquipment(survivor.getTask().getReceivedStuff().getOxygenMask());
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).increaseEquipment(survivor.getTask().getReceivedStuff().getKitchenRobot());
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).increaseEquipment(survivor.getTask().getReceivedStuff().getPickaxe());

                if(survivor.getTask().getReceivedStuff().getSurvivor() != null)
                    myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, survivor.getTask().getReceivedStuff().getSurvivor()));

                if(survivor.getEquipment() == myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT)){
                    myGame.getResourceManager().getResource(Constants.Resources.FOOD).increaseResource(survivor.getTask().getReceivedStuff().getFood());
                    myGame.getResourceManager().getResource(Constants.Resources.TOOLS).increaseResource(survivor.getTask().getReceivedStuff().getTools());
                    myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).increaseResource(survivor.getTask().getReceivedStuff().getMaterials());
                    myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).increaseResource(survivor.getTask().getReceivedStuff().getElectricity());
                    myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).increaseEquipment(survivor.getTask().getReceivedStuff().getSearchlight());
                    myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).increaseEquipment(survivor.getTask().getReceivedStuff().getOxygenMask());
                    myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).increaseEquipment(survivor.getTask().getReceivedStuff().getKitchenRobot());
                    myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).increaseEquipment(survivor.getTask().getReceivedStuff().getPickaxe());

                    if(survivor.getTask().getReceivedStuff().getSurvivor() != null)
                        myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, survivor.getTask().getReceivedStuff().getSurvivor()));
                }

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());

                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setDiscovered(true);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAbleToBuild(true);

                if(myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()) == myGame.getRoomManager().getExitRoom()){
                    myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("EXIT_ROOM_RTA.0");
                }else {
                    myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setType(Constants.RoomType.ROOM_TO_ARRANGE);
                    myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("ROOM_TO_ARRANGE.0");
                }

                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);
                break;
            case CREAT_SEARCHLIGHT:
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).increaseEquipment(
                    survivor.getTask().getReceivedStuff().getSearchlight()
                );
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case CREAT_KITCHEN_ROBOT:
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).increaseEquipment(
                    survivor.getTask().getReceivedStuff().getKitchenRobot()
                );
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case CREAT_OXYGEN_MASK:
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).increaseEquipment(
                    survivor.getTask().getReceivedStuff().getOxygenMask()
                );
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case CREAT_PICKAXE:
                myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).increaseEquipment(
                    survivor.getTask().getReceivedStuff().getPickaxe()
                );
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_RESTROOM:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.RESTROOM);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("RESTROOM.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_KITCHEN:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.KITCHEN);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("KITCHEN.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_ELEVATOR:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.ELEVATOR);
                if(myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()) == myGame.getRoomManager().getExitRoom()){
                    myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("EXIT_ROOM_W.0");
                }else {
                    myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("ELEVATOR.0");
                }
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_WORKSHOP:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.WORKSHOP);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("WORKSHOP.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_POWER_STATION:
                myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).increaseResource(survivor.getTask().getReceivedStuff().getElectricity());

                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.POWER_STATION);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("POWER_STATION.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_AIR_PUMP:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.AIR_PUMP);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("AIR_PUMP.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case BUILD_TINKER_ROOM:
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(0);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setBuildUp(Constants.Buildings.TINKER_ROOM);
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setActualPicture("TINKER_ROOM.0");
                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);

                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
            case CREAT_TOOLS:
                myGame.getResourceManager().getResource(Constants.Resources.TOOLS).increaseResource(
                    survivor.getTask().getReceivedStuff().getTools()
                );
                survivor.increaseEnergy(survivor.getTask().getReceivedStuff().getEnergyForSurvivor());
                break;
        }
    }

    public static void saveGame(Stage stage, MyGame myGame) {
        // Folder do zapisu
        FileHandle saveFolder = Gdx.files.local("saves/");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }

        // Tworzenie nazwy pliku
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String baseFileName = myGame.getPlayer().getName() + "-" + timeStamp;

        // Dane gry
        FileHandle gameFile = Gdx.files.local("saves/" + baseFileName + ".bin");
        try (ObjectOutputStream oos = new ObjectOutputStream(gameFile.write(false))) {
            oos.writeObject(myGame); // Zapis obiektu MyGame
        } catch (IOException e) {
            e.printStackTrace();
            displayInfoBox(stage, "Error saving game: " + e.getMessage(), uiHelps.Mark.ERROR, () -> {
                System.out.println("Error saving game!");
            });
            return;
        }

        // Dane metadanych
        FileHandle metaFile = Gdx.files.local("saves/" + baseFileName + ".meta");
        try (ObjectOutputStream oos = new ObjectOutputStream(metaFile.write(false))) {
            LoadGameScreen.SaveFileData saveFileData = new LoadGameScreen.SaveFileData(myGame.getPlayer().getName(), new Date());
            oos.writeObject(saveFileData); // Zapis danych meta
        } catch (IOException e) {
            e.printStackTrace();
            displayInfoBox(stage, "Error saving metadata: " + e.getMessage(), uiHelps.Mark.ERROR, () -> {
                System.out.println("Error saving metadata!");
            });
            return;
        }

        // Wyświetlenie potwierdzenia
        displayInfoBox(stage, "Game saved successfully to: " + baseFileName, uiHelps.Mark.INFO, () -> {
            System.out.println("Game saved successfully!");
        });
    }


    public static MyGame loadGame(String baseFileName) {
        // Odczytanie pliku gry
        FileHandle gameFile = Gdx.files.local("saves/" + baseFileName + ".bin");

        if (!gameFile.exists()) {
            System.out.println("Error: Game file not found!");
            return null;
        }

        JsonLoader.mainLoader();
        try (ObjectInputStream ois = new ObjectInputStream(gameFile.read())) {
            return (MyGame) ois.readObject(); // Odczyt MyGame
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LoadGameScreen.SaveFileData readMetaFile(String baseFileName) {
        FileHandle metaFile = Gdx.files.local("saves/" + baseFileName + ".meta");

        if (!metaFile.exists()) {
            System.out.println("Error: Metadata file not found!");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(metaFile.read())) {
            return (LoadGameScreen.SaveFileData) ois.readObject(); // Odczyt SaveFileData
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }




}
