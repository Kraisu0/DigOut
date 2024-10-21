package com.kraisu.digout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.scenes.KeyUseScreen;
import com.kraisu.digout.scenes.MainMenuScreen;
import com.kraisu.digout.scenes.SplashScreen;

public class DigOutGame extends Game {
    private Skin skin;


    public static final String TITLE = "DigOut", VERSION = "0.0.1";

    @Override
    public void create() {
        Gdx.app.log(TITLE, "create()");

//        skin = new Skin();
//        skin.add("default-font", new BitmapFont(Gdx.files.internal("fonts/default.fnt")));
//        skin.add("df.white", new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));
//        skin.add("df.black", new Label.LabelStyle(skin.getFont("default-font"), Color.BLACK));
//        skin.add("df.red", new Label.LabelStyle(skin.getFont("default-font"), Color.RED));
//        skin.add("df.green", new Label.LabelStyle(skin.getFont("default-font"), Color.GREEN));
//        skin.add("pixel-font", new BitmapFont(Gdx.files.internal("fonts/myfont.fnt")));
//        skin.add("pf.white", new Label.LabelStyle(skin.getFont("pixel-font"), Color.WHITE));
//        skin.add("pf.black", new Label.LabelStyle(skin.getFont("pixel-font"), Color.BLACK));
//        skin.add("pf.red", new Label.LabelStyle(skin.getFont("pixel-font"), Color.RED));
//        skin.add("pf.green", new Label.LabelStyle(skin.getFont("pixel-font"), Color.GREEN));

        setScreen(new SplashScreen());

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        KeyUseScreen.resizeFullScreen();
        super.render();
    }

    @Override
    public void pause() {
        Gdx.app.log(TITLE, "pause()");
        super.pause();
    }

    @Override
    public void resume() {
        Gdx.app.log(TITLE, "resume()");
        super.resume();
    }

    @Override
    public void dispose() {
        Gdx.app.log(TITLE, "dispose()");
        super.dispose();
    }

    public Skin getSkin()
    {return skin;}
}
