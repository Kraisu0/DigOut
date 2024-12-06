package com.kraisu.digout.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.DigOutGame;
import com.kraisu.digout.game.MyGame;
import com.kraisu.digout.help.Constants;
import com.kraisu.digout.help.ConstantsGenerator;
import com.kraisu.digout.logs.DateLogs;
import com.kraisu.digout.stuff.Equipment;
import com.kraisu.digout.stuff.EquipmentPrice;
import com.kraisu.digout.stuff.Resource;

import static com.kraisu.digout.logs.DateLogs.logs;

public class WinGameScreen implements Screen {

    private Stage stage;
    private Table table, staffTable, outerTable;
    private Label heading, conLabel, winLabel;
    private TextButton backButton;
    private MyGame myGame;



    public WinGameScreen(MyGame myGame) {
        this.myGame = myGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        outerTable = new Table();
        outerTable.setFillParent(true);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - 40f);
        table.setBackground(DigOutGame.skin.getDrawable("box"));

        // Heading & label
        heading = new Label("YOU WIN!", DigOutGame.skin.get("greenHugeFont", Label.LabelStyle.class));
        conLabel = new Label("CONGRATULATION", DigOutGame.skin.get("hugeFont", Label.LabelStyle.class));

        winLabel = new Label("You managed to save: " + myGame.getSurvivorManager().getAllSurvivors().size() + " survivors and you got them out on the " + myGame.getRound() + "th day. This is how many things you managed to get out of the mine:",
            DigOutGame.skin.get("mediumFont", Label.LabelStyle.class));
        winLabel.setColor(Color.YELLOW);
        winLabel.setWrap(true);
        winLabel.setAlignment(Align.center);
        winLabel.setWidth(1000);

        staffTable = new Table();
        staffTable = connectStaff(myGame);


