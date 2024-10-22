package com.kraisu.digout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.scenes.KeyUseScreen;
import com.kraisu.digout.scenes.MainMenuScreen;
import com.kraisu.digout.scenes.SplashScreen;
import com.kraisu.digout.logs.DateLogs;

import java.io.File;

import static com.kraisu.digout.logs.DateLogs.createLogFile;
import static com.kraisu.digout.logs.DateLogs.logs;

public class DigOutGame extends Game {
    public static Skin skin;

    public static final String TITLE = "DigOut", VERSION = "0.0.1";
    public static File LOGFILE = new File("./logs/digout.log");

    @Override
    public void create() {
        //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skins.atlas"));
        skin = new Skin(Gdx.files.internal("skins.json"));
        logs(DateLogs.LogType.INFO, "create()", null);

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
        logs(DateLogs.LogType.INFO, "pause()", null);
        super.pause();
    }

    @Override
    public void resume() {
        logs(DateLogs.LogType.INFO, "resume()", null);
        super.resume();
    }

    @Override
    public void dispose() {
        logs(DateLogs.LogType.INFO, "dispose()", null);
        logs(DateLogs.LogType.INFO, "EIXT GAME\n", null);
        super.dispose();
    }

    public Skin getSkin()
    {return skin;}
}
