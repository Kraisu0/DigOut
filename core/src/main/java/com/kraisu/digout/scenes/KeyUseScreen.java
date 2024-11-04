package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.awt.*;

import static com.kraisu.digout.DigOutGame.titleBarHeight;

public class KeyUseScreen {


    private static boolean isFullscreen = true;

    public static void resizeFullScreen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            toggleFullscreen();
        }
    }

    private static void toggleFullscreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

        if (isFullscreen) {
                int screenWidth = Gdx.graphics.getWidth();
                int screenHeight = Gdx.graphics.getHeight() - screenInsets.bottom - 55;
                Gdx.graphics.setWindowedMode(screenWidth, screenHeight);
                Gdx.graphics.setUndecorated(false);

        } else {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        isFullscreen = !isFullscreen;
    }

    public static boolean isFullscreen() {
        return isFullscreen;
    }
}
