package com.kraisu.digout;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;

import java.util.UUID;

import com.kraisu.digout.help.Constants;
import com.kraisu.digout.loaders.JsonLoader;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.survivor.Survivor;

import static com.kraisu.digout.genertor.Generators.generateNewSurvivors;
import static com.kraisu.digout.genertor.Generators.generateRandomSurvivor;
import static com.kraisu.digout.logs.DateLogs.logs;

public class SurvivorGenerator extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture avatar;
    private String name;
    private int age;
    private String profileInfo;
    private Constants.Survivors profession;
    private UUID staticUUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public BitmapFont font;
    private GlyphLayout layout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"));
        font.getData().setScale(0.6f); // Zmniejsz wielkość tekstu (0.5f to przykład, dostosuj według potrzeb)
        layout = new GlyphLayout();

        JsonLoader.mainLoader();

        generateNewSurvivorRend();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        if (avatar != null) {
            batch.draw(avatar, (Gdx.graphics.getWidth() - avatar.getWidth()) / 2, 250);

            float baseY = 190; // Początkowa pozycja y
            float spacing = 5; // Odstęp między liniami

            float yPosition = baseY; // Początkowa pozycja y dla tekstu

            yPosition = drawWrappedText("Name: " + name, Gdx.graphics.getWidth() / 2, yPosition);
            yPosition = drawWrappedText("Age: " + age, Gdx.graphics.getWidth() / 2, yPosition - spacing);
            yPosition = drawWrappedText("Info: " + profileInfo, Gdx.graphics.getWidth() / 2, yPosition - spacing);
            yPosition = drawWrappedText("Profession: " + profession, Gdx.graphics.getWidth() / 2, yPosition - spacing);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (avatar != null) avatar.dispose();
            generateNewSurvivorRend();
        }
    }

    private float drawWrappedText(String text, float x, float y) {
        // Ustal maksymalną szerokość do zawijania tekstu
        float wrapWidth = 500f; // Maksymalna szerokość w pikselach
        layout.setText(font, text, Color.WHITE, wrapWidth, -1, true);
        // Wyśrodkowanie tekstu
        font.draw(batch, layout, x - layout.width / 2, y);
        return y - layout.height; // Zwróć nową pozycję y
    }


    private void generateNewSurvivorRend() {
        Constants.Survivors survivorType = generateRandomSurvivor();
        if (survivorType != null) {
            Survivor survivor = generateNewSurvivors(staticUUID, survivorType);
            avatar = survivor.getImg();
            name = survivor.getName();
            age = survivor.getAge();
            profileInfo = survivor.getProfileInformation();
            profession = survivor.getProfession();
            System.out.println(survivor.toString());

            logs(DateLogs.LogType.INFO, staticUUID, "create new Survivor: " + name  + ", age: " + age + ", prof. info: " + profileInfo + " , profession: ", null);
        } else {
            System.out.println("Survivor not found");
        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (avatar != null) avatar.dispose();
    }
}
