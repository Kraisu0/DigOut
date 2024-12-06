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

import java.awt.*;
import java.io.File;

import static com.kraisu.digout.logs.DateLogs.createLogFile;
import static com.kraisu.digout.logs.DateLogs.logs;

public class DigOutGame extends Game {
    public static Skin skin;
    public static Skin skinButton;
    public static Skin uiskin;
    public static Skin skinSurvivorBox;
    public static Skin skinAvatars;
    public static Skin borderSkin;
    public static Skin skinRoom;

    public static final String TITLE = "DIG OUT", VERSION = "1.1.0";
    public static File LOGFILE = new File("../../../digout.log");
    public static Integer titleBarHeight = null;

    @Override
    public void create() {
        //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/skins.atlas"));
        skin = new Skin(Gdx.files.internal("skins.json"));
        skin.getFont("small-font").getData().markupEnabled = true;
        skin.getFont("medium-font").getData().markupEnabled = true;
        skin.getFont("big-font").getData().markupEnabled = true;
        skin.getFont("huge-font").getData().markupEnabled = true;
        skinButton = new Skin(Gdx.files.internal("buttons.json"));
        uiskin = new Skin(Gdx.files.internal("uiskin.json"));
        skinSurvivorBox = new Skin(Gdx.files.internal("SurvivorsBox.json"));
        skinAvatars = new Skin(Gdx.files.internal("avatars.json"));
        borderSkin = new Skin(Gdx.files.internal("borderRoom.json"));
        skinRoom = new Skin(Gdx.files.internal("Rooms.json"));
        logs(DateLogs.LogType.INFO, null, "create()", null);

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
        logs(DateLogs.LogType.INFO, null, "pause()", null);
        super.pause();
    }

    @Override
    public void resume() {
        logs(DateLogs.LogType.INFO, null,"resume()", null);
        super.resume();
    }

    @Override
    public void dispose() {
        logs(DateLogs.LogType.INFO, null,"dispose()", null);
        logs(DateLogs.LogType.INFO, null,"EIXT GAME\n", null);
        super.dispose();
    }

    public Skin getSkin()
    {return skin;}
}
