package com.kraisu.digout;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kraisu.digout.scenes.MainMenuScreen;

public class DigOutGame extends Game {
    private Skin skin;

    @Override
    public void create() {
        skin = new Skin();
        skin.add("default-font", new BitmapFont(Gdx.files.internal("fonts/default.fnt")));
        skin.add("df.white", new Label.LabelStyle(skin.getFont("default-font"), Color.WHITE));
        skin.add("df.black", new Label.LabelStyle(skin.getFont("default-font"), Color.BLACK));
        skin.add("df.red", new Label.LabelStyle(skin.getFont("default-font"), Color.RED));
        skin.add("df.green", new Label.LabelStyle(skin.getFont("default-font"), Color.GREEN));
        skin.add("pixel-font", new BitmapFont(Gdx.files.internal("fonts/myfont.fnt")));
        skin.add("pf.white", new Label.LabelStyle(skin.getFont("pixel-font"), Color.WHITE));
        skin.add("pf.black", new Label.LabelStyle(skin.getFont("pixel-font"), Color.BLACK));
        skin.add("pf.red", new Label.LabelStyle(skin.getFont("pixel-font"), Color.RED));
        skin.add("pf.green", new Label.LabelStyle(skin.getFont("pixel-font"), Color.GREEN));

        setScreen(new MainMenuScreen(this));

    }

    public Skin getSkin()
    {return skin;}
}
