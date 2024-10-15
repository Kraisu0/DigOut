package com.kraisu.digout.scenes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Grid {
    private Vector2 position;
    private float width, height;
    private float cellWidth, cellHeight;
    private int gridWidth, gridHeight;
    private float currentZoom = 1f; // Zmienna przechowująca aktualny zoom
    private float maxZoom = 5f; // Maksymalne powiększenie

    public Grid(Vector2 position, float width, float height, float cellWidth, float cellHeight, int gridWidth, int gridHeight) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i <= gridWidth; i++) {
            float x = position.x + i * cellWidth * currentZoom;
            shapeRenderer.line(x, position.y, x, position.y + height * currentZoom);
        }
        for (int j = 0; j <= gridHeight; j++) {
            float y = position.y + j * cellHeight * currentZoom;
            shapeRenderer.line(position.x, y, position.x + width * currentZoom, y);
        }
        shapeRenderer.end();
    }

    public void zoom(float amount) {
        float zoomFactor = 1.05f; // Współczynnik powiększenia

        // Zmiana zoomu w zależności od kierunku scrolla
        if (amount > 0) {
            currentZoom *= zoomFactor; // Powiększenie
        } else {
            currentZoom /= zoomFactor; // Pomniejszenie
        }

        // Ograniczenia zoomu
        currentZoom = Math.max(0.1f, Math.min(currentZoom, maxZoom));
    }

    public void resetZoomAndPosition() {
        currentZoom = 1f; // Resetuj zoom
        position.set(200, 150); // Resetuj pozycję, jeśli potrzebne
    }

    public void translate(float deltaX, float deltaY) {
        position.add(deltaX, deltaY);
    }
}
