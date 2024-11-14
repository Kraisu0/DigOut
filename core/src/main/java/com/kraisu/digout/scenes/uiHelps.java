package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.BuildingPrice;
import com.kraisu.digout.stuff.EquipmentPrice;
import com.kraisu.digout.survivor.Survivor;
import com.kraisu.digout.survivor.Task;

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



    public static Table tableAddEq(Survivor survivor, Stage stage, Game game) {
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

        LinkedHashMap<Constants.Equipment, Integer> equipmentStatus = game.getEquipmentManager().getEquipmentStatus();
        LinkedHashMap<Constants.Equipment, String> equipmentNames = game.getEquipmentManager().getEquipmentNames();

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
                                game.getEquipmentManager().getEquipment(equipment).setAllocatedAmount(1);
                                game.getEquipmentManager().getEquipment(equipment).consumeAllocatedEquipment();
                                survivor.setEquipment(game.getEquipmentManager().getEquipment(equipment));
                                logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + " got a " + survivor.getEquipment().getName(), null);

                                if(equipment == Constants.Equipment.PICKAXE) {
                                    survivor.setProfession(Constants.Survivors.MINER);
                                    logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + " was trained as a Miner.", null);
                                }

                                addButton.setDisabled(true);
                                addEqAndTaskTable.clear();
                                needsRefreshAfterAddEQ = true;
                                tempSurvivor = survivor;
                            }
                        });
                    }
                });
            }

            table.add(addButton).height(32).width(64).pad(5);
        }

        TextButton addToolButton = new TextButton("Add", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));

        if (game.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() == 0 || survivor.getProfession() == Constants.Survivors.WORKER || survivor.getProfession() == Constants.Survivors.MINER) {
            addToolButton.setDisabled(true);
        } else {
            addToolButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    displayConfirmBox(stage, "Do you want to rebrand a survivor as a Worker?", confirmed -> {
                        if (confirmed) {
                            game.getResourceManager().getResource(Constants.Resources.TOOLS).setTotalAmount(game.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() - 1);
                            survivor.setProfession(Constants.Survivors.WORKER);
                            addToolButton.setDisabled(true);
                            addEqAndTaskTable.clear();
                            needsRefreshAfterAddEQ = true;
                            tempSurvivor = survivor;

                            logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + "was trained as a Worker.", null);
                        }
                    });
                }
            });
        }

        table.add(addToolButton).height(32).width(64).pad(5);

        return table;
    }

    public static Table addChoseTask (Stage stage, Survivor survivor, Game game) {
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
        taskBlocker(waitingButton, waitingTooltip, game, survivor.getProfession(), Constants.Tasks.WAIT);
        waitingTooltip.setInstant(true);
        waitingButton.addListener(waitingTooltip);

        waitingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.WAIT);
            }
        });

        TextButton trainingButton = new TextButton("Training to", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingTooltip = new TextTooltip(" The survivor will be trained for another profession. To make he more useful. \n\n You can train: in the Kitchen to become a cook or in the Workshop to become an engineer.", DigOutGame.skin);
        taskBlocker(trainingButton, trainingTooltip, game, survivor.getProfession(), Constants.Tasks.TRAIN);
        trainingTooltip.setInstant(true);
        trainingButton.addListener(trainingTooltip);

        trainingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                trainingTasksTable(table, stage, survivor, game);
            }
        });

        TextButton restingButton = new TextButton("Resting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip restingTooltip = new TextTooltip(" The survivor will rest. He needs to get some sleep before the next hard days. \n\n Sleeping restores 1 energy point. \n\n This action can only be performed in the restroom.", DigOutGame.skin);
        taskBlocker(restingButton, restingTooltip, game, survivor.getProfession(), Constants.Tasks.REST);
        restingTooltip.setInstant(true);
        restingButton.addListener(restingTooltip);


        restingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.REST);
            }
        });

        TextButton eatingButton = new TextButton("Eating", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip eatingTooltip = new TextTooltip(" The survivor must feed. These breads are probably lembas. \n\n Eating restores full energy point. \n\n For this action you will need food and a base or a free restroom.", DigOutGame.skin);
        taskBlocker(eatingButton, eatingTooltip, game, survivor.getProfession(), Constants.Tasks.EAT);
        eatingTooltip.setInstant(true);
        eatingButton.addListener(eatingTooltip);

        eatingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.EAT);
            }
        });

        TextButton creatingStuffButton = new TextButton("Create something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingStuffTooltip = new TextTooltip(" The survivor will create some stuff: food, tools or extra EQ. \n\n The Cook can create food, the Worker can create tools, and the Engineer can create additional EQ.", DigOutGame.skin);
        taskBlocker(creatingStuffButton, creatingStuffTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT);
        creatingStuffTooltip.setInstant(true);
        creatingStuffButton.addListener(creatingStuffTooltip);

        creatingStuffButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                creatingTasksTable(table, stage, survivor, game);
            }
        });

        TextButton buildingButton = new TextButton("Build something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTooltip = new TextTooltip(" The survivor will build. Let's hope nothing collapses. \n\n These actions can only be performed by the Worker and, of course, the additional resources it needs." , DigOutGame.skin);
        taskBlocker(buildingButton, buildingTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD);
        buildingTooltip.setInstant(true);
        buildingButton.addListener(buildingTooltip);

        buildingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                buildingTasksTable(table, stage, survivor, game);
            }
        });

        TextButton digOutingButton = new TextButton("DigOuting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip digOutingTooltip = new TextTooltip(" The survivor will search for stuff and at the same time excavate a new room. \n\n These actions can only be performed by the Worker at the lower levels, and at the two highest levels only the Miner can mine.", DigOutGame.skin);
        taskBlocker(digOutingButton, digOutingTooltip, game, survivor.getProfession(), Constants.Tasks.DIG_OUT);
        digOutingTooltip.setInstant(true);
        digOutingButton.addListener(digOutingTooltip);

        digOutingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.DIG_OUT);
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

    private static void trainingTasksTable (Table table, Stage stage, Survivor survivor, Game game) {
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
        taskBlocker(trainingToCookButton, trainingToCookTooltip, game, survivor.getProfession(), Constants.Tasks.TRAIN_TO_COOK);
        trainingToCookTooltip.setInstant(true);
        trainingToCookButton.addListener(trainingToCookTooltip);

        trainingToCookButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.TRAIN_TO_COOK);
            }
        });

        TextButton trainingToEngineerButton = new TextButton("Training to Engineer", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingToEngineerTooltip = new TextTooltip(" The survivor will be trained as an engineer so that he can create useful tools. \n\n This action can be performed in the Tinker room.", DigOutGame.skin);
        taskBlocker(trainingToEngineerButton, trainingToEngineerTooltip, game, survivor.getProfession(), Constants.Tasks.TRAIN_TO_ENGINEER);
        trainingToEngineerTooltip.setInstant(true);
        trainingToEngineerButton.addListener(trainingToEngineerTooltip);

        trainingToEngineerButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.TRAIN_TO_ENGINEER);
            }
        });

        table.add(closeButton).size(30, 30).pad(5).top().right().row();
        table.add(info).pad(10).expandX().fillX().center().row();
        table.add(trainingToCookButton).pad(10).expandX().fillX().center().row();
        table.add(trainingToEngineerButton).pad(10).expandX().fillX().center().row();
    }

    private static void buildingTasksTable (Table table, Stage stage, Survivor survivor, Game game) {
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
        taskBlocker(buildingRestroomButton, buildingRestroomTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_RESTROOM);
        buildingRestroomTooltip.setInstant(true);
        buildingRestroomButton.addListener(buildingRestroomTooltip);

        buildingRestroomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_RESTROOM);
            }
        });

        TextButton buildingKitchenButton = new TextButton("Building Kitchen", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingKitchenTooltip = new TextTooltip(" In the Kitchen, the cook can make food and survivors can train to be cooks.", DigOutGame.skin);
        taskBlocker(buildingKitchenButton, buildingKitchenTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_KITCHEN);
        buildingKitchenTooltip.setInstant(true);
        buildingKitchenButton.addListener(buildingKitchenTooltip);

        buildingKitchenButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_KITCHEN);
            }
        });

        TextButton buildingElevatorButton = new TextButton("Building Elevator", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingElevatorTooltip = new TextTooltip(" The elevator allows you to get to the next level.", DigOutGame.skin);
        taskBlocker(buildingElevatorButton, buildingElevatorTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_ELEVATOR);
        buildingElevatorTooltip.setInstant(true);
        buildingElevatorButton.addListener(buildingElevatorTooltip);

        buildingElevatorButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_ELEVATOR);
            }
        });

        TextButton buildingWorkshopButton = new TextButton("Building Workshop", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingWorkshopTooltip = new TextTooltip(" In the Workshop, Workers can create tools.", DigOutGame.skin);
        taskBlocker(buildingWorkshopButton, buildingWorkshopTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_WORKSHOP);
        buildingWorkshopTooltip.setInstant(true);
        buildingWorkshopButton.addListener(buildingWorkshopTooltip);

        buildingWorkshopButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_WORKSHOP);
            }
        });

        TextButton buildingPowerStationButton = new TextButton("Building Power Station", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingPowerStationTooltip = new TextTooltip(" The power station allows you to connect 4 buildings to electricity. \n\n Power station increases electricity level by 4.", DigOutGame.skin);
        taskBlocker(buildingPowerStationButton, buildingPowerStationTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_POWER_STATION);
        buildingPowerStationTooltip.setInstant(true);
        buildingPowerStationButton.addListener(buildingPowerStationTooltip);

        buildingPowerStationButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_POWER_STATION);
            }
        });

        TextButton buildingAirPumpButton = new TextButton("Building Air pump", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingAirPumpTooltip = new TextTooltip(" Placing the air pump on a level reduces the air requirement by 1 level." , DigOutGame.skin);
        taskBlocker(buildingAirPumpButton, buildingAirPumpTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_AIR_PUMP);
        buildingAirPumpTooltip.setInstant(true);
        buildingAirPumpButton.addListener(buildingAirPumpTooltip);

        buildingAirPumpButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_AIR_PUMP);
            }
        });

        TextButton buildingTinkerRoomButton = new TextButton("Building Tinker Room", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTinkerRoomTooltip = new TextTooltip(" In the Tinker Room, an engineer can create additional EQ and a survivor can be trained to be an engineer.", DigOutGame.skin);
        taskBlocker(buildingTinkerRoomButton, buildingTinkerRoomTooltip, game, survivor.getProfession(), Constants.Tasks.BUILD_TINKER_ROOM);
        buildingTinkerRoomTooltip.setInstant(true);
        buildingTinkerRoomButton.addListener(buildingTinkerRoomTooltip);

        buildingTinkerRoomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.BUILD_TINKER_ROOM);
            }
        });


        table.add(closeButton).size(30, 30).pad(5).colspan(9).top().right().row();
        table.add(info).pad(5).colspan(9).expandX().fillX().center().row();
        table.add(buildingRestroomButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.RESTROOM_PRICE, game);
        table.add(buildingKitchenButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.KITCHEN_PRICE, game);
        table.add(buildingElevatorButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.ELEVATOR_PRICE, game);
        table.add(buildingWorkshopButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.WORKSHOP_PRICE, game);
        table.add(buildingPowerStationButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.POWER_STATION_PRICE, game);
        table.add(buildingAirPumpButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.AIR_PUMP_PRICE, game);
        table.add(buildingTinkerRoomButton).pad(10).colspan(9).height(table.getHeight()/8).expandX().fillX().center().row();
        createBuildCost(table, Constants.BuildingPrices.TINKER_ROOM_PRICE, game);
        table.row().space(10);

    }

    private static void creatingTasksTable (Table table, Stage stage, Survivor survivor, Game game) {
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
        taskBlocker(creatingFoodButton, creatingFoodTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_FOOD);
        creatingFoodTooltip.setInstant(true);
        creatingFoodButton.addListener(creatingFoodTooltip);

        creatingFoodButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_FOOD);
            }
        });

        TextButton creatingToolsButton = new TextButton("Creating Tools", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingToolsTooltip = new TextTooltip(" Tools can be made from materials.", DigOutGame.skin);
        taskBlocker(creatingToolsButton, creatingToolsTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_TOOLS);
        creatingToolsTooltip.setInstant(true);
        creatingToolsButton.addListener(creatingToolsTooltip);

        creatingToolsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_TOOLS);
            }
        });

        TextButton creatingSearchlightButton = new TextButton("Creating Searchlight", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingSearchlightTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).getDescription(), DigOutGame.skin);
        taskBlocker(creatingSearchlightButton, creatingSearchlightTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_SEARCHLIGHT);
        creatingSearchlightTooltip.setInstant(true);
        creatingSearchlightButton.addListener(creatingSearchlightTooltip);

        creatingSearchlightButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_SEARCHLIGHT);
            }
        });

        TextButton creatingKitchenRobotButton = new TextButton("Creating Kitchen Robot", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingKitchenRobotTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).getDescription(), DigOutGame.skin);
        taskBlocker(creatingKitchenRobotButton, creatingKitchenRobotTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_KITCHEN_ROBOT);
        creatingKitchenRobotTooltip.setInstant(true);
        creatingKitchenRobotButton.addListener(creatingKitchenRobotTooltip);

        creatingKitchenRobotButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_KITCHEN_ROBOT);
            }
        });

        TextButton creatingOxygenMaskButton = new TextButton("Creating Oxygen Mask", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingOxygenMaskTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).getDescription(), DigOutGame.skin);
        taskBlocker(creatingOxygenMaskButton, creatingOxygenMaskTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_OXYGEN_MASK);
        creatingOxygenMaskTooltip.setInstant(true);
        creatingOxygenMaskButton.addListener(creatingOxygenMaskTooltip);

        creatingOxygenMaskButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_OXYGEN_MASK);
            }
        });

        TextButton creatingPickaxeButton = new TextButton("Creating Pickaxe", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingPickaxeTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).getDescription(), DigOutGame.skin);
        taskBlocker(creatingPickaxeButton, creatingPickaxeTooltip, game, survivor.getProfession(), Constants.Tasks.CREAT_PICKAXE);
        creatingPickaxeTooltip.setInstant(true);
        creatingPickaxeButton.addListener(creatingPickaxeTooltip);

        creatingPickaxeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                addEqAndTaskTable.clear();
                showRoomChooser(stage, survivor,game, Constants.Tasks.CREAT_PICKAXE);
            }
        });


        table.add(closeButton).size(30, 30).pad(5).colspan(79).top().right().row();
        table.add(info).pad(5).colspan(7).expandX().fillX().center().row();
        table.add(creatingFoodButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        table.add(creatingToolsButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateToolsCost(table, Constants.EquipmentPrices.TOOLS_PRICE, game);
        table.add(creatingSearchlightButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, game);
        table.add(creatingKitchenRobotButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.KITCHEN_ROBOT_PRICE, game);
        table.add(creatingOxygenMaskButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.OXYGEN_MASK_PRICE, game);
        table.add(creatingPickaxeButton).pad(10).colspan(7).height(table.getHeight()/8).expandX().fillX().center().row();
        createCreateCost(table, Constants.EquipmentPrices.PICKAXE_PRICE, game);
        table.row().space(10);

    }

    public static void createBuildCost(Table table, BuildingPrice buildingPrice, Game game){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));
        Image electricityImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.ELECTRICITY + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(buildingPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(buildingPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(buildingPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label electricityCost = new Label(buildingPrice.isElectricityRequired() ? "Required" : "Not Required", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(buildingPrice.getMaterials() > game.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(buildingPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(buildingPrice.getFood() > game.getResourceManager().getResource( Constants.Resources.FOOD).getTotalAmount())
            foodCost = new Label(String.valueOf(buildingPrice.getFood()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(buildingPrice.getTools() > game.getResourceManager().getResource( Constants.Resources.TOOLS).getTotalAmount())
            toolsCost = new Label(String.valueOf(buildingPrice.getTools()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if((buildingPrice.isElectricityRequired() ? 1 : 0) > game.getResourceManager().getResource( Constants.Resources.ELECTRICITY).getTotalAmount())
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

    public static void createCreateCost(Table table, EquipmentPrice equipmentPrice, Game game){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(equipmentPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(equipmentPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getMaterials() > game.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(equipmentPrice.getFood() > game.getResourceManager().getResource( Constants.Resources.FOOD).getTotalAmount())
            foodCost = new Label(String.valueOf(equipmentPrice.getFood()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

        if(equipmentPrice.getTools() > game.getResourceManager().getResource( Constants.Resources.TOOLS).getTotalAmount())
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

    public static void createCreateToolsCost(Table table, EquipmentPrice equipmentPrice, Game game){

        Label cost = new Label("Cost: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.MATERIALS + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getMaterials() > game.getResourceManager().getResource( Constants.Resources.MATERIALS).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getMaterials()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));


        table.add(cost).padRight(5).padLeft(10).center();
        table.add(CRImage).size(32, 32).padRight(2).center();
        table.add(CRCost).padRight(5).center();

        table.row();

    }

    public static void taskBlocker(TextButton button, TextTooltip tooltip, Game game, Constants.Survivors profession, Constants.Tasks tasks){
        boolean hasKitchen = game.getRoomManager().hasBuildOfType(Constants.Buildings.KITCHEN);
        boolean hasRestroom = game.getRoomManager().hasBuildOfType(Constants.Buildings.RESTROOM);
        boolean hasWorkshop = game.getRoomManager().hasBuildOfType(Constants.Buildings.WORKSHOP);
        //boolean hasAirPump = game.getRoomManager().hasBuildOfType(Constants.Buildings.AIR_PUMP);
        boolean hasTinkerRoom = game.getRoomManager().hasBuildOfType(Constants.Buildings.TINKER_ROOM);
        //boolean hasPowerStation = game.getRoomManager().hasBuildOfType(Constants.Buildings.POWER_STATION);

        boolean hasLightRock = game.getRoomManager().hasRoomOfType(Constants.RoomType.LIGHT_ROOK_TYPE);
        boolean hasHardRock = game.getRoomManager().hasRoomOfType(Constants.RoomType.HARD_ROOK_TYPE);
        boolean hasRoomToArrange = game.getRoomManager().hasRoomOfType(Constants.RoomType.ROOM_TO_ARRANGE);

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
                        if(game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
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
                            break;
                        }
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                        tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                        button.setDisabled(true);
                        break;
                    }
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                            break;
                        }
                    case EAT:
                        if(game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                            break;
                        }
                    case BUILD:
                        if(!hasRoomToArrange) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no room to build infrastructure here.");
                            button.setDisabled(true);
                            break;
                        }
                    case BUILD_RESTROOM:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.RESTROOM_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Restroom.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_KITCHEN:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.KITCHEN_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Kitchen.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_ELEVATOR:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.ELEVATOR_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Elevator.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_WORKSHOP:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.WORKSHOP_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Workshop.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_POWER_STATION:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.POWER_STATION_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Power Station.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_AIR_PUMP:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.AIR_PUMP_PRICE)) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]Not enough resources to build Air Pump.");
                            button.setDisabled(true);
                        }
                        break;
                    case BUILD_TINKER_ROOM:
                        if(isEnoughResources(game, null, Constants.BuildingPrices.RESTROOM_PRICE)) {
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
                            if (isEnoughResources(game, Constants.EquipmentPrices.TOOLS_PRICE, null)) {
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
                            break;
                        }
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                            break;
                        }
                    case EAT:
                        if(game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                            break;
                        }
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
                            break;
                        }
                    case TRAIN_TO_ENGINEER:
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]A engineer cannot be trained as a engineer.");
                            button.setDisabled(true);
                            break;
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                            break;
                        }
                    case EAT:
                        if(game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                            break;
                        }
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
                            if (isEnoughResources(game, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
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
                            if (isEnoughResources(game, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
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
                            if (isEnoughResources(game, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
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
                            if (isEnoughResources(game, Constants.EquipmentPrices.SEARCHLIGHT_PRICE, null)) {
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
                            break;
                        }
                    case TRAIN_TO_ENGINEER:
                        if(!hasTinkerRoom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Tinker Room to do this action.");
                            button.setDisabled(true);
                            break;
                        }
                    case REST:
                        if(!hasRestroom) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Restroom to do this action.");
                            button.setDisabled(true);
                            break;
                        }
                    case EAT:
                        if(game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() - game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() == 0) {
                            tooltip.getActor().setText(tooltip.getActor().getText() + "\n\n [RED]There is no Food to eat.");
                            button.setDisabled(true);
                            break;
                        }
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

    public static boolean isEnoughResources(Game game, EquipmentPrice equipmentPrice, BuildingPrice buildingPrice){

        if (equipmentPrice != null)
        {
            if(game.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() -
                game.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() > equipmentPrice.getTools() &&
                game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() -
                    game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() > equipmentPrice.getFood() &&
                game.getResourceManager().getResource(Constants.Resources.MATERIALS).getTotalAmount() -
                    game.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount() > equipmentPrice.getMaterials()
            ) return false;
        }else{
            if(game.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() -
                game.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount() > buildingPrice.getTools() &&
                game.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() -
                    game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount() > buildingPrice.getFood() &&
                game.getResourceManager().getResource(Constants.Resources.MATERIALS).getTotalAmount() -
                    game.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount() > buildingPrice.getMaterials() &&
                game.getResourceManager().getResource( Constants.Resources.ELECTRICITY).getTotalAmount() -
                    game.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount() > (buildingPrice.isElectricityRequired() ? 1 : 0)
            )return false;
        }
        return true;
    }

    private static void reloadResources(Survivor survivor, Game game) {
        game.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
                + survivor.getTask().getCost().getMaterials());

        game.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount()
                + survivor.getTask().getCost().getTools());

        game.getResourceManager().getResource(Constants.Resources.FOOD).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount()
                + survivor.getTask().getCost().getFood());

        game.getResourceManager().getResource(Constants.Resources.ELECTRICITY).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount()
                + (survivor.getTask().getCost().isElectricityRequired() ? 1 : 0));
    }

    private static void showRoomChooser( Stage stage, Survivor survivor, Game game, Constants.Tasks task){
        survivorsTable.setTouchable(Touchable.disabled);
        Table table = new Table();
        table.setSize(centerTable.getWidth(), Gdx.graphics.getHeight()/7f);
        table.left().bottom();
        table.setPosition(0,0);

        createBorderRooms(stage, survivor, game, task);

        table.setBackground(DigOutGame.skin.getDrawable("box"));
        Label label = new Label("Select the room on the map above to which you want to assign the survivor.\n" +
            "[GRAY] Rooms marked with [GREEN]GREEN[GRAY] border can be selected for this task, there is no one inside so the survivor can complete the task here.\n" +
            "Rooms marked with [YELLOW]YELLOW[GRAY] border mean that someone is already assigned to that room, you can assign the current survivor here, but the previous one will lose the assigned task.\n" +
            "The room marked with [ORANGE]ORANGE[GRAY] border is the BASE, you can assign more than one survivor here.\n" +
            "Rooms marked with NO border mean that the tasks the survivor has selected cannot be completed in that room.", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        label.setWrap(true);
        label.setAlignment(1);
        label.setWidth(Gdx.graphics.getWidth()*3/4f);

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                actionAfterCloseByX(table);
            }
        });

        table.add(closeButton).size(30, 30).top().right().row();
        table.add(label).expandX().fillX().left().bottom().padLeft(5).padRight(5).padBottom(5);
        outerPinRoomTable.addActor(table);

    }

    private static void createBorderRooms(Stage stage, Survivor survivor, Game game, Constants.Tasks task){

        switch (task){
            case WAIT:
            case EAT:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(null, Constants.Buildings.RESTROOM), task);
                drawBaseRoom(game, stage, survivor, task);
                break;
            case REST:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(null, Constants.Buildings.RESTROOM), task);
                break;
            case TRAIN_TO_COOK:
            case CREAT_FOOD:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(null, Constants.Buildings.KITCHEN), task);
                break;
            case TRAIN_TO_ENGINEER:
            case CREAT_SEARCHLIGHT:
            case CREAT_KITCHEN_ROBOT:
            case CREAT_OXYGEN_MASK:
            case CREAT_PICKAXE:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(null, Constants.Buildings.TINKER_ROOM), task);
                break;
            case BUILD_RESTROOM:
            case BUILD_KITCHEN:
            case BUILD_ELEVATOR:
            case BUILD_WORKSHOP:
            case BUILD_POWER_STATION:
            case BUILD_AIR_PUMP:
            case BUILD_TINKER_ROOM:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(Constants.RoomType.ROOM_TO_ARRANGE, null), task);
                break;
            case CREAT_TOOLS:
                drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(null, Constants.Buildings.WORKSHOP), task);
                break;
            case DIG_OUT:
                if(survivor.getProfession() == Constants.Survivors.WORKER)
                    drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(Constants.RoomType.LIGHT_ROOK_TYPE, null), task);
                if(survivor.getProfession() == Constants.Survivors.MINER)
                    drawAvailableRooms(game, stage, survivor, game.getRoomManager().getRoomsByType(Constants.RoomType.HARD_ROOK_TYPE, null), task);
                break;
        }
    }

    private static void drawAvailableRooms(Game game, Stage stage, Survivor survivor, Coordinate[] c1, Constants.Tasks task){
        for (Coordinate entry : c1) {
            String color;

            if(game.getRoomManager().getRoom(entry).isFull()){
                color = "YELLOW_BORDER";
            }else{
                color = "GREEN_BORDER";
            }

            Table table = new Table();
            table.setSize(roomWidth, roomHeight);
            table.setPosition(gameTable.get(entry).getX() - 2, gameTable.get(entry).getY() + Gdx.graphics.getHeight()/7f + 1);
            table.setBackground(DigOutGame.borderSkin.getDrawable(color));
            table.setTouchable(Touchable.enabled);

            table.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(color.equals("GREEN_BORDER")){
                        displayConfirmBox(stage, "Do you want " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                            if (confirmed) {
                                survivorGotTask(survivor, entry, task, table, game);
                            }
                        });
                    }else {
                        Survivor temp = null;
                        try {
                            temp = game.getSurvivorManager().whoIsInTheRoom(entry);
                            Survivor finalTemp = temp;
                            displayConfirmBox(stage, "Do you want " + temp.getName() + " to be disconnected from his task and " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                                if (confirmed) {
                                    logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + finalTemp.getName() + ", Unpin task: " + finalTemp.getTask().getTask(), null);
                                    backResources(finalTemp, game);
                                    finalTemp.setTask(null);
                                    finalTemp.changeImgForNotWork();
                                    survivorGotTask(survivor, entry, task, table, game);
                                     }
                            });
                        } catch (NullPointerException e) {
                            logs(DateLogs.LogType.ERROR, game.getGameId(), "Survivor that should be unpinned returns NULL", e);
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


            outerPinRoomTable.addActor(table);
        }
    }

    private static void drawBaseRoom(Game game, Stage stage, Survivor survivor, Constants.Tasks task){
        Coordinate cb = game.getRoomManager().getBaseRoom().getCoordinates();
        Table table = new Table();
        table.setSize(roomWidth, roomHeight);
        table.setPosition(gameTable.get(cb).getX() + - 2, gameTable.get(cb).getY() + Gdx.graphics.getHeight()/7f + 1);
        table.setBackground(DigOutGame.borderSkin.getDrawable("ORANGE_BORDER"));
        table.setTouchable(Touchable.enabled);
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                displayConfirmBox(stage, "Do you want " + survivor.getName() + " to " + task + " in the selected room?", confirmed -> {
                    if (confirmed) {
                        survivorGotTask(survivor, cb, task, table, game);
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
                table.setBackground(DigOutGame.borderSkin.getDrawable("ORANGE_BORDER"));
            }
        });


        outerPinRoomTable.addActor(table);

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

    private static void survivorGotTask(Survivor survivor, Coordinate coordinate, Constants.Tasks task, Table table, Game game){
        survivor.setTask(new Task(coordinate, task, costOfTask(task)));
        reloadResources(survivor, game);
        needsRefreshAfterAddTask = true;
        tempSurvivor = survivor;
        survivor.changeImgForWork();
        game.getRoomManager().getRoom(coordinate).setFull(true);
        game.getRoomManager().getRoom(coordinate).changeImgForWork();

        addEqAndTaskTable.clear();
        needsRefreshAfterAddEQ = true;

        table.setTouchable(Touchable.disabled);
        outerPinRoomTable.clear();

        logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + " got a task: " + survivor.getTask().getTask() + " , in room: " + game.getRoomManager().getRoom(coordinate) + " , cords: " + coordinate.getX() + ", " + coordinate.getY(), null);
    }

    private static void actionAfterCloseByX(Table table){
        table.setVisible(false);
        addEqAndTaskTable.clear();
        outerPinRoomTable.clear();
        survivorsTable.setTouchable(Touchable.enabled);
    }

}
