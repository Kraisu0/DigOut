package com.kraisu.digout.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kraisu.digout.Main;

public class NewGameScreen implements Screen, InputProcessor {
    private final Main game;
    private Stage stage;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    // Wymiary siatki
    private float cellWidth = 150;
    private float cellHeight = 80;
    private final int gridWidth = 10;
    private final int gridHeight = 10;

    // Ustawienia prostokąta
    private final Vector2 framePosition = new Vector2(200, 150);
    private final float frameWidth = 1500; // Szerokość ramki
    private final float frameHeight = 800;  // Wysokość ramki

    private Vector2 gridPosition; // Pozycja siatki
    private float zoom = 1.0f; // Wartość zoomu
    private final float zoomSpeed = 0.1f; // Prędkość zoomowania
    private final float maxZoom = 3f; // Maksymalne powiększenie
    private final float minZoom = 1f; // Minimalne powiększenie
    private Vector2 lastTouch = new Vector2(); // Pozycja dla przesuwania siatki

    public NewGameScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();

        // Ustawienie początkowej pozycji siatki
        resetGridPosition();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        Gdx.input.setInputProcessor(this); // Ustawienie InputProcessor
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        drawBlackBackground(); // Rysowanie czarnego tła
        drawFrame();
        drawGrid();
    }

    private void drawBlackBackground() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
    }

    private void handleInput() {
        // Reset zoomu i pozycji siatki po naciśnięciu Spacji
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            resetZoomAndPosition();
        }
        // Przełączenie pełnoekranowe po naciśnięciu F
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        // Obsługa przesuwania siatki
        if (Gdx.input.isTouched()) {
            float deltaX = lastTouch.x - Gdx.input.getX();
            float deltaY = lastTouch.y - Gdx.input.getY();

            // Przesuwanie siatki
            gridPosition.x += deltaX;
            gridPosition.y += deltaY;

            // Ograniczenia przesuwania
            if (gridPosition.x < framePosition.x + (frameWidth - gridWidth * cellWidth * zoom)) {
                gridPosition.x = framePosition.x + (frameWidth - gridWidth * cellWidth * zoom);
            }
            if (gridPosition.x > framePosition.x) {
                gridPosition.x = framePosition.x;
            }
            if (gridPosition.y < framePosition.y + (frameHeight - gridHeight * cellHeight * zoom)) {
                gridPosition.y = framePosition.y + (frameHeight - gridHeight * cellHeight * zoom);
            }
            if (gridPosition.y > framePosition.y) {
                gridPosition.y = framePosition.y;
            }

            lastTouch.set(Gdx.input.getX(), Gdx.input.getY());
        } else {
            lastTouch.set(Gdx.input.getX(), Gdx.input.getY());
        }
    }


    private void drawFrame() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Rysowanie ramki
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(framePosition.x, framePosition.y, frameWidth, frameHeight);
        shapeRenderer.end();
    }

    private void drawGrid() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        // Rysowanie siatki
        for (int i = 0; i <= gridWidth; i++) {
            float x = gridPosition.x + i * cellWidth * zoom;
            shapeRenderer.line(x, gridPosition.y, x, gridPosition.y + gridHeight * cellHeight * zoom);
        }
        for (int j = 0; j <= gridHeight; j++) {
            float y = gridPosition.y + j * cellHeight * zoom;
            shapeRenderer.line(gridPosition.x, y, gridPosition.x + gridWidth * cellWidth * zoom, y);
        }

        shapeRenderer.end();
    }

    private void resetZoomAndPosition() {
        zoom = minZoom; // Reset zoomu
        resetGridPosition();
    }

    private void resetGridPosition() {
        gridPosition = new Vector2(framePosition.x + (frameWidth - (gridWidth * cellWidth * zoom)) / 2,
            framePosition.y + (frameHeight - (gridHeight * cellHeight * zoom)) / 2);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        lastTouch.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = screenX - lastTouch.x;
        float deltaY = screenY - lastTouch.y;

        // Przesuwanie siatki z ograniczeniami
        float newX = gridPosition.x + deltaX;
        float newY = gridPosition.y + deltaY;

        // Sprawdzenie ograniczeń przesuwania
        if (newX < framePosition.x + (frameWidth - gridWidth * cellWidth * zoom)) {
            newX = framePosition.x + (frameWidth - gridWidth * cellWidth * zoom);
        }
        if (newX > framePosition.x) {
            newX = framePosition.x;
        }
        if (newY < framePosition.y + (frameHeight - gridHeight * cellHeight * zoom)) {
            newY = framePosition.y + (frameHeight - gridHeight * cellHeight * zoom);
        }
        if (newY > framePosition.y) {
            newY = framePosition.y;
        }

        gridPosition.set(newX, newY);
        lastTouch.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        // Powiększanie i pomniejszanie siatki
        float zoomChange = v > 0 ? zoomSpeed : -zoomSpeed;
        if ((v > 0 && zoom < maxZoom) || (v < 0 && zoom > minZoom)) {
            float oldZoom = zoom;
            zoom += zoomChange;

            // Obliczanie środka siatki
            float centerX = gridPosition.x + (gridWidth * cellWidth * oldZoom) / 2;
            float centerY = gridPosition.y + (gridHeight * cellHeight * oldZoom) / 2;

            // Ustalamy nowe położenie siatki tak, aby się powiększała/pomniejszała od środka
            gridPosition.x = centerX - (gridWidth * cellWidth * zoom) / 2;
            gridPosition.y = centerY - (gridHeight * cellHeight * zoom) / 2;
        }
        return true;
    }



    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
