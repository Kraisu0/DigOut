package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.managers.SurvivorManager;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.stuff.Diary;
import com.kraisu.digout.survivor.Survivor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;


import static com.kraisu.digout.logs.DateLogs.logs;
import static com.kraisu.digout.managers.DiaryManager.displaySumBox;
import static com.kraisu.digout.scenes.uiHelps.*;

public class GameScreen implements Screen {

    private static MyGame myGame;
    private Stage stage;
    private Table outerTable, outerMenuTable, outerAddEqTable, rightTable, nameTable, resourcesTable,
        infoTable, infoButtonTable, miniMenuTable, survivorInfoTable,
        eqInfoTable, diaryInfoTable;
    private static Table menuTable;
    private Map<Constants.Resources, Integer> resourceStatus, resourceAllocatedStatus;
    private LinkedHashMap<Constants.Equipment, Integer> equipmentStatus;
    private Map<Constants.Resources, String> resourceName;
    private LinkedHashMap<Constants.Equipment, String> equipmentName, equipmentDescription;
    private LinkedHashMap<Constants.Resources, String> resourceDescription;
    private Button infoButton, eqButton, diaryButton, endRoundButton;
    private Label fps;
    private uiHelps uiHelps1;
    private String name;
    private int roundNumber;
    private TextTooltip endRoundTooltip, tooltipAddEQ;

    private static Button addTask, addEQ;
    private static boolean needsRefreshAfterAddEQ = false;
    private static boolean needsRefreshAfterAddTask = false;
    private static boolean needsRefreshAfterEndRound = false;
    private static boolean isGameWin = false;
    private static boolean isGameLose = false;
    private static boolean isXWasClicked = true;
    private static boolean isChooseRoomVisible = false;
    private static Survivor tempSurvivor = null;
    private static Table addEqAndTaskTable, outerPinRoomTable, centerTable, survivorsTable, roomsTable;
    private static boolean isMenuOpen, isInfoBoxOpen;
    private static Map<Coordinate, Table> gameTable = Map.of();

    public static final float roomWidth = Gdx.graphics.getWidth() * 4 / 5f / 10;
    public static final float roomHeight = Gdx.graphics.getHeight() *11 / 14f / 10 - 2;



    public GameScreen(MyGame myGame) {
        this.myGame = myGame;
        this.resourceStatus = new LinkedHashMap<>();
        this.resourceName = new LinkedHashMap<>();
        this.equipmentStatus = new LinkedHashMap<Constants.Equipment, Integer>();
        this.equipmentName = new LinkedHashMap<Constants.Equipment, String>();
        this.equipmentDescription = new LinkedHashMap<Constants.Equipment, String>();
        this.resourceDescription = new LinkedHashMap<Constants.Resources, String>();
        this.uiHelps1 = new uiHelps();
        gameTable = new LinkedHashMap<>();
        isMenuOpen = false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        outerTable = new Table();
        outerTable.setFillParent(true);
        outerMenuTable = new Table();
        outerMenuTable.setFillParent(true);
        outerAddEqTable = new Table();
        outerAddEqTable.setFillParent(true);
        outerPinRoomTable = new Table();
        outerPinRoomTable.setFillParent(true);

        centerTable = new Table();
        rightTable = new Table();

        nameTable = new Table();

        resourcesTable = new Table();

        roomsTable = new Table();

        infoTable = new Table();
        infoButtonTable = new Table();
        survivorInfoTable = new Table();
        eqInfoTable = new Table();
        diaryInfoTable = new Table();


        survivorsTable = new Table();
        ScrollPane scrollPane = new ScrollPane(survivorsTable);
        scrollPane.setScrollingDisabled(false, true);

        addEqAndTaskTable = new Table();

        miniMenuTable = new Table();
        menuTable = new Table();

        //menu table
        menuTable.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/3f);
        menuTable.setVisible(false);

        menuTable.setBackground(DigOutGame.skin.getDrawable("box.gray"));

