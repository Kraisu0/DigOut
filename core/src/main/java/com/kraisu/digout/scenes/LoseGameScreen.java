package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.logs.DateLogs;

import static com.kraisu.digout.logs.DateLogs.logs;

public class LoseGameScreen implements Screen {

    private Stage stage;
    private Table table, outerTable;
    private Label heading, loseLabel;
    private TextButton backButton;
    private MyGame myGame;



    public LoseGameScreen(MyGame myGame) {
        this.myGame = myGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        outerTable = new Table();
        outerTable.setFillParent(true);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        table.setBackground(DigOutGame.skin.getDrawable("box.gray"));

        // Heading & label
        heading = new Label("YOU LOSE!", DigOutGame.skin.get("redHugeFont", Label.LabelStyle.class));

        loseLabel = new Label("Unfortunately, you lost, you didn't manage to get any survivors out of the collapsed mine. Hopefully it will work out next time.",
            DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        loseLabel.setWrap(true);
        loseLabel.setAlignment(Align.center);
        loseLabel.setWidth(1000);

        // button
        backButton = new TextButton("BACK TO MAIN MENU", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SplashToNextScreen(Constants.WhereSplashGo.EXIT, myGame));
                logs(DateLogs.LogType.INFO, null, "Back to main menu after Lose", null);
            }
        });

        table.add(heading).center();
        table.row().pad(20);
        table.add(loseLabel).center().expandX().fillX();
        table.row().padTop(80);
        table.add(backButton);

        //table.debug();

        outerTable.add(table).center().width(table.getWidth()).height(table.getHeight());

        stage.addActor(outerTable);
    }

    @Override
    public void render(float v) {
        KeyUseScreen.resizeFullScreen();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);;

        stage.act(v);
        stage.draw();
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
