package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kraisu.digout.Main;

public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private boolean isFullscreen = true;

    public MainMenuScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background
        Texture backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        Drawable backgroundDrawable = new TextureRegionDrawable(backgroundTexture);
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        // Button styles
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = game.getSkin().getFont("default-font");
        buttonStyle.fontColor = Color.GOLD;
        buttonStyle.downFontColor = Color.RED;

        // Buttons
        TextButton newGameButton = new TextButton("Nowa Gra", buttonStyle);
        TextButton settingsButton = new TextButton("Ustawienia", buttonStyle);
        TextButton exitButton = new TextButton("Wyjście", buttonStyle);

        newGameButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.setScreen(new NewGameScreen(game));
            }
            return true;
        });
        settingsButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                // Placeholder for settings functionality
            }
            return true;
        });
        exitButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                Gdx.app.exit();
            }
            return true;
        });

        // Position buttons in a column
        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.add(newGameButton).pad(10).row();
        buttonTable.add(settingsButton).pad(10).row();
        buttonTable.add(exitButton).pad(10);

        stage.addActor(buttonTable);
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
    }

    private void toggleFullscreen() {
        if (isFullscreen) {
            Gdx.graphics.setWindowedMode(1280, 720); // Rozmiar okna, gdy wyłączony jest pełny ekran
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
    }
}