        Button backToGameButton = new TextButton("BACK TO GAME", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        backToGameButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menuTable.setVisible(false);
                isMenuOpen = false;
            }
        });

        Button saveGameButton = new TextButton("SAVE GAME", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        saveGameButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                displayConfirmBox(stage, "Do you want save this file?", confirmed -> {
                    if (confirmed) {
                        uiHelps.saveGame(stage, myGame);
                        logs(DateLogs.LogType.INFO, myGame.getGameId(), "Game saved!", null);
                    }
                });
            }
        });

        //TODO Dodać slidera mojego
        Slider volumeSlider = new Slider(0, 1, 0.1f, false, DigOutGame.uiskin);
        volumeSlider.setValue(myGame.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                myGame.setVolume(volumeSlider.getValue());
            }
        });

        Button exitButton = new TextButton("EXIT TO MAIN MENU", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                displayConfirmBox(stage, "Are you sure you want to exit without saving?", confirmed -> {
                    if (confirmed) {
                        ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new SplashToNextScreen(Constants.WhereSplashGo.EXIT, myGame));
                    }
                });

            }
        });

        menuTable.add(backToGameButton).pad(10).width(menuTable.getWidth()-20).colspan(2).row();
        menuTable.add(saveGameButton).pad(10).width(menuTable.getWidth()-20).colspan(2).row();
        menuTable.add(new Label("Volume", DigOutGame.skin.get("bigFont", Label.LabelStyle.class))).pad(10).expandX().center();
        menuTable.add(volumeSlider).pad(10).width(menuTable.getWidth()/2 - 20).left().row();
        menuTable.add(exitButton).pad(10).width(menuTable.getWidth()-20).colspan(2);

        outerMenuTable.add(menuTable);


        //name
        nameTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));
        name = myGame.getPlayer().getName();
        Label nameLabel;
        int nameLength = name.length();

        if (nameLength > 30) {
            name = name.substring(0, 27) + "...";
        }

        if(nameLength <= 15)
            nameLabel = new Label(name, DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
        else if (nameLength <= 25)
            nameLabel = new Label(name, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        else
            nameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        nameLabel.setColor(Color.GOLD);
        nameTable.add(nameLabel).expandX().center().height(Gdx.graphics.getHeight()/14f);


        //resources bar
        populateResourceBar();


        //screen 10x10
        for (int i = 10; i >= 1; i--) {
            for (int j = 1; j <= 10; j++) {
                Coordinate tempCoordinate = new Coordinate(j, i);
                Table tempTable = new Table();
                tempTable.setSize(roomWidth, roomHeight);
                gameTable.put(tempCoordinate, tempTable);

                tempTable.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("Coordinates: " + tempCoordinate.getX() + ", " + tempCoordinate.getY());
                    }
                });

            }
        }

        for (int i = 10; i >= 1; i--) {
            for (int j = 1; j <= 10; j++) {
                Coordinate tempCoordinate = new Coordinate(j, i);
                Table tempTable = gameTable.get(tempCoordinate);
                tempTable.setSize(roomWidth, roomHeight);

                if (tempTable != null) {
                    roomsTable.add(tempTable).size(roomWidth, roomHeight).maxSize(roomWidth, roomHeight).expand().fill();
                } else {
                    logs(DateLogs.LogType.INFO,
                        myGame.getGameId(),
                        "Error: tempTable is null for coordinate: " + tempCoordinate, null);
                }
            }
            roomsTable.row();
        }

        colorTileAtCoordinateBaseRoom(myGame.getRoomManager().getBaseRoom());
        myGame.getRoomManager().makeAbleToDiscoveredNearestRooms(myGame.getRoomManager().getBaseRoom().getCoordinates(), myGame);
        updateColorRoom();

        //infoTable
        infoButton = new TextButton("Info", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));
        infoButton.setDisabled(true);
        eqButton = new TextButton("EQ", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));
        diaryButton = new TextButton("Diary", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));

        infoButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                changeToInfoSurvivor();
            }
        });

        eqButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.clear();
                infoTable.add(eqInfoTable).expand().fill().center();

                infoButton.setDisabled(false);
                eqButton.setDisabled(true);
                diaryButton.setDisabled(false);
            }
        });

        diaryButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                changeToDiary();
            }
        });

        infoButtonTable.add(infoButton).expand().fill().center().padTop(0).height(Gdx.graphics.getHeight()/14f);
        infoButtonTable.add(eqButton).expand().fill().center().height(Gdx.graphics.getHeight()/14f);
        infoButtonTable.add(diaryButton).expand().fill().center().height(Gdx.graphics.getHeight()/14f);

        survivorInfoTable.setBackground(DigOutGame.skin.getDrawable("box"));
        eqInfoTable.setBackground(DigOutGame.skin.getDrawable("box.gray"));
        diaryInfoTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));



        //eq table
//        myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).setTotalAmount(2);
        refreshEqInfoTable();

        //addEqTable
        //addEqAndTaskTable.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        outerAddEqTable.add(addEqAndTaskTable);

