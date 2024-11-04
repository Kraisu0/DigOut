package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;

public class NewGameNameScreen implements Screen {
    private Stage stage;
    private Table table, outerTable;
    private Label heading, labelName;
    private TextField nameField;
    private TextButton createButton;
    private String newName = "";

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        outerTable = new Table();
        outerTable.setFillParent(true);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/3f);
        table.setBackground(DigOutGame.skin.getDrawable("box.grey"));

        // Heading & label
        heading = new Label("NEW GAME NAME", DigOutGame.skin.get("hugeFont", Label.LabelStyle.class));

        labelName = new Label("Name:", DigOutGame.skin.get("bigFont", Label.LabelStyle.class));

        // FieldText
        nameField = new TextField("", DigOutGame.skin.get("black", TextField.TextFieldStyle.class));

        // button
        createButton = new TextButton("CREATE NEW GAME", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));
        createButton.setDisabled(true);

        // Listener Text field
        nameField.setTextFieldListener((textField, c) -> {
            newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                createButton.setDisabled(true);
            } else {
                createButton.setDisabled(false);
            }
        });

        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!createButton.isDisabled()) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new NewGameSplashScreen(newName));
                    System.out.println("New Game Name: " + newName);
                }
            }
        });

        table.add(heading).colspan(2).center();
        table.row().pad(20);
        table.add(labelName).right();
        table.add(nameField).width(400);
        table.row().pad(20);
        table.add(createButton).colspan(2);

        //table.debug();

        outerTable.add(table).center().width(table.getWidth()).height(table.getHeight());

        stage.addActor(outerTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
