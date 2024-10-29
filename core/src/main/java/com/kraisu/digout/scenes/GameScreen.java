package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.Game;
import com.kraisu.digout.genertor.Generators;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.rooms.Coordinate;
import com.kraisu.digout.survivor.Survivor;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.kraisu.digout.logs.DateLogs.logs;

public class GameScreen implements Screen {

    private Game game;
    private Stage stage;
    private Table outerTable, outerMenuTable, rightTable, centerTable, nameTable, resourcesTable,
        roomsTable, infoTable, infoButtonTable, survivorsTable, miniMenuTable, survivorInfoTable,
        eqInfoTable, diaryInfoTable, survivorRowTopTable, survivorRowBottomTable;
    private static Table menuTable;
    private Map<Constants.Resources, Integer> resourceStatus;
    private LinkedHashMap<Constants.Equipment, Integer> equipmentStatus;
    private Map<Constants.Resources, String> resourceName;
    private LinkedHashMap<Constants.Equipment, String> equipmentName, equipmentDescription;
    private LinkedHashMap<Constants.Resources, String> resourceDescription;
    private Map<Coordinate, Table> gameTable;

    private String name;
    private int roundNumber;

    private static boolean isMenuOpen;

    public GameScreen(Game game) {
        this.game = game;
        this.resourceStatus = new LinkedHashMap<>();
        this.resourceName = new LinkedHashMap<>();
        this.equipmentStatus = new LinkedHashMap<Constants.Equipment, Integer>();
        this.equipmentName = new LinkedHashMap<Constants.Equipment, String>();
        this.equipmentDescription = new LinkedHashMap<Constants.Equipment, String>();
        this.resourceDescription = new LinkedHashMap<Constants.Resources, String>();
        this.gameTable = new LinkedHashMap<>();
        this.isMenuOpen = false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        outerTable = new Table();
        outerTable.setFillParent(true);
        outerMenuTable = new Table();
        outerMenuTable.setFillParent(true);

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


        survivorRowTopTable = new Table();
        survivorRowBottomTable = new Table();
        survivorsTable = new Table();
        ScrollPane scrollPane = new ScrollPane(survivorsTable);
        scrollPane.setScrollingDisabled(false, false);

        miniMenuTable = new Table();
        menuTable = new Table();

        //menu table
        menuTable.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/3f);
        menuTable.setVisible(false);

        menuTable.setBackground(DigOutGame.skin.getDrawable("box.grey"));

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
        resourceStatus = game.getResourceManager().getResourceStatus();
        resourceName = game.getResourceManager().getResourceNames();
        resourceDescription = game.getResourceManager().getResourceDescription();

        for(Map.Entry<Constants.Resources, Integer> entry : resourceStatus.entrySet()) {
            Constants.Resources resource = entry.getKey();
            String description = resourceDescription.get(resource);

            Image resourceIcon = new Image(new Texture(Gdx.files.internal("assets/resources/" + entry.getKey().toString()+ "_icon_64.png")));
            resourceIcon.setScaling(Scaling.none);
            resourceIcon.setSize(64, 64);

            TextTooltip tooltip = new TextTooltip(description, DigOutGame.skin);
            tooltip.setInstant(true);
            //tooltip.getContainer().pad(5);
            resourceIcon.addListener(tooltip);



            String formattedAmount = String.format("%02d", entry.getValue());
            Label resourceAmountLabel = new Label(formattedAmount, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));