//        myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, Constants.Survivors.MINER));
//        myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, Constants.Survivors.COOK));
//        myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, Constants.Survivors.ENGINEER));
//        myGame.getSurvivorManager().addSurvivor(myGame.getSurvivorManager().generateNewSurvivors(myGame, Constants.Survivors.UNTRAINED));


        //survivors bar
        float survivorBoxWidth = 150f;
        float survivorBoxHeight = Gdx.graphics.getHeight()/14f;

        survivorsTable.align(Align.left | Align.top);
        survivorsTable.setBackground(DigOutGame.skin.getDrawable("box.wood"));

        int survivorCount = myGame.getSurvivorManager().getAllSurvivors().size();

        for (int i = 0; i < survivorCount; i++) {
            Survivor survivor = myGame.getSurvivorManager().getAllSurvivors().toArray(new Survivor[0])[i];


            Table survivorEntry = new Table();
            Table survivorEntryNames = new Table();
            Table survivorEntryFull = new Table();

            Image icon = new Image(new TextureRegionDrawable(new TextureRegion( new Texture(survivor.getImgPath()))));
            survivorEntry.add(icon).size(64, 64).padLeft(20);

            Label survivorNameLabel = new Label(survivor.getName(), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            survivorEntryNames.add(survivorNameLabel).size(86, 48).padRight(5).padLeft(5).center().row();

            Image energyIcon = new Image(survivor.getEnergyIconDrawable());
            survivorEntryNames.add(energyIcon).size(64, 16).left().padLeft(5).padBottom(3);

            if(survivorCount % 2 == 1 && survivorCount / 2 == i-1){
               survivorsTable.row();
            }

            if(survivorCount % 2 == 0 && survivorCount / 2 == i){
                survivorsTable.row();
            }

            if(survivor.getProfession() == Constants.Survivors.COOK || survivor.getProfession() == Constants.Survivors.UNTRAINED){
                survivorNameLabel.setColor(Color.BLACK);
            }

            survivorEntryFull.setBackground(DigOutGame.skinSurvivorBox.getDrawable( "box." + survivor.getProfession()));
            survivorEntryFull.add(survivorEntry);
            survivorEntryFull.add(survivorEntryNames);

            survivorEntryFull.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    addEqAndTaskTable.clear();
                    survivorInfoTable.clear();
                    survivorInfoTable.add(showSurvivorInfo(survivor));
                    changeToInfoSurvivor();
                }
            });

            survivorsTable.add(survivorEntryFull).size(survivorBoxWidth, survivorBoxHeight);
        }


        //mini menu
        populateMiniMenu();




        infoTable.add(survivorInfoTable).expand().fill().center();

        centerTable.add(resourcesTable).height(Gdx.graphics.getHeight()/14f).expandX().fillX().row();
        centerTable.add(roomsTable).expand().fill().row();
        centerTable.add(scrollPane).height(Gdx.graphics.getHeight() / 7f).expandX().fillX();
        rightTable.add(nameTable).height(Gdx.graphics.getHeight()/14f).expandX().fillX().row();
        rightTable.add(infoButtonTable).height(Gdx.graphics.getHeight()/14f).expandX().fillX().row();
        rightTable.add(infoTable).expand().fill().row();
        rightTable.add(miniMenuTable).height(Gdx.graphics.getHeight()*3/14f).expandX().fillX();

        outerTable.add(centerTable).width(Gdx.graphics.getWidth() * 4 / 5f).fillY().expandY();
        outerTable.add(rightTable).width(Gdx.graphics.getWidth() / 5f).fillY().expandY();

