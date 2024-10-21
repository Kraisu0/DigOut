package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UtilsScreen {

    //creating fonts
    public static class fonts {
        public static final BitmapFont whiteFont = new BitmapFont(Gdx.files.internal("fonts/pixelwhite.fnt"), false);
        public static final BitmapFont blackFont = new BitmapFont(Gdx.files.internal("fonts/pixelblack.fnt"), false);
        public static final BitmapFont goldFont = new BitmapFont(Gdx.files.internal("fonts/pixelgold.fnt"), false);
        public static final BitmapFont gold80Font = new BitmapFont(Gdx.files.internal("fonts/pf80gold.fnt"), false);
        public static final BitmapFont black80Font = new BitmapFont(Gdx.files.internal("fonts/pf80black.fnt"), false);
    }
}
