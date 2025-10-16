package com.kraisu.digout.scenes;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kraisu.digout.game.NewGame;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.tween.SpriteAccessor;

import static com.kraisu.digout.logs.DateLogs.logs;

public class NewGameSplashScreen implements Screen {

    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;
    private String gameName;

    public NewGameSplashScreen(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Texture splashTexture = new Texture("img/splashScreenNewGame.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        splash.setColor(1, 1, 1, 0);

        // Create new game
       // NewGame newGame = new NewGame(gameName);
        //logs(DateLogs.LogType.INFO, newGame.getGame().getGameId(), "Create New MyGame", null);

        // Fade in animation
        Tween.to(splash, SpriteAccessor.ALPHA, 3)
            .target(1f)
            .start(tweenManager);
    }

    @Override
    public void render(float delta) {
        KeyUseScreen.resizeFullScreen();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        batch.begin();
        splash.draw(batch);
        batch.end();

        // Check for space key press
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            // Start fade-out animation and transition to GameScreen
            Tween.to(splash, SpriteAccessor.ALPHA, 3)
                .target(0f)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        NewGame newGame = new NewGame(gameName);
                        logs(DateLogs.LogType.INFO, newGame.getGame().getGameId(), "Create New MyGame", null);
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(newGame.getGame()));
                    }
                })
                .start(tweenManager);
        }else {

        }
    }

    @Override
    public void resize(int i, int i1) {

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

    }

}