//        eqInfoTable.debug();
//        survivorInfoTable.debug();
//        menuTable.debug();
//        outerTable.debug();
//        centerTable.debug();
//        rightTable.debug();
//        resourcesTable.debug();
//        roomsTable.debug();
//        survivorsTable.debug();
//        nameTable.debug();
//        miniMenuTable.debug();
//        addEqAndTaskTable.debug();

        stage.addActor(outerTable);
        stage.addActor(outerAddEqTable);
        stage.addActor(outerPinRoomTable);
        stage.addActor(outerMenuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        //this.MyGame = MyGame;
        //refreshEqInfoTable();

        //MyGame.getSurvivorManager().showDisable();

        try {
            checkNeedsRefresh();
        } catch (InterruptedException e) {
            logs(DateLogs.LogType.ERROR, myGame.getGameId(), "Error while doing checkNeedsRefresh", e);
            throw new RuntimeException(e);
        }

        if(menuTable.isVisible() || !isXWasClicked) {
            outerTable.setColor(0, 0, 0, 0.3f);
            outerTable.setTouchable(Touchable.disabled);
        } else {
            outerTable.setColor(0, 0, 0, 1f);
            outerTable.setTouchable(Touchable.enabled);
        }

//        if(!isXWasClicked) {
//            outerTable.setColor(0, 0, 0, 0.3f);
//            outerTable.setTouchable(Touchable.disabled);
//        } else {
//            outerTable.setColor(0, 0, 0, 1f);
//            outerTable.setTouchable(Touchable.enabled);
//        }

        fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        openMenu();




        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void checkNeedsRefresh() throws InterruptedException {
        if (needsRefreshAfterAddEQ) {
            needsRefreshAfterAddEQ = false;
            refreshEqInfoTable();
            refreshSurvivorBar();
            refreshResourceBar();
            survivorInfoTable.clear();
            survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
            survivorsTable.setTouchable(Touchable.enabled);


        }

        if(needsRefreshAfterAddTask){
            needsRefreshAfterAddTask = false;
            updateColorRoom();
            refreshSurvivorBar();
            refreshResourceBar();
            survivorInfoTable.clear();
            survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
            survivorsTable.setTouchable(Touchable.enabled);
            checkAbleToEndRound(myGame, endRoundTooltip ,endRoundButton);


        }

        if(needsRefreshAfterEndRound){
            needsRefreshAfterEndRound = false;
            isXWasClicked = false;
            myGame.getSurvivorManager().allSurvivorGotReceivedStuff();
            myGame.getResourceManager().consumeAllocatedResources();
            uiHelps1.doTheTasks(myGame);
            myGame.getSurvivorManager().checkOxygenForAllLevels(myGame);
            Diary diary = new Diary();
            diary.makeEntryForAllSurvivors(myGame);
            myGame.getDiaryManager().makeDiaryEntryPerDay(myGame, diary);
            changeToDiary();
            displaySumBox(stage, diary, myGame);
            myGame.getSurvivorManager().clearSurvivorsTasks(myGame);
            myGame.getSurvivorManager().checkSurvivorStatus();
            refreshEqInfoTable();
            survivorInfoTable.clear();
            refreshSurvivorBar();
            refreshResourceBar();
            discoveredRooms(myGame);
            updateColorRoom();
            myGame.increaseRound();
            refreshMiniMenu();
            myGame.getSurvivorManager().checkLoseGame();
            myGame.getRoomManager().checkWinGame();
        }


        if (isXWasClicked && isGameLose) {
            endRoundButton.setDisabled(true);
            isGameLose = false;
            isXWasClicked = false;
            displayInfoBox(stage, "You lost, unfortunately you lost all survivors :c", Mark.ERROR, () -> {
                ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new SplashToNextScreen(Constants.WhereSplashGo.LOSE, myGame));
            });
        }

        if (isXWasClicked && !isGameLose && isGameWin) {
            endRoundButton.setDisabled(true);
            isGameWin = false;
            isXWasClicked = false;
            displayInfoBox(stage, "Yeyy you Win! Good job! all the surviving survivors are happy c:", Mark.INFO, () -> {
                ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new SplashToNextScreen(Constants.WhereSplashGo.WIN, myGame));
            });
        }

        if(addTask != null) {
            if (isChooseRoomVisible) {
                addTask.setDisabled(true);
            } else {
                addTask.setDisabled(false);
            }
        }

        if(addEQ != null) {
            if (isChooseRoomVisible) {
                addEQ.setDisabled(true);
                tooltipAddEQ.getActor().setText("Assign additional equipment to the survivor or train him as a Worker by giving him tools. \n\n" +
                    "[RED] A survivor cannot receive additional EQ or tools if he is in the process of choosing a room.");
            } else {
                addEQ.setDisabled(false);
                tooltipAddEQ.getActor().setText("Assign additional equipment to the survivor or train him as a Worker by giving him tools.");
            }
        }


    }

    public static void openMenu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            menuTable.setVisible(isMenuOpen);
        }
    }

    private Table showSurvivorInfo(Survivor survivor){
        addEqAndTaskTable.clear();
        Table info = new Table();

        Image icon = new Image(new TextureRegionDrawable(new TextureRegion( new Texture(survivor.getImgPath()))));
        info.add(icon).size(256,256).expandX().fillX().center().pad(2).colspan(2).row();

        Image energyIcon = new Image(survivor.getEnergyIconDrawable());
        info.add(energyIcon).size(224,32).expandX().fillX().center().pad(2).colspan(2).row();

        Label name = new Label("Name: " + survivor.getName(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        info.add(name).expandX().fillX().center().pad(2).colspan(2).row();

        Label age = new Label("Age: " + survivor.getAge(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        info.add(age).expandX().fillX().center().pad(2).colspan(2).row();

        Label profession = new Label("Profession: " + survivor.getProfession(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        info.add(profession).expandX().fillX().center().pad(2).colspan(2).row();

        Label description = new Label(survivor.getProfileInformation(), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        description.setWrap(true);
        description.setWidth(128);
        info.add(description).expandX().fillX().center().pad(2).colspan(2).row();

        Label isTaskAssigned = new Label("Task: ---" , DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

        if(survivor.getTask() != null) {
            isTaskAssigned = new Label("Task: " + survivor.getTask().getTask(), DigOutGame.skin.get("greenMediumFont", Label.LabelStyle.class));
            if(survivor.getTask().getTask().toString().length() > 10){
                isTaskAssigned.setFontScale(0.8f);
            }else{
                isTaskAssigned.setFontScale(1f);
            }

        }

        info.add(isTaskAssigned).expandX().fillX().center().pad(2).colspan(2).row();

        Label placeOfAssignment = new Label("Allocation: ---" , DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

        if(survivor.getTask() != null && survivor.getTask().getCoordinateOfRoom() != null) {
            placeOfAssignment = new Label("Allocation: " + survivor.getTask().getCoordinateOfRoom().getX() + ", " + survivor.getTask().getCoordinateOfRoom().getY(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        }

        info.add(placeOfAssignment).expandX().fillX().center().pad(2).colspan(2);

        info.row().space(5);

        if(survivor.getEquipment() == null)
        {

            addEQ = new TextButton("+", DigOutGame.skinButton);
            tooltipAddEQ = new TextTooltip("Assign additional equipment to the survivor or train him as a Worker by giving him tools.", DigOutGame.skin);

            addEQ.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Table equipmentTable = tableAddEq(survivor, stage, myGame);
                    equipmentTable.setVisible(true);
                    addEqAndTaskTable.clear();
                    addEqAndTaskTable.add(equipmentTable).pad(5);
                }
            });

            if(survivor.getTask() != null) {
                addEQ.setDisabled(true);
                tooltipAddEQ.getActor().setText("Assign additional equipment to the survivor or train him as a Worker by giving him tools. \n\n" +
                    "[RED] A survivor cannot receive additional EQ or tools if they are assigned a task.");
            }else{
                addEQ.setDisabled(false);
                tooltipAddEQ.getActor().setText("Assign additional equipment to the survivor or train him as a Worker by giving him tools.");
            }

            tooltipAddEQ.setInstant(true);
            addEQ.addListener(tooltipAddEQ);

            info.add(addEQ).size(64, 64).expandX().fillX().center().pad(2);
        }else{
            Image EQIcon = new Image(new Texture(Gdx.files.internal(survivor.getEquipment().getIconPath())));
            info.add(EQIcon).size(64,64).expandX().fillX().center().pad(2);
        }


        if(survivor.getTask() == null)
        {
            addTask = new TextButton("Add Task", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
            addTask.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Table addTaskTable = addChoseTask(stage, survivor, myGame);
                    addTaskTable.setVisible(true);
                    addEqAndTaskTable.clear();
                    addEqAndTaskTable.add(addTaskTable).pad(5);
                }
            });

            info.add(addTask).size(150, 64).expandX().fillX().center().pad(2);
        }else{
            Button unpinTask = new TextButton("Unpin Task", DigOutGame.skinButton.get("small-red", TextButton.TextButtonStyle.class));
            unpinTask.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    displayConfirmBox(stage, "Do you want to unpin a task from a survivor?", confirmed -> {
                        if (confirmed) {
                            tempSurvivor = survivor;
                            logs(DateLogs.LogType.INFO, myGame.getGameId(), "Survivor: " + survivor.getName() + ", Unpin task: " + survivor.getTask().getTask(), null);
                            backResources(survivor, myGame);
                            myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setAmountOfSurvivors(
                                myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).getAmountOfSurvivors() - 1
                            );
                            myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updateSpace(myGame);
                            myGame.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).updatePicture();
                            survivor.setTask(null);
                            needsRefreshAfterAddTask = true;
                            survivor.changeImgForNotWork();
                        }
                    });
                }
            });
            info.add(unpinTask).size(150, 64).expandX().fillX().center().pad(2);
        }

        return info;
    }

    public static void backResources(Survivor survivor, MyGame myGame) {
        myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
                - survivor.getTask().getCost().getMaterials());

        myGame.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount()
                - survivor.getTask().getCost().getTools());

        myGame.getResourceManager().getResource(Constants.Resources.FOOD).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount()
                - survivor.getTask().getCost().getFood());

        myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).setAllocatedAmount
            (myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount()
                - (survivor.getTask().getCost().isElectricityRequired() ? 1 : 0));
    }

    private void changeToInfoSurvivor(){
            infoTable.clear();
            infoTable.add(survivorInfoTable).expand().fill().center();

            infoButton.setDisabled(true);
            eqButton.setDisabled(false);
            diaryButton.setDisabled(false);
    }

    private void changeToDiary(){
        infoTable.clear();
        populateDiaryBox();
        infoTable.add(diaryInfoTable).expand().fill().center();

        infoButton.setDisabled(false);
        eqButton.setDisabled(false);
        diaryButton.setDisabled(true);
    }

    public void discoveredRooms(MyGame myGame) {
        Map<Coordinate, Room> rooms = myGame.getRoomManager().getRooms();
        List<Coordinate> coordinatesToProcess = new ArrayList<>();

        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if (room.getType() == Constants.RoomType.ROOM_TO_ARRANGE || room.getBuildUp() == Constants.Buildings.ELEVATOR ||
                (room == myGame.getRoomManager().getExitRoom() && myGame.getRoomManager().getExitRoom().isDiscovered())) {

                if (room.getType() == Constants.RoomType.EXIT_TYPE && !room.isDiscovered()) {
                    System.out.println("Wywołane przy cooradach: " + room.getCoordinates().getX() + ", " + room.getCoordinates().getY());
                    continue;
                }

                coordinatesToProcess.add(room.getCoordinates());
            }
        }

        for (Coordinate coordinate : coordinatesToProcess) {
            myGame.getRoomManager().makeAbleToDiscoveredNearestRooms(coordinate, myGame);
        }
    }


    public void colorTileAtCoordinateBaseRoom(Room room) {
        Coordinate coordinate = room.getCoordinates();
        Table tile = gameTable.get(coordinate);
        tile.setSize(roomWidth,roomHeight);

        if (tile != null) {
            tile.setBackground(DigOutGame.skinRoom.getDrawable(room.getActualPicture()));
        } else {
            logs(DateLogs.LogType.INFO, myGame.getGameId(), "Error: No tile found at coordinate: " + coordinate, null);
        }
    }

    public void updateColorRoom(){
        Map<Coordinate, Room> rooms = myGame.getRoomManager().getRooms();

        for (Map.Entry<Coordinate, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();

            if(room.getType() == Constants.RoomType.EXIT_TYPE && !room.isAbleToDiscover())
                continue;

            colorTileAtCoordinateBaseRoom(room);

        }
    }

    public static void colorTileAtCoordinate(Coordinate coordinate, Room room, String name) {

        Table tile = gameTable.get(coordinate);
        tile.setSize(roomWidth,roomHeight);

        if (tile != null) {
            tile.setBackground(DigOutGame.skinRoom.getDrawable(name));
        } else {
            logs(DateLogs.LogType.INFO, myGame.getGameId(), "Error: No tile found at coordinate: " + coordinate, null);
        }
    }

    private void populateEqInfoTable() {
        equipmentStatus = myGame.getEquipmentManager().getEquipmentStatus();
        equipmentName = myGame.getEquipmentManager().getEquipmentNames();
        equipmentDescription = myGame.getEquipmentManager().getEquipmentDescription();

        eqInfoTable.clear();

        float infoHeight = Gdx.graphics.getHeight() * 10 / 14f / 20;

        for (Map.Entry<Constants.Equipment, Integer> entry : equipmentStatus.entrySet()) {
            Constants.Equipment equipment = entry.getKey();
            Integer amount = entry.getValue();
            String name = equipmentName.get(equipment);
            String description = equipmentDescription.get(equipment);

            Image equipmentIcon = new Image(new Texture(Gdx.files.internal(myGame.getEquipmentManager().getEquipment(equipment).getIconPath())));
            equipmentIcon.setScaling(Scaling.none);
            equipmentIcon.setSize(64, 64);

            TextTooltip tooltip = new TextTooltip(description, DigOutGame.skin);
            tooltip.setInstant(true);
            equipmentIcon.addListener(tooltip);

            String formattedAmount = String.format("%02d", amount);
            Label equipmentAmountLabel = new Label(formattedAmount, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
            Label equipmentNameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

            eqInfoTable.add(equipmentIcon).size(64, 64).expand().fill().center().height(infoHeight);
            eqInfoTable.add(equipmentAmountLabel).expand().fill().center().height(infoHeight);
            eqInfoTable.row();
            eqInfoTable.add(equipmentNameLabel).colspan(2).expandX().center().padBottom(5);
            eqInfoTable.row();
        }

        Label descriptionLabel = new Label("Additional equipment can be made in the TINKER ROOM or found during DIG OUT (Not counting the [PURPLE]PICKAXE[WHITE], it can only be crafted).\n" +
            "Each piece of equipment can be assigned to a survivor, but cannot be recovered, so assign it well.", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        eqInfoTable.add(descriptionLabel).colspan(2).expand().fill().center().height(infoHeight*3).padTop(2);
        //eqInfoTable.debug();

    }

    private void refreshEqInfoTable() {
        populateEqInfoTable();
    }

    private void populateSurvivorBar() {
        survivorsTable.clear();

        float survivorBoxWidth = 150f;
        float survivorBoxHeight = Gdx.graphics.getHeight()/14f;

        survivorsTable.align(Align.left | Align.top);

        int survivorCount = myGame.getSurvivorManager().getAllSurvivors().size();

        for (int i = 0; i < survivorCount; i++) {
            Survivor survivor = myGame.getSurvivorManager().getAllSurvivors().toArray(new Survivor[0])[i];


            Table survivorEntry = new Table();
            Table survivorEntryNames = new Table();
            Table survivorEntryFull = new Table();

            Image icon = new Image(new TextureRegionDrawable(new TextureRegion( new Texture(survivor.getImgPath()))));
            survivorEntry.add(icon).size(64, 64).padLeft(20);

            Label survivorNameLabel = new Label(survivor.getName(), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
            survivorEntryNames.add(survivorNameLabel).size(86, 48).padRight(5).padLeft(5).center().row();

            Image energyIcon = new Image(survivor.getEnergyIconDrawable());
            survivorEntryNames.add(energyIcon).size(64, 16).left().padLeft(5).padBottom(3);

            if(survivorCount % 2 == 1 && survivorCount / 2 == i-1){
                survivorsTable.row();
            }

            if(survivorCount % 2 == 0 && survivorCount / 2 == i){
                survivorsTable.row();
            }

            if(survivor.getProfession() == Constants.Survivors.COOK || survivor.getProfession() == Constants.Survivors.UNTRAINED){
                survivorNameLabel.setColor(Color.BLACK);
            }

            survivorEntryFull.setBackground(DigOutGame.skinSurvivorBox.getDrawable( "box." + survivor.getProfession()));
            survivorEntryFull.add(survivorEntry);
            survivorEntryFull.add(survivorEntryNames);

            survivorEntryFull.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    survivorInfoTable.clear();
                    survivorInfoTable.add(showSurvivorInfo(survivor));
                    changeToInfoSurvivor();
                }
            });

            survivorsTable.add(survivorEntryFull).size(survivorBoxWidth, survivorBoxHeight);

        }
    }

    private void refreshSurvivorBar() {
        populateSurvivorBar();
    }

    private void refreshSurvivorInfoTable(Survivor survivor) {
        survivorInfoTable.clear();  // Wyczyść tabelę
        survivorInfoTable.add(showSurvivorInfo(survivor));  // Załaduj ponownie informacje
    }

    private void populateDiaryBox(){
        diaryInfoTable.clear();
        Table diaryContentTable = new Table();
        diaryContentTable.setBackground(DigOutGame.skin.getDrawable("diary"));

        Label nameLabel = new Label("[BLACK]DIARY", DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

        Label descriptionLabel = new Label("[BLACK]" + myGame.getDiaryManager().updateDiaryBox()
            , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        descriptionLabel.setWrap(true);

        diaryContentTable.add(nameLabel).pad(10).expandX().center().row();
        diaryContentTable.add(descriptionLabel).expandX().fillX().pad(10).row();

        ScrollPane scrollPane = new ScrollPane(diaryContentTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.layout();

        diaryInfoTable.add(scrollPane).expand().fill().pad(15).row();

        scrollPane.setScrollY(scrollPane.getMaxY());

    }

    private void refreshDiaryBox() {
        populateDiaryBox();
    }

    private void populateResourceBar(){
        resourcesTable.clear();
        resourcesTable.setBackground(DigOutGame.skin.getDrawable("box.wood"));

        resourceStatus = myGame.getResourceManager().getResourceStatus();
        resourceAllocatedStatus = myGame.getResourceManager().getAllocatedResourcesStatus();
        resourceName = myGame.getResourceManager().getResourceNames();
        resourceDescription = myGame.getResourceManager().getResourceDescription();

        for (Map.Entry<Constants.Resources, Integer> entry : resourceStatus.entrySet()) {
            Constants.Resources resource = entry.getKey();
            int allocatedResource =  entry.getValue() - resourceAllocatedStatus.get(resource);
            String description = resourceDescription.get(resource);
            String name = resourceName.get(resource);

            Table resourceEntry = new Table();
            Table resourceEntryNames = new Table();
            Table resourceEntryFull = new Table();

            Image resourceIcon = new Image(new Texture(Gdx.files.internal("resources/" + resource.toString() + "_icon_64.png")));
            resourceIcon.setScaling(Scaling.none);
            resourceIcon.setSize(64, 64);

            TextTooltip imageTooltip = new TextTooltip(description, DigOutGame.skin);
            imageTooltip.setInstant(true);
            resourceIcon.addListener(imageTooltip);

            String formattedAmount = String.format("%02d", entry.getValue());
            Label resourceAmountLabel = new Label(formattedAmount, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

            Label stringLabel = new Label("[RED] -> ", DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

            String formattedAllocatedAmount = "[GRAY]" +  String.format("%02d", allocatedResource);
            Label resourceAllocatedAmountLabel = new Label(formattedAllocatedAmount, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

            TextTooltip allocatedAmountTooltip = new TextTooltip("Resource value after assigning tasks.", DigOutGame.skin);
            allocatedAmountTooltip.setInstant(true);
            resourceAllocatedAmountLabel.addListener(allocatedAmountTooltip);

            Label resourceNameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

            resourceEntry.add(resourceIcon).size(64, 64).expandX().fillX().center();
            resourceEntryNames.add(resourceAmountLabel).expandX().fillX().pad(5).center();
            resourceEntryNames.add(stringLabel).expandX().fillX().center().pad(5);
            resourceEntryNames.add(resourceAllocatedAmountLabel).expandX().fillX().center().pad(5).row();
            resourceEntryNames.add(resourceNameLabel).colspan(3).expandX().fillX().center().pad(5);

            resourceEntryFull.add(resourceEntry);
            resourceEntryFull.add(resourceEntryNames);

            resourcesTable.add(resourceEntryFull).expandX().fillX();
        }
    }

    private void refreshResourceBar() {
        populateResourceBar();
    }

    public static void setTouchableEnabledGameRooms(){
        for (Map.Entry<Coordinate, Table> entry : gameTable.entrySet()) {
            entry.getValue().setTouchable(Touchable.enabled);
        }
    }

    public static void setTouchableDisabledGameRooms(){
        for (Map.Entry<Coordinate, Table> entry : gameTable.entrySet()) {
            entry.getValue().setTouchable(Touchable.disabled);
        }
    }

    private void checkAbleToEndRound(MyGame myGame, TextTooltip tooltip, Button button){
        boolean a = true;
        a = myGame.getSurvivorManager().checkIfAllSurvivorHaveTask();
        button.setDisabled(!a);

        System.out.println(a);

        showTooltipEndRound(tooltip, button);
    }

    private void showTooltipEndRound(TextTooltip tooltip, Button button){
        String oxygen;

        if(myGame.getSurvivorManager().hasLevelWithMoreThanFourSurvivors(myGame)){
            oxygen = "\n\n[ORANGE]There are 4 or more survivors in one or more levels, which will affect their energies." +
                " You can change this by building AIR_PUMP or giving survivors oxygen masks.";
        }else{
            oxygen = "";
        }

        if(button.isDisabled()){
            tooltip.getActor().setText("To end the round, all survivors are to be assigned tasks.\n" +
                "\n" +
                "[RED]Not all survivors are assigned tasks." + oxygen);
        }else{
            tooltip.getActor().setText("You can end the round so that the survivors complete their tasks." + oxygen);
        }
    }

    private void populateMiniMenu(){
        miniMenuTable.clear();
        miniMenuTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));
        roundNumber = myGame.getRound();
        Label statusRoundGame = new Label("Day: " + roundNumber, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

        endRoundButton = new TextButton("END ROUND", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        endRoundButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                displayConfirmBox(stage, "Are you sure that all survivors completed their assigned tasks?", confirmed -> {
                    if (confirmed) {
                        needsRefreshAfterEndRound = true;
                    }
                });
            }
        });

        endRoundTooltip = new TextTooltip("To end the round, all survivors are to be assigned tasks.\n" +
            "\n" +
            "[RED]Not all survivors are assigned tasks.", DigOutGame.skin);
        endRoundTooltip.setInstant(true);
        endRoundButton.addListener(endRoundTooltip);

        checkAbleToEndRound(myGame, endRoundTooltip, endRoundButton);

        Button menuButton = new TextButton("MENU", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menuTable.setVisible(true);
                isMenuOpen = true;
            }
        });
        fps = new Label(null, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        endRoundButton.setSize(100, 40);
        menuButton.setSize(50, 20);

        miniMenuTable.add(statusRoundGame).expandX().pad(5).center().height(Gdx.graphics.getHeight()*3/70f).colspan(2).row();
        miniMenuTable.add(endRoundButton).expandX().fillX().center().pad(5).height(Gdx.graphics.getHeight()*6/70f).colspan(2).row();
        miniMenuTable.add(fps).expandX().fillX().left().height(Gdx.graphics.getHeight()*3/70f).pad(5);
        miniMenuTable.add(menuButton).expandX().fillX().center().height(Gdx.graphics.getHeight()*3/70f).pad(5);
    }

    private void refreshMiniMenu() {
        populateMiniMenu();
    }

    public static boolean isNeedsRefreshAfterAddEQ() {
        return needsRefreshAfterAddEQ;
    }

    public static void setNeedsRefreshAfterAddEQ(boolean needsRefreshAfterAddEQ1) {
        needsRefreshAfterAddEQ = needsRefreshAfterAddEQ1;
    }

    public static boolean isNeedsRefreshAfterAddTask() {
        return needsRefreshAfterAddTask;
    }

    public static void setNeedsRefreshAfterAddTask(boolean needsRefreshAfterAddTask1) {
        needsRefreshAfterAddTask = needsRefreshAfterAddTask1;
    }

    public static boolean isNeedsRefreshAfterEndRound() {
        return needsRefreshAfterEndRound;
    }

    public static void setNeedsRefreshAfterEndRound(boolean needsRefreshAfterEndRound1) {
        needsRefreshAfterEndRound = needsRefreshAfterEndRound1;
    }

    public static boolean isGameWin() {
        return isGameWin;
    }

    public static void setGameWin(boolean gameWin) {
        isGameWin = gameWin;
    }

    public static boolean isGameLose() {
        return isGameLose;
    }

    public static void setGameLose(boolean gameLose) {
        isGameLose = gameLose;
    }

    public static boolean isXWasClicked() {
        return isXWasClicked;
    }

    public static void setXWasClicked(boolean XWasClicked) {
        isXWasClicked = XWasClicked;
    }

    public static boolean isIsChooseRoomVisible() {
        return isChooseRoomVisible;
    }

    public static void setIsChooseRoomVisible(boolean chooseRoomVisible) {
        isChooseRoomVisible = chooseRoomVisible;
    }

    public static Survivor getTempSurvivor() {
        return tempSurvivor;
    }

    public static void setTempSurvivor(Survivor tempSurvivor1) {
        tempSurvivor = tempSurvivor1;
    }

    public static Table getAddEqAndTaskTable() {
        return addEqAndTaskTable;
    }

    public static void setAddEqAndTaskTable(Table addEqAndTaskTable1) {
        addEqAndTaskTable = addEqAndTaskTable1;
    }

    public static Table getOuterPinRoomTable() {
        return outerPinRoomTable;
    }

    public static void setOuterPinRoomTable(Table outerPinRoomTable1) {
        outerPinRoomTable = outerPinRoomTable1;
    }

    public static Table getCenterTable() {
        return centerTable;
    }

    public static void setCenterTable(Table centerTable1) {
        centerTable = centerTable1;
    }

    public static Table getSurvivorsTable() {
        return survivorsTable;
    }

    public static void setSurvivorsTable(Table survivorsTable1) {
        survivorsTable = survivorsTable1;
    }

    public static Table getRoomsTable() {
        return roomsTable;
    }

    public static void setRoomsTable(Table roomsTable1) {
        roomsTable = roomsTable1;
    }

    public static boolean isIsMenuOpen() {
        return isMenuOpen;
    }

    public static void setIsMenuOpen(boolean isMenuOpen) {
        GameScreen.isMenuOpen = isMenuOpen;
    }

    public static boolean isIsInfoBoxOpen() {
        return isInfoBoxOpen;
    }

    public static void setIsInfoBoxOpen(boolean isInfoBoxOpen) {
        GameScreen.isInfoBoxOpen = isInfoBoxOpen;
    }

    public static Map<Coordinate, Table> getGameTable() {
        return gameTable;
    }

    public static void setGameTable(Map<Coordinate, Table> gameTable) {
        GameScreen.gameTable = gameTable;
    }

    public static Button getAddTask() {
        return addTask;
    }

    public static void setAddTask(Button addTask) {
        GameScreen.addTask = addTask;
    }
}
