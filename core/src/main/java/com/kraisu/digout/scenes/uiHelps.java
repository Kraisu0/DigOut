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
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.managers.EquipmentManager;
import com.kraisu.digout.managers.ResourceManager;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.stuff.BuildingPrice;
import com.kraisu.digout.stuff.EquipmentPrice;
import com.kraisu.digout.survivor.Survivor;
import com.kraisu.digout.survivor.Task;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.kraisu.digout.logs.DateLogs.logs;


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
                table.setVisible(false);
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
                                logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + "got a." + survivor.getEquipment().getName(), null);
                                game.getEquipmentManager().getEquipment(equipment).setAllocatedAmount(1);
                                game.getEquipmentManager().getEquipment(equipment).consumeAllocatedEquipment();
                                survivor.setEquipment(game.getEquipmentManager().getEquipment(equipment));

                                if(equipment == Constants.Equipment.PICKAXE) {
                                    survivor.setProfession(Constants.Survivors.MINER);
                                    logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + "was trained as a Miner.", null);
                                }

                                addButton.setDisabled(true);
                                GameScreen.addEqAndTaskTable.clear();
                                GameScreen.needsRefreshAfterAddEQ = true;
                                GameScreen.tempSurvivor = survivor;
                            }
                        });
                    }
                });
            }

            table.add(addButton).height(32).width(64).pad(5);
        }

        TextButton addToolButton = new TextButton("Add", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));

        if (game.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() == 0 || survivor.getProfession() == Constants.Survivors.WORKER || survivor.getProfession() == Constants.Survivors.MINER) {
            addToolButton.setDisabled(true);
        } else {
            addToolButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    displayConfirmBox(stage, "Do you want to rebrand a survivor as a Worker?", confirmed -> {
                        if (confirmed) {
                            game.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount(1);
                            game.getResourceManager().getResource(Constants.Resources.TOOLS).consumeAllocatedResources();
                            survivor.setProfession(Constants.Survivors.WORKER);
                            addToolButton.setDisabled(true);
                            GameScreen.addEqAndTaskTable.clear();
                            GameScreen.needsRefreshAfterAddEQ = true;
                            GameScreen.tempSurvivor = survivor;

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
                table.setVisible(false);
            }
        });

        Label info = new Label("What should the chosen survivor do?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton waitingButton = new TextButton("Waiting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip waitingTooltip = new TextTooltip(" The survivor will do nothing. He'll just sit on his ass and wait. \n\n This action can only be performed in the base or restroom.", DigOutGame.skin);
        waitingTooltip.setInstant(true);
        waitingButton.addListener(waitingTooltip);

        waitingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                survivor.setTask(new Task(null, Constants.Tasks.WAIT));
                GameScreen.needsRefreshAfterAddTask = true;
                GameScreen.tempSurvivor = survivor;
            }
        });

        TextButton trainingButton = new TextButton("Training to", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingTooltip = new TextTooltip(" The survivor will be trained for another profession. To make he more useful. \n\n You can train: in the Kitchen to become a cook or in the Workshop to become an engineer.", DigOutGame.skin);
        trainingTooltip.setInstant(true);
        trainingButton.addListener(trainingTooltip);

        trainingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                trainingTasksTable(table, stage, survivor);
            }
        });

        TextButton restingButton = new TextButton("Resting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip restingTooltip = new TextTooltip(" The survivor will rest. He needs to get some sleep before the next hard days. \n\n Sleeping restores 1 energy point. \n\n This action can only be performed in the restroom.", DigOutGame.skin);
        restingTooltip.setInstant(true);
        restingButton.addListener(restingTooltip);

        restingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go rest");
            }
        });

        TextButton eatingButton = new TextButton("Eating", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip eatingTooltip = new TextTooltip(" The survivor must feed. These breads are probably lembas. \n\n Eating restores full energy point. \n\n For this action you will need food and a base or a free restroom.", DigOutGame.skin);
        eatingTooltip.setInstant(true);
        eatingButton.addListener(eatingTooltip);

        eatingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go eat");
            }
        });

        TextButton creatingStuffButton = new TextButton("Create something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingStuffTooltip = new TextTooltip(" The survivor will create some stuff: food, tools or extra EQ. \n\n The Cook can create food, the Worker can create tools, and the Engineer can create additional EQ.", DigOutGame.skin);
        creatingStuffTooltip.setInstant(true);
        creatingStuffButton.addListener(creatingStuffTooltip);

        creatingStuffButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                creatingTasksTable(table, stage, survivor, game);
            }
        });

        TextButton buildingButton = new TextButton("Build something", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTooltip = new TextTooltip(" The survivor will build. Let's hope nothing collapses. \n\n These actions can only be performed by the Worker and, of course, the additional resources it needs." , DigOutGame.skin);
        buildingTooltip.setInstant(true);
        buildingButton.addListener(buildingTooltip);

        buildingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                buildingTasksTable(table, stage, survivor, game);
            }
        });

        TextButton digOutingButton = new TextButton("DigOuting", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip digOutingTooltip = new TextTooltip(" The survivor will search for stuff and at the same time excavate a new room. \n\n These actions can only be performed by the Worker at the lower levels, and at the two highest levels only the Miner can mine.", DigOutGame.skin);
        digOutingTooltip.setInstant(true);
        digOutingButton.addListener(digOutingTooltip);

        digOutingButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go digout");
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

    private static void trainingTasksTable (Table table, Stage stage, Survivor survivor) {
        table.clear();
        table.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 4f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        TextButton closeButton = new TextButton("X", DigOutGame.skinButton.get("small-dark-red", TextButton.TextButtonStyle.class));
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.setVisible(false);
            }
        });

        Label info = new Label("What profession do you want to train a survivor for?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton trainingToCookButton = new TextButton("Training to Cook", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingToCookTooltip = new TextTooltip(" The survivor will be trained to be a cook so that he can contribute to the community by cooking. \n\n This action can be performed in the Kitchen.", DigOutGame.skin);
        trainingToCookTooltip.setInstant(true);
        trainingToCookButton.addListener(trainingToCookTooltip);

        trainingToCookButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go training to Cook");
            }
        });

        TextButton trainingToEngineerButton = new TextButton("Training to Engineer", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip trainingToEngineerTooltip = new TextTooltip(" The survivor will be trained as an engineer so that he can create useful tools. \n\n This action can be performed in the Tinker room.", DigOutGame.skin);
        trainingToEngineerTooltip.setInstant(true);
        trainingToEngineerButton.addListener(trainingToEngineerTooltip);

        trainingToEngineerButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go training to Engineer");
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
                table.setVisible(false);
            }
        });

        Label info = new Label("What should the survivor build?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton buildingRestroomButton = new TextButton("Building Restroom", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingRestroomTooltip = new TextTooltip(" In the Resstroom, survivors can rest and eat.", DigOutGame.skin);
        buildingRestroomTooltip.setInstant(true);
        buildingRestroomButton.addListener(buildingRestroomTooltip);

        buildingRestroomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a Restroom");
            }
        });

        TextButton buildingKitchenButton = new TextButton("Building Kitchen", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingKitchenTooltip = new TextTooltip(" In the Kitchen, the cook can make food and survivors can train to be cooks.", DigOutGame.skin);
        buildingKitchenTooltip.setInstant(true);
        buildingKitchenButton.addListener(buildingKitchenTooltip);

        buildingKitchenButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a Kitchen");
            }
        });

        TextButton buildingElevatorButton = new TextButton("Building Elevator", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingElevatorTooltip = new TextTooltip(" The elevator allows you to get to the next level.", DigOutGame.skin);
        buildingElevatorTooltip.setInstant(true);
        buildingElevatorButton.addListener(buildingElevatorTooltip);

        buildingElevatorButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a Elevator");
            }
        });

        TextButton buildingWorkshopButton = new TextButton("Building Workshop", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingWorkshopTooltip = new TextTooltip(" In the Workshop, Workers can create tools.", DigOutGame.skin);
        buildingWorkshopTooltip.setInstant(true);
        buildingWorkshopButton.addListener(buildingWorkshopTooltip);

        buildingWorkshopButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a Workshop");
            }
        });

        TextButton buildingPowerStationButton = new TextButton("Building Power Station", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingPowerStationTooltip = new TextTooltip(" The power station allows you to connect 4 buildings to electricity. \n\n Power station increases electricity level by 4.", DigOutGame.skin);
        buildingPowerStationTooltip.setInstant(true);
        buildingPowerStationButton.addListener(buildingPowerStationTooltip);

        buildingPowerStationButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a PowerStation");
            }
        });

        TextButton buildingAirPumpButton = new TextButton("Building Air pump", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingAirPumpTooltip = new TextTooltip(" Placing the air pump on a level reduces the air requirement by 1 level." , DigOutGame.skin);
        buildingAirPumpTooltip.setInstant(true);
        buildingAirPumpButton.addListener(buildingAirPumpTooltip);

        buildingAirPumpButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a AirPump");
            }
        });

        TextButton buildingTinkerRoomButton = new TextButton("Building Tinker Room", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip buildingTinkerRoomTooltip = new TextTooltip(" In the Tinker Room, an engineer can create additional EQ and a survivor can be trained to be an engineer.", DigOutGame.skin);
        buildingTinkerRoomTooltip.setInstant(true);
        buildingTinkerRoomButton.addListener(buildingTinkerRoomTooltip);

        buildingTinkerRoomButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go building a TinkerRoom");
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
                table.setVisible(false);
            }
        });

        Label info = new Label("What should the survivor create?", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        TextButton creatingFoodButton = new TextButton("Creating Food", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingFoodTooltip = new TextTooltip(" Food can be made in the kitchen, from some rat or spring water.", DigOutGame.skin);
        creatingFoodTooltip.setInstant(true);
        creatingFoodButton.addListener(creatingFoodTooltip);

        creatingFoodButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Food");
            }
        });

        TextButton creatingToolsButton = new TextButton("Creating Tools", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingToolsTooltip = new TextTooltip(" Tools can be made from construction resources building materials.", DigOutGame.skin);
        creatingToolsTooltip.setInstant(true);
        creatingToolsButton.addListener(creatingToolsTooltip);

        creatingToolsButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Tools");
            }
        });

        TextButton creatingSearchlightButton = new TextButton("Creating Searchlight", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingSearchlightTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).getDescription(), DigOutGame.skin);
        creatingSearchlightTooltip.setInstant(true);
        creatingSearchlightButton.addListener(creatingSearchlightTooltip);

        creatingSearchlightButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Searchlight");
            }
        });

        TextButton creatingKitchenRobotButton = new TextButton("Creating Kitchen Robot", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingKitchenRobotTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).getDescription(), DigOutGame.skin);
        creatingKitchenRobotTooltip.setInstant(true);
        creatingKitchenRobotButton.addListener(creatingKitchenRobotTooltip);

        creatingKitchenRobotButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Kitchen Robot");
            }
        });

        TextButton creatingOxygenMaskButton = new TextButton("Creating Oxygen Mask", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingOxygenMaskTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).getDescription(), DigOutGame.skin);
        creatingOxygenMaskTooltip.setInstant(true);
        creatingOxygenMaskButton.addListener(creatingOxygenMaskTooltip);

        creatingOxygenMaskButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Oxygen mask");
            }
        });

        TextButton creatingPickaxeButton = new TextButton("Creating Pickaxe", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        TextTooltip creatingPickaxeTooltip = new TextTooltip(" " + game.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).getDescription(), DigOutGame.skin);
        creatingPickaxeTooltip.setInstant(true);
        creatingPickaxeButton.addListener(creatingPickaxeTooltip);

        creatingPickaxeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Survivor go creating a Pickaxe");
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
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.CONSTRUCTION_RESOURCES + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));
        Image electricityImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.ELECTRICITY + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(buildingPrice.getConstructionResources()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(buildingPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(buildingPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label electricityCost = new Label(buildingPrice.isElectricityRequired() ? "Required" : "Not Required", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(buildingPrice.getConstructionResources() > game.getResourceManager().getResource( Constants.Resources.CONSTRUCTION_RESOURCES).getTotalAmount())
            CRCost = new Label(String.valueOf(buildingPrice.getConstructionResources()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

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
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.CONSTRUCTION_RESOURCES + "_icon_64.png")));
        Image foodImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.FOOD + "_icon_64.png")));
        Image toolsImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.TOOLS + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getConstructionResources()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label foodCost = new Label(String.valueOf(equipmentPrice.getFood()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Label toolsCost = new Label(String.valueOf(equipmentPrice.getTools()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getConstructionResources() > game.getResourceManager().getResource( Constants.Resources.CONSTRUCTION_RESOURCES).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getConstructionResources()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));

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
        Image CRImage = new Image(new Texture(Gdx.files.internal("assets/resources/" + Constants.Resources.CONSTRUCTION_RESOURCES + "_icon_64.png")));

        Label CRCost = new Label(String.valueOf(equipmentPrice.getConstructionResources()), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        if(equipmentPrice.getConstructionResources() > game.getResourceManager().getResource( Constants.Resources.CONSTRUCTION_RESOURCES).getTotalAmount())
            CRCost = new Label(String.valueOf(equipmentPrice.getConstructionResources()), DigOutGame.skin.get("redSmallFont", Label.LabelStyle.class));


        table.add(cost).padRight(5).padLeft(10).center();
        table.add(CRImage).size(32, 32).padRight(2).center();
        table.add(CRCost).padRight(5).center();

        table.row();

    }


}
