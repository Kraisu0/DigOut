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

import static com.kraisu.digout.DigOutGame.TITLE;
import static com.kraisu.digout.DigOutGame.VERSION;
import static com.kraisu.digout.scenes.UtilsScreen.fonts.*;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextureAtlas atlas;
    private Label heading, footer;
    private TextButton buttonNewGame, buttonLoadGame, buttonSettings, buttonExit;


    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Texture bgTexture = new Texture(Gdx.files.internal("img/background.png"));

        //creating atlas & skin
        atlas = new TextureAtlas(Gdx.files.internal("ui/buttonAtlas.atlas"));
        skin = new Skin(atlas);

        //creating table
        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        //creating heading
        heading = new Label(TITLE, new LabelStyle(black80Font, Color.WHITE));
        heading.setFontScale(1.5f);

        //creating footer
        footer = new Label("Version: " + VERSION, new LabelStyle(whiteFont, Color.WHITE));
        footer.setFontScale(0.5f);

        //creating buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonDOWN");
        textButtonStyle.down = skin.getDrawable("buttonUP");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = gold80Font;

        buttonNewGame = new TextButton("NEW GAME", textButtonStyle);
        buttonNewGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGameNameScreen());
                Gdx.app.log(TITLE, "Open new game screen");
            }
        });

        buttonLoadGame = new TextButton("LOAD GAME", textButtonStyle);
        buttonLoadGame.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //((Game) Gdx.app.getApplicationListener()).setScreen(new LoadGameScreen());
                Gdx.app.log(TITLE, "Open load game screen");
            }
        });

        buttonSettings = new TextButton("SETTINGS", textButtonStyle);
        buttonSettings.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //((Game) Gdx.app.getApplicationListener()).setScreen(new SettingsScreen());
                Gdx.app.log(TITLE, "Open settings screen");
            }
        });

        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                Gdx.app.log(TITLE, "Exit game");
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
        table.debug(); //opcja debugowania

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
        skin.dispose();
        atlas.dispose();
    }
}
