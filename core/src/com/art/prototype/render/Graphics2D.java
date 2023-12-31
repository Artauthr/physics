package com.art.prototype.render;

import com.art.prototype.GlobalVariables;
import com.art.prototype.api.API;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;

public class Graphics2D {

    @Getter
    private SpriteBatch batch;
    @Getter
    private ShapeRenderer shapeRenderer;
    @Getter
    private ExtendViewport gameViewport;
    @Getter
    private ScreenViewport uiViewport;
    @Getter
    private Camera gameCamera;
    @Getter
    private Camera uiCamera;

    public Graphics2D() {
        initGraphics();
    }

    private void initGraphics () {
        this.batch = new SpriteBatch();

        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        //game viewport
        gameViewport = new ExtendViewport(100, 50);
        this.gameCamera = gameViewport.getCamera();
        gameCamera.position.set(gameCamera.viewportWidth / 2, gameCamera.viewportHeight / 2, 0); // Center the camera

        //ui viewport
        uiViewport = new ScreenViewport();
        uiViewport.setUnitsPerPixel(GlobalVariables.UNITS_PER_PIXEL_UI);
        this.uiCamera = uiViewport.getCamera();
    }

//    public Vector2 unprojectMouseToUI (float x, float y) {
//
//    }

    public static Graphics2D get() {
        return API.get(Graphics2D.class);
    }
}
