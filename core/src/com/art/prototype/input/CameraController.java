package com.art.prototype.input;

import com.art.prototype.GlobalVariables;
import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.ui.ALayout;
import com.art.prototype.ui.GameUI;
import com.art.prototype.ui.editor.EditorUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import lombok.Getter;

public class CameraController {
    private float accumulator = 0;
    private final float HOVER_UPDATE_RATE = 0.0f;
    private float hoverTimer = 0;

    @Getter
    private Mode cameraMode;

    private Vector3 tmpTarget;
    private final float CLAMP = 0.8f;

    public CameraController () {
        tmpTarget = new Vector3();
        cameraMode = Mode.MANUAL_CONTROL;
    }

    public void update (float deltaTime) {
        switch (cameraMode) {
            case MANUAL_CONTROL:
                if (accumulator != 0) {
                    accumulator = MathUtils.clamp(accumulator, -CLAMP, CLAMP);
                    Camera gameCamera = Graphics2D.get().getGameCamera();
                    tmpTarget.y = gameCamera.position.y;
                    tmpTarget.x = gameCamera.position.x += accumulator;
                    gameCamera.position.interpolate(tmpTarget, 0.2f, Interpolation.elasticIn);
                    int sign = accumulator > 0 ? -1 : 1;
                    accumulator += sign * deltaTime;
                    if (accumulator <= 0.2f && accumulator >= -0.2f) {
                        accumulator = 0;
                    }
                    GameUI gameUI = API.get(GameUI.class);
                    EditorUI editorUI = gameUI.getLayout(EditorUI.class);
                    if (editorUI.getTransformer().isActive()) {
                        editorUI.getTransformer().reEvaluateSizePos();
                    }
                }
                break;
            case PLAYER_FOLLOW:
                Camera gameCamera = Graphics2D.get().getGameCamera();
                tmpTarget.y = gameCamera.position.y;
                tmpTarget.x = API.get(World.class).getPlayer().getPos().x;
                gameCamera.position.interpolate(tmpTarget, 0.1f, Interpolation.elasticIn);
                break;
        }

        hoverTimer += deltaTime;
        if (hoverTimer >= HOVER_UPDATE_RATE) {
            this.updateCurrentHovered(Gdx.input.getX(), Gdx.input.getY());
            hoverTimer = 0;
        }

    }

    private void updateCurrentHovered (float screenX, float screenY) {
        Vector2 pooledVec2 = Pools.obtain(Vector2.class);
        pooledVec2.set(screenX, screenY);
        Graphics2D.get().getGameViewport().unproject(pooledVec2);
        World world = API.get(World.class);
        Array<StaticBody> staticBodies = world.getStaticBodies();
        Editor editor = API.get(Editor.class);

        final float CLICKBOX_PAD = 1.2f;
        final float CLICKBOX_PAD_Y = 1.3f;

        for (StaticBody staticBody : staticBodies) {
            Rectangle physicsBodyRect = Utils.getPhysicsBodyRect(staticBody);
            float originalWidth = physicsBodyRect.width;
            float originalHeight = physicsBodyRect.height;
            float wRatio = originalWidth * CLICKBOX_PAD;
            float hRatio = originalHeight * CLICKBOX_PAD_Y;

            // Increase the size of the rectangle
            physicsBodyRect.setSize(wRatio, hRatio);

            // Adjust the position to keep the staticBody centered within the enlarged rectangle
            physicsBodyRect.x -= (wRatio - originalWidth) * 0.5f;
            physicsBodyRect.y -= (hRatio - originalHeight) * 0.5f;

            if (GlobalVariables.HOVER_DEBUG) {
                world.setLastDebugRect(physicsBodyRect);
            }
            if (physicsBodyRect.contains(pooledVec2)) {
                editor.setCurrentHoveredObject(staticBody);
                return;
            }
        }
        editor.setCurrentHoveredObject(null);
    }

    public void queueMovement (float amount) {
        this.accumulator += amount;
    }

    enum Mode {
        PLAYER_FOLLOW,
        MANUAL_CONTROL,
        ;
    }
}
