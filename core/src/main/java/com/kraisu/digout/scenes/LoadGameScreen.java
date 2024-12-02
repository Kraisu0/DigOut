package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.logs.DateLogs;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.kraisu.digout.logs.DateLogs.logs;
import static com.kraisu.digout.scenes.uiHelps.displayInfoBox;
import static com.kraisu.digout.scenes.uiHelps.loadGame;

public class LoadGameScreen implements Screen {
    private Stage stage;
    private Table table, loadTable, outerTable;
    private Label heading;
    private Button buttonBackToMainMenu;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        outerTable = new Table();
        loadTable = new Table();

        outerTable.setFillParent(true);

        table.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/1.2f);
        table.setBackground(DigOutGame.skin.getDrawable("box.dark"));

        heading = new Label("GAME LOADER", DigOutGame.skin.get("hugeFont", Label.LabelStyle.class));

        loadTable.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/1.2f - 60);
        loadTable.top();

        ScrollPane scrollPane = new ScrollPane(loadTable);
        scrollPane.setScrollingDisabled(true, false);

        buttonBackToMainMenu = new TextButton("BACK TO MAIN MENU", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        buttonBackToMainMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                logs(DateLogs.LogType.INFO, null,"Open main menu screen", null);
            }
        });


        table.add(heading).padBottom(20).row();
        table.add(scrollPane).expand().fill().pad(20).row();
        table.add(buttonBackToMainMenu).width(Gdx.graphics.getWidth()/3f).pad(20);

        outerTable.add(table).center().width(table.getWidth()).height(table.getHeight());

        stage.addActor(outerTable);

        makeLoader();
    }

    @Override
    public void render(float delta) {
        KeyUseScreen.resizeFullScreen();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

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

    private void makeLoader() {
        FileHandle saveFolder = Gdx.files.local("saves/");

        if (!saveFolder.exists() || !saveFolder.isDirectory()) {
            Label noSavesLabel = new Label("No saves found!", DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
            loadTable.add(noSavesLabel).expand().center();
            return;
        }

        FileHandle[] metaFiles = saveFolder.list((file) -> file.getName().endsWith(".meta"));
        System.out.println("Meta files found: " + metaFiles.length);

        // Sortowanie plików po dacie modyfikacji (od najnowszego do najstarszego)
        Arrays.sort(metaFiles, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));

        for (FileHandle file : metaFiles) {
            System.out.println("Found meta file: " + file.name());
        }

        for (FileHandle metaFile : metaFiles) {
            String baseFileName = metaFile.nameWithoutExtension();
            SaveFileData saveFileData = readMetaFile(baseFileName);

            if (saveFileData != null) {
                addSaveTile(saveFileData, baseFileName);
            }
        }
    }


    private SaveFileData readMetaFile(String baseFileName) {
        // Otwórz plik .meta
        FileHandle metaFile = Gdx.files.local("saves/" + baseFileName + ".meta");

        // Sprawdź, czy plik istnieje
        if (!metaFile.exists()) {
            System.err.println("Meta file not found: " + baseFileName);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(metaFile.read())) {
            // Odczytaj obiekt SaveFileData
            SaveFileData saveFileData = (SaveFileData) ois.readObject();
            System.out.println("Successfully loaded meta file: " + baseFileName);
            return saveFileData;
        } catch (Exception e) {
            System.err.println("Error reading meta file: " + baseFileName + " - " + e.getMessage());
            return null;
        }
    }

    private void addSaveTile(SaveFileData saveFileData, String baseFileName) {
        Table tile = new Table();
        tile.setBackground(DigOutGame.skin.getDrawable("box.gray"));
        Label nameLabel;
        String name = saveFileData.getName();
        int nameLength = name.length();

        if (nameLength > 22) {
            name = name.substring(0, 19) + "...";  // Ucina nazwę do 27 znaków i dodaje "..."
        }

        if (name.length() <= 10)
            nameLabel = new Label(name, DigOutGame.skin.get("bigFont", Label.LabelStyle.class));
        else if (name.length() <= 17)
            nameLabel = new Label(name, DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        else
            nameLabel = new Label(name, DigOutGame.skin.get("smallFont", Label.LabelStyle.class));


        nameLabel.setColor(1f, 0.84f, 0f, 1f); // Złoty kolor

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Label dateLabel = new Label(dateFormat.format(saveFileData.getDate()), DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        dateLabel.setColor(1f, 1f, 1f, 1f); // Biały kolor

        TextButton loadButton = new TextButton("LOAD", DigOutGame.skinButton.get("small", TextButton.TextButtonStyle.class));
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGameSplashScreen(baseFileName));
                System.out.println("Loading game from: " + saveFileData.getName());
            }
        });

        tile.add(nameLabel).pad(10).left().expandX();
        tile.add(dateLabel).pad(10).left().expandX();
        tile.add(loadButton).pad(10).right();
        tile.row();

        loadTable.add(tile).expandX().fillX().padBottom(10).row();

        // Debugging line to check if the tile is being added
        System.out.println("Added save tile for: " + saveFileData.getName());
    }


    static class SaveFileData implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final Date date;

        public SaveFileData(String name, Date date) {
            this.name = name;
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public Date getDate() {
            return date;
        }
    }



}
