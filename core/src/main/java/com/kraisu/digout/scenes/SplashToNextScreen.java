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
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.tween.SpriteAccessor;

import static com.kraisu.digout.logs.DateLogs.logs;


public class SplashToNextScreen implements Screen {

    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;
    private Constants.WhereSplashGo where;
    private MyGame myGame;

    public SplashToNextScreen(Constants.WhereSplashGo where, MyGame myGame) {
        this.where = where;
        this.myGame = myGame;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Texture splashTexture = new Texture("img/splashLoading2.png");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        splash.setColor(1, 1, 1, 0);
        //Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        whereGo();


    }

    @Override
    public void render(float v) {
        KeyUseScreen.resizeFullScreen();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(v);

        batch.begin();
        splash.draw(batch);
        batch.end();
    }

    private void whereGo(){
        switch (where) {
            case EXIT:
                Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1f).repeatYoyo(1,0.2f).setCallback(new TweenCallback(){
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
                    }
                }).start(tweenManager);

                break;
            case WIN:
                Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1f).repeatYoyo(1,0.2f).setCallback(new TweenCallback(){
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new WinGameScreen(myGame));
                        logs(DateLogs.LogType.INFO, null, "Win game!", null);
                    }
                }).start(tweenManager);
                break;
            case LOSE:
                Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1f).repeatYoyo(1,0.2f).setCallback(new TweenCallback(){
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new LoseGameScreen(myGame));
                        logs(DateLogs.LogType.INFO, null, "Lose Game!", null);
                    }
                }).start(tweenManager);
                break;
            default:
                System.out.println("cos poszło nie tak w splash screenie!");
                break;

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
