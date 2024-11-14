package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.managers.SurvivorManager;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.rooms.Room;
import com.kraisu.digout.survivor.Survivor;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.kraisu.digout.logs.DateLogs.logs;
import static com.kraisu.digout.scenes.uiHelps.*;

public class GameScreen implements Screen {

    private static Game game;
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
    private Button infoButton, eqButton, diaryButton;
    private Label fps;

    private String name;
    private int roundNumber;


    public static final float roomWidth = Gdx.graphics.getWidth() * 4 / 5f / 10;
    public static final float roomHeight = Gdx.graphics.getHeight() *11 / 14f / 10 - 2;
    private static boolean isMenuOpen, isInfoBoxOpen;
    public static boolean needsRefreshAfterAddEQ = false;
    public static boolean needsRefreshAfterAddTask = false;
    public static Survivor tempSurvivor = null;
    public static Table addEqAndTaskTable, outerPinRoomTable, centerTable, survivorsTable, roomsTable;
    public static Map<Coordinate, Table> gameTable = Map.of();


    public GameScreen(Game game) {
        this.game = game;
        this.resourceStatus = new LinkedHashMap<>();
        this.resourceName = new LinkedHashMap<>();
        this.equipmentStatus = new LinkedHashMap<Constants.Equipment, Integer>();
        this.equipmentName = new LinkedHashMap<Constants.Equipment, String>();
        this.equipmentDescription = new LinkedHashMap<Constants.Equipment, String>();
        this.resourceDescription = new LinkedHashMap<Constants.Resources, String>();
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
                //TODO logika zapisu gry FAZA 2/3
            }
        });

        //TODO Dodać slidera mojego
        Slider volumeSlider = new Slider(0, 1, 0.1f, false, DigOutGame.uiskin);
        volumeSlider.setValue(game.getVolume());
        volumeSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setVolume(volumeSlider.getValue());
            }
        });

        Button exitButton = new TextButton("EXIT TO MAIN MENU", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        exitButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                ((com.badlogic.gdx.Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        menuTable.add(backToGameButton).pad(10).width(menuTable.getWidth()-20).colspan(2).row();
        menuTable.add(saveGameButton).pad(10).width(menuTable.getWidth()-20).colspan(2).row();
        menuTable.add(new Label("Volume", DigOutGame.skin.get("bigFont", Label.LabelStyle.class))).pad(10).expandX().center();
        menuTable.add(volumeSlider).pad(10).width(menuTable.getWidth()/2 - 20).left().row();
        menuTable.add(exitButton).pad(10).width(menuTable.getWidth()-20).colspan(2);

        outerMenuTable.add(menuTable);


        //name //TODO ograć to jak jest dłuższa nazwa
        nameTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));
        name = game.getPlayer().getName();
        Label nameLabel;
        int nameLength = name.length();

        if(nameLength <= 15)
            nameLabel = new Label(name, DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
        else if (nameLength <= 25)
            nameLabel = new Label(name, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        else
            nameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        nameLabel.setColor(Color.GOLD);
        nameTable.add(nameLabel).expandX().center().height(Gdx.graphics.getHeight()/14f);


        //resources bar
        game.getResourceManager().getResource(Constants.Resources.TOOLS).setTotalAmount(5);
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

//                tempTable.addListener(new InputListener() {
//                    @Override
//                    public boolean mouseMoved(InputEvent event, float x, float y) {
//                        tempTable.setColor(1, 1, 1, 0.3f);
//                        return true; // sygnalizuje, że zdarzenie zostało przetworzone
//                    }
//
//                    @Override
//                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                        // Przywróć domyślny kolor
//                        tempTable.setColor(1, 1, 1, 1); // domyślny kolor tła (pełna widoczność)
//                    }
//                });
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
                        game.getGameId(),
                        "Error: tempTable is null for coordinate: " + tempCoordinate, null);
                }
            }
            roomsTable.row();
        }

        colorTileAtCoordinateBaseRoom(game.getRoomManager().getBaseRoom());


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
                infoTable.clear();
                infoTable.add(diaryInfoTable).expand().fill().center();

                infoButton.setDisabled(false);
                eqButton.setDisabled(false);
                diaryButton.setDisabled(true);
            }
        });

        infoButtonTable.add(infoButton).expand().fill().center().padTop(0).height(Gdx.graphics.getHeight()/14f);
        infoButtonTable.add(eqButton).expand().fill().center().height(Gdx.graphics.getHeight()/14f);
        infoButtonTable.add(diaryButton).expand().fill().center().height(Gdx.graphics.getHeight()/14f);

        survivorInfoTable.setBackground(DigOutGame.skin.getDrawable("box"));
        eqInfoTable.setBackground(DigOutGame.skin.getDrawable("box.gray"));
        diaryInfoTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));



        //eq table
        game.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).setTotalAmount(5);
        refreshEqInfoTable();

        //addEqTable
        //addEqAndTaskTable.setSize(Gdx.graphics.getWidth() / 3f, Gdx.graphics.getHeight() / 2f);
        outerAddEqTable.add(addEqAndTaskTable);


        game.getSurvivorManager().addSurvivor(SurvivorManager.generateNewSurvivors(game, Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(SurvivorManager.generateNewSurvivors(game, Constants.Survivors.ENGINEER));
        game.getSurvivorManager().addSurvivor(SurvivorManager.generateNewSurvivors(game, Constants.Survivors.MINER));
        game.getSurvivorManager().addSurvivor(SurvivorManager.generateNewSurvivors(game, Constants.Survivors.UNTRAINED));

        //survivors bar
        float survivorBoxWidth = 150f;
        float survivorBoxHeight = Gdx.graphics.getHeight()/14f;

        survivorsTable.align(Align.left | Align.top);

        int survivorCount = game.getSurvivorManager().getAllSurvivors().size();

        for (int i = 0; i < survivorCount; i++) {
            Survivor survivor = game.getSurvivorManager().getAllSurvivors().toArray(new Survivor[0])[i];


            Table survivorEntry = new Table();
            Table survivorEntryNames = new Table();
            Table survivorEntryFull = new Table();

            Image icon = new Image(new TextureRegionDrawable(new TextureRegion(survivor.getImg())));
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
        miniMenuTable.setBackground(DigOutGame.skin.getDrawable("box.dark"));
        roundNumber = game.getRound();
        Label statusRoundGame = new Label("Day: " + roundNumber, DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
        Button menuButton = new TextButton("MENU", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menuTable.setVisible(true);
                isMenuOpen = true;
            }
        });
        fps = new Label(null, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        miniMenuTable.add(statusRoundGame).expandX().pad(10).center().row();
        miniMenuTable.add(menuButton).expandX().fillX().center().pad(10).row();
        miniMenuTable.add(fps).expandX().fillX().left().pad(10).row();





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

        //this.game = game;
        //refreshEqInfoTable();

        //game.getSurvivorManager().showDisable();

        if (needsRefreshAfterAddEQ) {
            refreshEqInfoTable();
            refreshSurvivorBar();
            refreshResourceBar();
            survivorInfoTable.clear();
            survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
            needsRefreshAfterAddEQ = false;
            survivorsTable.setTouchable(Touchable.enabled);
        }

        if(needsRefreshAfterAddTask){
            refreshSurvivorBar();
            refreshResourceBar();
            survivorInfoTable.clear();
            survivorInfoTable.add(showSurvivorInfo(tempSurvivor));
            needsRefreshAfterAddTask = false;
            survivorsTable.setTouchable(Touchable.enabled);
        }

        if(menuTable.isVisible()) {
            outerTable.setColor(0, 0, 0, 0.3f);
            outerTable.setTouchable(Touchable.disabled);
        } else {
            outerTable.setColor(0, 0, 0, 1f);
            outerTable.setTouchable(Touchable.enabled);
        }

        game.getRoomManager().makeAbleToDiscoveredNearestRooms(game.getRoomManager().getBaseRoom().getCoordinates(), game.getGameId());

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

    public static void openMenu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            menuTable.setVisible(isMenuOpen);
        }
    }

    private Table showSurvivorInfo(Survivor survivor){
        addEqAndTaskTable.clear();
        Table info = new Table();

        Image icon = new Image(new TextureRegionDrawable(new TextureRegion(survivor.getImg())));
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

        if(survivor.getTask() != null)
            isTaskAssigned = new Label("Task: " + survivor.getTask().getTask(), DigOutGame.skin.get("greenMediumFont", Label.LabelStyle.class));

        info.add(isTaskAssigned).expandX().fillX().center().pad(2).colspan(2).row();

        Label placeOfAssignment = new Label("Place of Assignemnt: ---" , DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

        if(survivor.getTask() != null && survivor.getTask().getCoordinateOfRoom() != null) {
            placeOfAssignment = new Label("Place of Assignemnt: " + survivor.getTask().getCoordinateOfRoom().getX() + ", " + survivor.getTask().getCoordinateOfRoom().getY(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        }

        info.add(placeOfAssignment).expandX().fillX().center().pad(2).colspan(2);

        info.row().space(5);

        if(survivor.getEquipment() == null)
        {

            Button addEQ = new TextButton("+", DigOutGame.skinButton);
            addEQ.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Table equipmentTable = tableAddEq(survivor, stage, game);
                    equipmentTable.setVisible(true);
                    addEqAndTaskTable.clear();
                    addEqAndTaskTable.add(equipmentTable).pad(5);
                }
            });

            info.add(addEQ).size(64, 64).expandX().fillX().center().pad(2);
        }else{
            Image EQIcon = new Image(new Texture(Gdx.files.internal(survivor.getEquipment().getIconPath())));
            info.add(EQIcon).size(64,64).expandX().fillX().center().pad(2);
        }


        if(survivor.getTask() == null)
        {
            Button addTask = new TextButton("Add Task", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
            addTask.addListener(new ChangeListener() {
                public void changed(ChangeEvent event, Actor actor) {
                    Table addTaskTable = addChoseTask(stage, survivor, game);
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
                            logs(DateLogs.LogType.INFO, game.getGameId(), "Survivor: " + survivor.getName() + ", Unpin task: " + survivor.getTask().getTask(), null);
                            backResources(survivor, game);
                            game.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).setFull(false);
                            game.getRoomManager().getRoom(survivor.getTask().getCoordinateOfRoom()).changeImgForNotWork();
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

    public static void backResources(Survivor survivor, Game game) {
        game.getResourceManager().getResource(Constants.Resources.MATERIALS).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.MATERIALS).getAllocatedAmount()
                - survivor.getTask().getCost().getMaterials());

        game.getResourceManager().getResource(Constants.Resources.TOOLS).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.TOOLS).getAllocatedAmount()
                - survivor.getTask().getCost().getTools());

        game.getResourceManager().getResource(Constants.Resources.FOOD).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.FOOD).getAllocatedAmount()
                - survivor.getTask().getCost().getFood());

        game.getResourceManager().getResource(Constants.Resources.ELECTRICITY).setAllocatedAmount
            (game.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getAllocatedAmount()
                - (survivor.getTask().getCost().isElectricityRequired() ? 1 : 0));
    }

    private void changeToInfoSurvivor(){
            infoTable.clear();
            infoTable.add(survivorInfoTable).expand().fill().center();

            infoButton.setDisabled(true);
            eqButton.setDisabled(false);
            diaryButton.setDisabled(false);
    }

    public void colorTileAtCoordinateBaseRoom(Room room) {
        Coordinate coordinate = room.getCoordinates();
        Table tile = gameTable.get(coordinate);
        tile.setSize(roomWidth,roomHeight);

        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
        shapeRenderer.end();

        if (tile != null) {
            tile.setBackground(room.getRoomSkins().getDrawable("BaseRoom"));
        } else {
            logs(DateLogs.LogType.INFO, game.getGameId(), "Error: No tile found at coordinate: " + coordinate, null);
        }
    }

    public static void colorTileAtCoordinate(Coordinate coordinate, Room room, String name) {

        Table tile = gameTable.get(coordinate);
        tile.setSize(roomWidth,roomHeight);
        ShapeRenderer shapeRenderer = new ShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
        shapeRenderer.end();


        if (tile != null) {
            tile.setBackground(room.getRoomSkins().getDrawable(name));
        } else {
            logs(DateLogs.LogType.INFO, game.getGameId(), "Error: No tile found at coordinate: " + coordinate, null);
        }
    }

