package com.art.prototype.input;

import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.render.Graphics2D;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;

public class CameraController {
    private float accumulator = 0;

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
                }
                break;
            case PLAYER_FOLLOW:
                Camera gameCamera = Graphics2D.get().getGameCamera();
                tmpTarget.y = gameCamera.position.y;
                tmpTarget.x = API.get(World.class).getPlayer().getPos().x;
                gameCamera.position.interpolate(tmpTarget, 0.1f, Interpolation.elasticIn);
        }

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
