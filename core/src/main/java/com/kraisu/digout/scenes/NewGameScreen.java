package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.Main;

public class NewGameScreen implements Screen {
    private final Main game;
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final Skin skin;
    private boolean isFullscreen = false;

    private final int gridWidth = 120;
    private final int gridHeight = 60;
    private final Color[][] cellColors = new Color[10][10]; // Tablica na kolory prostokątów

    private Table colorMenu;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public NewGameScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        shapeRenderer = new ShapeRenderer();

        // Inicjalizacja skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        initializeColorMenu();
    }

    private void initializeColorMenu() {
        colorMenu = new Table();
        colorMenu.setVisible(false);

        // Przycisk czerwony
        TextButton redButton = new TextButton("Czerwony", skin); // Użyj skin dla przycisków
        redButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (selectedRow != -1 && selectedCol != -1) {
                    cellColors[selectedRow][selectedCol] = Color.RED;
                }
                colorMenu.setVisible(false);
            }
        });

        // Przycisk biały
        TextButton whiteButton = new TextButton("Biały", skin);
        whiteButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (selectedRow != -1 && selectedCol != -1) {
                    cellColors[selectedRow][selectedCol] = Color.WHITE;
                }
                colorMenu.setVisible(false);
            }
        });

        // Przycisk czarny
        TextButton blackButton = new TextButton("Czarny", skin);
        blackButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (selectedRow != -1 && selectedCol != -1) {
                    cellColors[selectedRow][selectedCol] = Color.BLACK;
                }
                colorMenu.setVisible(false);
            }
        });

        colorMenu.add(redButton).pad(5);
        colorMenu.row();
        colorMenu.add(whiteButton).pad(5);
        colorMenu.row();
        colorMenu.add(blackButton).pad(5);
        colorMenu.pack();

        stage.addActor(colorMenu);
    }


    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            toggleFullscreen();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        int offsetX = (Gdx.graphics.getWidth() - gridWidth * 10) / 2;
        int offsetY = (Gdx.graphics.getHeight() - gridHeight * 10) / 2;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Rysowanie prostokątów z przypisanymi kolorami
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int x = offsetX + col * gridWidth;
                int y = offsetY + row * gridHeight;

                // Rysowanie koloru w środku prostokąta, jeśli jest przypisany
                if (cellColors[row][col] != null) {
                    shapeRenderer.setColor(cellColors[row][col]);
                    shapeRenderer.rect(x, y, gridWidth, gridHeight);
                }
            }
        }

        shapeRenderer.end();

        // Rysowanie obramowania siatki
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GRAY);
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int x = offsetX + col * gridWidth;
                int y = offsetY + row * gridHeight;
                shapeRenderer.rect(x, y, gridWidth, gridHeight);
            }
        }
        shapeRenderer.end();

        // Obsługa kliknięcia na prostokąt
        if (Gdx.input.justTouched()) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Odwrócenie osi Y

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    int x = offsetX + col * gridWidth;
                    int y = offsetY + row * gridHeight;

                    // Sprawdzenie, czy kliknięto w prostokąt
                    if (mouseX > x && mouseX < x + gridWidth && mouseY > y && mouseY < y + gridHeight) {
                        selectedRow = row;
                        selectedCol = col;
                        colorMenu.setPosition(mouseX, mouseY); // Wyświetlenie menu w pobliżu kliknięcia
                        colorMenu.setVisible(true);
                        break;
                    }
                }
            }
        }
    }

    private void toggleFullscreen() {
        if (isFullscreen) {
            Gdx.graphics.setWindowedMode(1280, 720);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        isFullscreen = !isFullscreen;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        shapeRenderer.dispose();
        skin.dispose(); // Pamiętaj, aby zwolnić skin przy zamykaniu
    }
}
