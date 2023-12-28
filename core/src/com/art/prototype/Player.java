package com.art.prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Setter;

public class Player extends DynamicBody {
    private final float width = 2;
    private final float height = 4;

    @Setter
    private float jumpAmount = 90f;
    private final float HORIZONTAL_SPEED = 35f;
    private final float MIN_JUMP_SPEED = 76f;
    private Array<Integer> keycodes;
    private Vector2 prevPos = new Vector2();

    private boolean spacePressed = false;
    private boolean spaceBeingPressed;
    private float spacePressDuration = 0f;


    public void backTick () {
        this.pos.y = prevPos.y;
    }

    public Player () {
        pos.set(50, 25);
        size.set(width, height);
        keycodes = new Array<>();
    }

    public void jump () {
        if (velocity.y == 0) {
            velocity.y = jumpAmount;
        }
    }

    public void update (float deltaTime) {
        handleInput(deltaTime);
        prevPos.set(pos);
        pos.add(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    private void handleInput(float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x = -HORIZONTAL_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x = HORIZONTAL_SPEED;
        } else {
            velocity.x = 0;
        }

        boolean beingHeld = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (beingHeld) {
            jump();
            spacePressDuration += deltaTime;
            jumpAmount += 50f;
            System.out.println("jumpAmount = " + jumpAmount);
            spacePressed = true;
        }
        if (spacePressed && !beingHeld) {
//            System.err.println("JUMP INTERRUPTED");
            stopJump();
            spacePressed = false;
            jumpAmount = MIN_JUMP_SPEED;
            spacePressDuration = 0;
        }
    }

    private void stopJump () {
        this.velocity.y *= 0.67f;
    }

    /*
    Gdx.input.isKeyJustPressed() => keydown
    Gdx.input.isKeyPressed() => held
    When Gdx.input.isKeyPressed() was true but is false again => keyUp
     */

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
