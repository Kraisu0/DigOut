package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kraisu.digout.DigOutGame;


public class uiHelps {

    public enum Mark{
        INFO,
        ERROR,
        WARNING;
    }

    public static void displayInfoBox(Stage stage, String message, Mark mark) {
        Table table = new Table();
        float boxWidth = Gdx.graphics.getWidth() / 3f;
        float boxHeight = Gdx.graphics.getHeight() / 7f;
        table.setSize(boxWidth, boxHeight);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        Label description = new Label(message, DigOutGame.skin.get(getFontStyleForMark(mark), Label.LabelStyle.class));
        description.setWrap(true);
        description.setAlignment(Align.center);

        table.add(description).expand().fill().pad(10f);

        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.removeActor()));
            }
        });

        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth() / 2f, Gdx.graphics.getHeight() / 11f);

        table.addAction(Actions.sequence(Actions.delay(5), Actions.fadeOut(0.5f), Actions.removeActor()));

    }


    private static String getFontStyleForMark(Mark mark) {
        switch (mark) {
            case INFO:
                return "infoFont";
            case ERROR:
                return "errorFont";
            case WARNING:
                return "warnFont";
            default:
                return "infoFont";
        }
    }

    public interface ConfirmCallback {
        void onResult(boolean confirmed);
    }

    public static void displayConfirmBox(Stage stage, String message, ConfirmCallback callback) {
        Table table = new Table();
        float boxWidth = Gdx.graphics.getWidth() / 6f;
        float boxHeight = Gdx.graphics.getHeight() / 8f;
        table.setSize(boxWidth, boxHeight);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        Label description = new Label(message, DigOutGame.skin.get("medium-font", Label.LabelStyle.class));
        description.setWrap(true);
        description.setAlignment(Align.center);

        table.add(description).expand().fill().pad(10f).row();

        Button okButton = new TextButton("Confirm", DigOutGame.skinButton.get("small-green", TextButton.TextButtonStyle.class));
        Button cancelButton = new TextButton("Cancel", DigOutGame.skinButton.get("small-red", TextButton.TextButtonStyle.class));

        table.add(okButton).pad(5);
        table.add(cancelButton).pad(5);

        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onResult(true);
                table.remove();
            }
        });

        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.onResult(false);
                table.remove();
            }
        });

        stage.addActor(table);
        table.setPosition(Gdx.graphics.getWidth() / 2f - table.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - table.getHeight() / 2f);
    }




}
