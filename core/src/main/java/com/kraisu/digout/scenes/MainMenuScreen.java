package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.logs.DateLogs;

import static com.kraisu.digout.DigOutGame.TITLE;
import static com.kraisu.digout.DigOutGame.VERSION;
import static com.kraisu.digout.logs.DateLogs.logs;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Table table;
    private TextureAtlas atlas;
    private Label heading, footer;
    private TextButton buttonNewGame, buttonLoadGame, buttonSettings, buttonExit;


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //creating table
        table = new Table(DigOutGame.skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        //creating heading
        heading = new Label(TITLE, DigOutGame.skin.get("bigFont", LabelStyle.class));
        heading.setFontScale(5f);

        //creating footer
        footer = new Label("Version: " + VERSION, DigOutGame.skin.get("smallFont", LabelStyle.class));
        //footer.setFontScale(0.5f);

        //creatingButton
        buttonNewGame = new TextButton("NEW GAME", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        buttonNewGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGameNameScreen());
                logs(DateLogs.LogType.INFO, null, "Open new game screen", null);
            }
        });

        buttonLoadGame = new TextButton("LOAD GAME", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        buttonLoadGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGameScreen());
                logs(DateLogs.LogType.INFO, null,"Open load game screen", null);
            }
        });

        buttonSettings = new TextButton("SETTINGS", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        buttonSettings.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //((MyGame) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
                logs(DateLogs.LogType.INFO, null,"Open settings screen", null);
            }
        });

        //Blocker
        buttonSettings.setDisabled(true);

        buttonExit = new TextButton("EXIT", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        buttonExit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });


        //adding to table
        table.add(heading);
        table.row().space(80);
        table.add(buttonNewGame).width(Gdx.graphics.getWidth()/4f);
        table.row().space(80);
        table.add(buttonLoadGame).width(Gdx.graphics.getWidth()/4f);
        table.row().space(80);
        table.add(buttonSettings).width(Gdx.graphics.getWidth()/4f);
        table.row().space(80);
        table.add(buttonExit).width(Gdx.graphics.getWidth()/4f);
        table.row();
        table.add(footer).bottom().right();
        //table.debug(); //opcja debugowania

        //adding table to stage
        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        KeyUseScreen.resizeFullScreen();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        stage.act(v);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        //stage.getViewport().update(i, i1, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
    }
}
