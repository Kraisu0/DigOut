package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyUseScreen {


    private static boolean isFullscreen = true;

    public static void resizeFullScreen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            toggleFullscreen();
        }
    }

//    public static void resizeFullScreen() {
//        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
//            toggleFullscreen();
//        }
//    }

    private static void toggleFullscreen() {
        if (isFullscreen) {
            Gdx.graphics.setWindowedMode(1280, 720);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        isFullscreen = !isFullscreen;
    }

    public static boolean isFullscreen() {
        return isFullscreen;
    }
}