            resourcesTable.add(resourceIcon).size(64, 64).expandX().fillX().center();
            resourcesTable.add(resourceAmountLabel).expandX().fillX().center();
        }

        resourcesTable.row();

        for(Map.Entry<Constants.Resources, String> entry : resourceName.entrySet()) {
            Label resourceNameLabel = new Label(entry.getValue(), DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

            resourcesTable.add(resourceNameLabel).expandX().colspan(2).center();
        }


        //screen 10x10

        float roomWidth = Gdx.graphics.getWidth() * 4 / 5f / 10;
        float roomHeight = Gdx.graphics.getHeight() / 7f / 10;

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Coordinate tempCoordinate = new Coordinate(i, j);
                Table tempTable = new Table();
                tempTable.setSize(roomWidth, roomHeight);


                gameTable.put(tempCoordinate, tempTable);
            }
        }

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                Coordinate tempCoordinate = new Coordinate(i, j);

                Table tempTable = gameTable.get(tempCoordinate);

                if (tempTable == null) {
                    logs(DateLogs.LogType.INFO, game.getGameId(), "Error: tempTable is null for coordinate: " + tempCoordinate, null);
                } else {
                    roomsTable.add(tempTable).expand().fill();
                }
            }
            roomsTable.row();
        }



        //infoTable
        Button infoButton = new TextButton("Info", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));
        infoButton.setDisabled(true);
        Button eqButton = new TextButton("EQ", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));
        Button diaryButton = new TextButton("Diary", DigOutGame.skinButton.get("list-small", TextButton.TextButtonStyle.class));

        infoButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                infoTable.clear();
                infoTable.add(survivorInfoTable).expand().fill().center();

                infoButton.setDisabled(true);
                eqButton.setDisabled(false);
                diaryButton.setDisabled(false);
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
        eqInfoTable.setBackground(DigOutGame.skin.getDrawable("box.grey"));
        diaryInfoTable.setBackground(DigOutGame.skin.getDrawable("blackBox"));




        //eq table
        equipmentStatus = game.getEquipmentManager().getEquipmentStatus();
        equipmentName = game.getEquipmentManager().getEquipmentNames();
        equipmentDescription = game.getEquipmentManager().getEquipmentDescription();

        float infoHeight = Gdx.graphics.getHeight()*10/14f/12;

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
            //tooltip.getContainer().pad(5);
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




        //survivors bar
        float survivorBoxWidth = 150f;
        float survivorBoxHeight = 80f;

        survivorsTable.align(Align.left | Align.top);
        survivorRowBottomTable.align(Align.left);
        survivorRowTopTable.align(Align.left);

        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));
        game.getSurvivorManager().addSurvivor(Generators.generateNewSurvivors(game.getGameId(), Constants.Survivors.COOK));


        int survivorCount = game.getSurvivorManager().getAllSurvivors().size();



// Tworzymy nową tabelę dla ocalałych
        for (int i = 0; i < survivorCount; i++) {
            Survivor survivor = game.getSurvivorManager().getAllSurvivors().toArray(new Survivor[0])[i];

            // Tworzymy nową tabelę dla ocalałego
            Table survivorEntry = new Table();

            Image icon = new Image(new TextureRegionDrawable(new TextureRegion(survivor.getImg())));
            survivorEntry.add(icon).size(64, 64).padRight(10);

            Label survivorNameLabel = new Label(survivor.getName(), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
            survivorEntry.add(survivorNameLabel).padRight(10);

            Image energyIcon = new Image(survivor.getEnergyIconDrawable());
            survivorEntry.row();
            survivorEntry.add(energyIcon).colspan(2).size(64, 16).left();

            // Dodaj ocalałego do górnego wiersza
            if (i % 2 == 0) { // Co drugi ocalały do górnego wiersza
                survivorRowTopTable.add(survivorEntry).size(survivorBoxWidth, survivorBoxHeight).pad(3);
            } else { // Pozostałe do dolnego wiersza
                survivorRowBottomTable.add(survivorEntry).size(survivorBoxWidth, survivorBoxHeight).padLeft(3).padRight(3);
            }
        }

        // Dodaj tabele górnego i dolnego wiersza do głównej tabeli
        survivorsTable.add(survivorRowTopTable).row(); // Dodaj górny wiersz
        survivorsTable.add(survivorRowBottomTable); // Dodaj dolny wiersz


        //mini menu
        roundNumber = game.getRound();
        Label statusRoundGame = new Label("Day: " + roundNumber, DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
        Button menuButton = new TextButton("MENU", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menuTable.setVisible(true);
                isMenuOpen = true;
            }
        });

        miniMenuTable.add(statusRoundGame).expandX().pad(10).center().row();
        miniMenuTable.add(menuButton).expandX().fillX().center().pad(10);




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

        eqInfoTable.debug();
        menuTable.debug();
        outerTable.debug();
        centerTable.debug();
        rightTable.debug();
        resourcesTable.debug();
        roomsTable.debug();
        survivorsTable.debug();
        nameTable.debug();
        miniMenuTable.debug();

        stage.addActor(outerTable);
        stage.addActor(outerMenuTable);
    }

    public static void openMenu() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            menuTable.setVisible(isMenuOpen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        if(menuTable.isVisible()) {
            outerTable.setColor(0, 0, 0, 0.3f);
            outerTable.setTouchable(Touchable.disabled);
        } else {
            outerTable.setColor(0, 0, 0, 1f);
            outerTable.setTouchable(Touchable.enabled);
        }

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

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
}
