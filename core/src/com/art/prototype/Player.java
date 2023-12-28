package com.art.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;

public class Player extends DynamicBody {
    private final float width = 2;
    private final float height = 4;

    private final float JUMP_AMOUNT = 75f;
    private final float HORIZONTAL_SPEED = 50f;
    private Array<Integer> keycodes;


    public Player () {
        pos.set(50, 25);
        size.set(width, height);
        keycodes = new Array<>();
    }

    public void jump () {
        if (velocity.y == 0) {
            velocity.y = JUMP_AMOUNT;
        }
    }

    public void update (float deltaTime) {
        handleInput();
        pos.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -HORIZONTAL_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = HORIZONTAL_SPEED;
        } else {
            velocity.x = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump();
        }
    }

    public void queueInput (int keycode) {
        keycodes.add(keycode);
    }

    private void drainInput () {
        for (Integer keycode : keycodes) {
            if (keycode == Input.Keys.A) {
                velocity.x = -HORIZONTAL_SPEED;
            } else if (keycode == Input.Keys.D) {
                velocity.x = HORIZONTAL_SPEED;
            } else {
                velocity.x = 0;
            }
            if (keycode == Input.Keys.SPACE) {
                jump();
            }
        }
    }


    public void draw (ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
    }
}