//    public void colorTileAtCoordinate(Room room) {
//        Coordinate coordinate = room.getCoordinates();
//        Table tile = gameTable.get(coordinate);
//        tile.setSize(roomWidth, roomHeight);
//
//        if (tile != null) {
//            String drawableName = getDrawableNameForRoomType(room.getType());
//            tile.setBackground(room.getRoomSkins().getDrawable(drawableName));
//        } else {
//            logs(DateLogs.LogType.INFO, game.getGameId(), "Error: No tile found at coordinate: " + coordinate, null);
//        }
//    }
//
//    private String getDrawableNameForRoomType(Constants.RoomType type) {
//        switch (type) {
//            case HARD_ROOK_TYPE:
//                return "HARD_ROOK_TYPE";
//            case LIGHT_ROOK_TYPE:
//                return "LIGHT_ROOK_TYPE";
//            case BASE_TYPE:
//                return "BaseRoom";
//
//            default:
//                return "DefaultRoom"; // Domyślny obrazek
//        }
//    }

    private void populateEqInfoTable() {
        equipmentStatus = game.getEquipmentManager().getEquipmentStatus();
        equipmentName = game.getEquipmentManager().getEquipmentNames();
        equipmentDescription = game.getEquipmentManager().getEquipmentDescription();

        eqInfoTable.clear();

        float infoHeight = Gdx.graphics.getHeight() * 10 / 14f / 12;

        for (Map.Entry<Constants.Equipment, Integer> entry : equipmentStatus.entrySet()) {
            Constants.Equipment equipment = entry.getKey();
            Integer amount = entry.getValue();
            String name = equipmentName.get(equipment);
            String description = equipmentDescription.get(equipment);

            Image equipmentIcon = new Image(new Texture(Gdx.files.internal("assets/equipment/" + equipment.toString() + "_icon_64.png")));
            equipmentIcon.setScaling(Scaling.none);
            equipmentIcon.setSize(64, 64);

            TextTooltip tooltip = new TextTooltip(description, DigOutGame.skin);
            tooltip.setInstant(true);
            equipmentIcon.addListener(tooltip);

            String formattedAmount = String.format("%02d", amount);
            Label equipmentAmountLabel = new Label(formattedAmount, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
            Label equipmentNameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

            eqInfoTable.add(equipmentIcon).size(64, 64).expand().fill().center().height(infoHeight).padTop(10);
            eqInfoTable.add(equipmentAmountLabel).expand().fill().center().height(infoHeight).padTop(10);
            eqInfoTable.row();
            eqInfoTable.add(equipmentNameLabel).colspan(2).expandX().center().padBottom(infoHeight);
            eqInfoTable.row();
        }
    }

    private void refreshEqInfoTable() {
        populateEqInfoTable();
    }

    private void populateSurvivorBar() {
        survivorsTable.clear();

        float survivorBoxWidth = 150f;
        float survivorBoxHeight = Gdx.graphics.getHeight()/14f;

        survivorsTable.align(Align.left | Align.top);

        int survivorCount = game.getSurvivorManager().getAllSurvivors().size();

        for (int i = 0; i < survivorCount; i++) {
            Survivor survivor = game.getSurvivorManager().getAllSurvivors().toArray(new Survivor[0])[i];


            Table survivorEntry = new Table();
            Table survivorEntryNames = new Table();
            Table survivorEntryFull = new Table();

            Image icon = new Image(new TextureRegionDrawable(new TextureRegion(survivor.getImg())));
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

    private void populateResourceBar(){
        resourcesTable.clear();
        resourcesTable.setBackground(DigOutGame.skin.getDrawable("box.wood"));

        resourceStatus = game.getResourceManager().getResourceStatus();
        resourceAllocatedStatus = game.getResourceManager().getAllocatedResourcesStatus();
        resourceName = game.getResourceManager().getResourceNames();
        resourceDescription = game.getResourceManager().getResourceDescription();

        for (Map.Entry<Constants.Resources, Integer> entry : resourceStatus.entrySet()) {
            Constants.Resources resource = entry.getKey();
            int allocatedResource =  entry.getValue() - resourceAllocatedStatus.get(resource);
            String description = resourceDescription.get(resource);
            String name = resourceName.get(resource);

            Table resourceEntry = new Table();
            Table resourceEntryNames = new Table();
            Table resourceEntryFull = new Table();

            Image resourceIcon = new Image(new Texture(Gdx.files.internal("assets/resources/" + resource.toString() + "_icon_64.png")));
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

}
