package com.art.prototype;

import com.art.prototype.api.API;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Setter;

public class Player extends DynamicBody {
    private final float width = 2;
    private final float height = 4;

    @Setter
    private float jumpAmount = 90f;
    private final float HORIZONTAL_SPEED = 35f;
    private Vector2 prevPos = new Vector2();
    private boolean spacePressed = false;
    private float spacePressDuration = 0f;


    public Player () {
        pos.set(50, 25);
        size.set(width, height);
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

        if (!API.get(World.class).isGravEnabled()) {
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocity.y = -HORIZONTAL_SPEED;
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocity.y = HORIZONTAL_SPEED;
            } else {
                velocity.y = 0;
            }
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }

        boolean beingHeld = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (beingHeld) {
            spacePressDuration += deltaTime;
            jump();
//            jumpAmount += 1f;
            velocity.y += 1f;
            spacePressed = true;
        }
        if (spacePressed && !beingHeld) {
//            System.err.println("JUMP INTERRUPTED");
            stopJump();
            spacePressed = false;
//            jumpAmount = MIN_JUMP_SPEED;
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


    private Vector2 tmp = new Vector2();

    public Vector2 getNextFramePos () {
        final float deltaTime = Gdx.graphics.getDeltaTime();
        tmp.set(pos.x + velocity.x * deltaTime, pos.y + velocity.y * deltaTime);
        return tmp;
    }

    public void draw (ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
    }

    public void reset () {
        velocity.setZero();
        Vector2 middle = Utils.middleOfGameScreen();
        pos.set(middle);
    }
}