        // button
        backButton = new TextButton("BACK TO MAIN MENU", DigOutGame.skinButton.get("default", TextButton.TextButtonStyle.class));

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new SplashToNextScreen(Constants.WhereSplashGo.EXIT, myGame));
                logs(DateLogs.LogType.INFO, null, "Back to main menu after Win", null);
            }
        });

        table.add(heading).center();
        table.row().pad(20);
        table.add(conLabel).center();
        table.row().pad(20);
        table.add(winLabel).center().expandX().fillX();
        table.row().pad(20);
        table.add(staffTable).center().expandX().fillX();
        table.row().pad(20);
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
        stage.clear();
    }

    private Table createResourceStaffBlock(Resource resources){
        Table table = new Table();

        Label name = new Label( resources.getName() + ": ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image image = new Image(new Texture(Gdx.files.internal(resources.getIconPath())));
        Label quantity = new Label(String.valueOf(resources.getTotalAmount()) , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        table.add(image).size(32, 32).padRight(2).center();
        table.add(name).padRight(5).padLeft(10).center();
        table.add(quantity).padRight(5).center();

        return table;
    }

    private Table createEquipmentStaffBlock(Equipment equipment){
        Table table = new Table();

        Label name = new Label( equipment.getName() + ": ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image image = new Image(new Texture(Gdx.files.internal(equipment.getIconPath())));
        Label quantity = new Label(String.valueOf(equipment.getTotalAmount()) , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        table.add(image).size(32, 32).padRight(2).center();
        table.add(name).padRight(5).padLeft(10).center();
        table.add(quantity).padRight(5).center();

        return table;
    }

    private Table createBuilding(MyGame myGame){
        Table table = new Table();

        Label name = new Label( "Number of buildings constructed: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image image = new Image(new Texture(Gdx.files.internal("img/BUILDING_BLANK.png")));
        Label quantity = new Label(String.valueOf(myGame.getRoomManager().countBuildingsWinGame()) , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        table.add(image).size(32, 32).padRight(2).center();
        table.add(name).padRight(5).padLeft(10).center();
        table.add(quantity).padRight(5).center();

        return table;
    }

    private Table createScoring(MyGame myGame){
        Table table = new Table();

        Label name = new Label( "Score: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image image = new Image(new Texture(Gdx.files.internal("img/SCORE.png")));
        Label quantity = new Label(String.valueOf(countScore(myGame)) , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        quantity.setColor(Color.GOLD);

        table.add(image).size(32, 32).padRight(2).center();
        table.add(name).padRight(5).padLeft(10).center();
        table.add(quantity).padRight(5).center();

        return table;
    }

    private Table createSurvivors(MyGame myGame){
        Table table = new Table();

        Label name = new Label( "Number of survivors saved: ", DigOutGame.skin.get("smallFont", Label.LabelStyle.class));
        Image image = new Image(new Texture(Gdx.files.internal("img/SURVIVOR_BLANK.png")));
        Label quantity = new Label(String.valueOf(myGame.getSurvivorManager().getSurvivors().size()) , DigOutGame.skin.get("smallFont", Label.LabelStyle.class));

        table.add(image).size(32, 32).padRight(2).center();
        table.add(name).padRight(5).padLeft(10).center();
        table.add(quantity).padRight(5).center();

        return table;
    }

    private Table connectStaff(MyGame myGame){
        Table table = new Table();
        table.add(createScoring(myGame)).pad(10).expandX().fillX().row();
        table.add(createResourceStaffBlock(myGame.getResourceManager().getResource(Constants.Resources.MATERIALS))).pad(8).left().expandX().fillX().row();
        table.add(createResourceStaffBlock(myGame.getResourceManager().getResource(Constants.Resources.FOOD))).pad(8).left().expandX().fillX().row();
        table.add(createResourceStaffBlock(myGame.getResourceManager().getResource(Constants.Resources.TOOLS))).pad(8).left().expandX().fillX().row();
        table.add(createResourceStaffBlock(myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY))).pad(8).left().expandX().fillX().row();
        table.add(createEquipmentStaffBlock(myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT))).pad(8).left().expandX().fillX().row();
        table.add(createEquipmentStaffBlock(myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT))).pad(8).left().expandX().fillX().row();
        table.add(createEquipmentStaffBlock(myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK))).pad(8).left().expandX().fillX().row();
        table.add(createEquipmentStaffBlock(myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE))).pad(8).left().expandX().fillX().row();
        table.add(createBuilding(myGame)).pad(8).left().expandX().fillX().row();
        table.add(createSurvivors(myGame)).pad(8).left().expandX().fillX().row();
        return table;
    }

    private int countScore(MyGame myGame){
        int score = 0;
        score += myGame.getRoomManager().countBuildingsWinGame()* ConstantsGenerator.ScoreMultiplier.SCORE_ROOM;
        score += myGame.getSurvivorManager().getSurvivors().size()* ConstantsGenerator.ScoreMultiplier.SCORE_SURVIVOR;
        score += myGame.getResourceManager().getResource(Constants.Resources.MATERIALS).getTotalAmount() * ConstantsGenerator.ScoreMultiplier.SCORE_MATERIALS;
        score += myGame.getResourceManager().getResource(Constants.Resources.FOOD).getTotalAmount() * ConstantsGenerator.ScoreMultiplier.SCORE_FOOD;
        score += myGame.getResourceManager().getResource(Constants.Resources.TOOLS).getTotalAmount() * ConstantsGenerator.ScoreMultiplier.SCORE_TOOLS;
        score += myGame.getResourceManager().getResource(Constants.Resources.ELECTRICITY).getTotalAmount() * ConstantsGenerator.ScoreMultiplier.SCORE_ELECTRICITY;
        score += (myGame.getEquipmentManager().getEquipment(Constants.Equipment.SEARCHLIGHT).getTotalAmount() + myGame.getEquipmentManager().getEquipment(Constants.Equipment.KITCHEN_ROBOT).getTotalAmount() +
            myGame.getEquipmentManager().getEquipment(Constants.Equipment.OXYGEN_MASK).getTotalAmount() + myGame.getEquipmentManager().getEquipment(Constants.Equipment.PICKAXE).getTotalAmount()) * ConstantsGenerator.ScoreMultiplier.SCORE_EQ;
        score += myGame.getRound() * ConstantsGenerator.ScoreMultiplier.SCORE_DAYS;

        myGame.getPlayer().setScore(score);

        return score;
    }
}
