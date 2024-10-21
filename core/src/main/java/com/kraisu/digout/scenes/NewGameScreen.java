package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


import static com.kraisu.digout.scenes.UtilsScreen.fonts.black80Font;

public class NewGameScreen implements Screen {
    private Stage stage;
    private Label heading;

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = black80Font;
        labelStyle.fontColor = Color.WHITE;

        // Tworzenie nagłówka
        heading = new Label("New Game Screen", labelStyle);

        // Tworzenie tabeli, aby wyśrodkować nagłówek
        Table table = new Table();
        table.setFillParent(true); // Tabela zajmuje cały ekran
        table.add(heading).center(); // Wyśrodkowanie tekstu w tabeli

        // Dodanie tabeli do sceny
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Czarny kolor tła
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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
        // skin.dispose();
    }
}
