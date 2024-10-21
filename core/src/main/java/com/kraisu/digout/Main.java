package com.kraisu.digout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.scenes.MainMenuScreenOLD;

public class Main extends Game {
    private Skin skin;

    @Override
    public void create() {
        skin = new Skin();
        skin.add("default-font", new BitmapFont(Gdx.files.internal("default.fnt")));
        skin.add("default", new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));

        setScreen(new MainMenuScreenOLD(this));
    }

    public Skin getSkin() {
        return skin;
    }
}


