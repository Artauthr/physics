package com.art.prototype;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import lombok.Setter;

public class World {
    /*
    Physics world that applies forces to all objects in it;
    checks collision between objects, feeds the homeless and cures cancer
     */
    private Vector2 forces;
    private Array<PhysicsObject> allObjects;
    private Array<DynamicBody> dynamicBodies;
    private Array<StaticBody> staticBodies;

    @Setter
    private Player player;

    //PARAMS
    private final float GRAVITY = -4.8f;

    //temp collider rectangles
    private Rectangle tmp1;
    private Rectangle tmp2;

    private final float TIME_STEP = 1 / 60f;


    public World () {
        forces = new Vector2(0, GRAVITY);
        allObjects = new Array<>();
        dynamicBodies = new Array<>();
        staticBodies = new Array<>();

        tmp1 = new Rectangle();
        tmp2 = new Rectangle();
    }

    private float accumulator = 0;

    public void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            collisionCheckSingle();
            accumulator -= TIME_STEP;
        }
    }

    private void applyForceToAll () {
        for (DynamicBody dynamicBody : dynamicBodies) {
            applyForce(dynamicBody);
        }
    }

    private void collisionCheck () {
        for (DynamicBody dynamicBody : dynamicBodies) {
            applyForce(dynamicBody);
            for (StaticBody staticBody : staticBodies) {
                Utils.setupRectangleFor(tmp1, dynamicBody);
                Utils.setupRectangleFor(tmp2, staticBody);
                if (Intersector.overlaps(tmp1, tmp2)) {
                    dynamicBody.getVelocity().y = 0;
                    dynamicBody.pos.y = staticBody.pos.y + staticBody.size.y;

                }
            }
        }
    }

    private void collisionCheckSingle () {
        applyForcePlayer(player);
        for (StaticBody staticBody : staticBodies) {
            Utils.setupRectangleFor(tmp2, player);
            Utils.setupRectangleFor(tmp1, staticBody);
            if (Intersector.overlaps(tmp1, tmp2)) {
                player.getVelocity().y = 0;
                player.pos.y = staticBody.pos.y + staticBody.size.y;
                player.setJumpAmount(76f); //hardcoded get from player class
            }
        }
    }
    private void resolveStaticCollision (DynamicBody dynamicBody, StaticBody staticBody) {

        boolean yLower = dynamicBody.pos.y < staticBody.pos.y;
        boolean xLower = dynamicBody.pos.x < staticBody.pos.x;

        final float xDist = Math.abs(dynamicBody.pos.x - staticBody.pos.x);
        final float yDist = Math.abs(dynamicBody.pos.y - staticBody.pos.y);

        final float fartherDistance = Math.max(xDist, yDist);

        if (fartherDistance == xDist) {
            if (xLower) {
                System.out.println("LEFT SIDE HIT");
                return;
            } else {
                System.out.println("RIGHT SIDE HIT");
                return;
            }
        }
        if (fartherDistance == yDist) {
            if (yLower) {
                System.out.println("BOTTOM SIDE HIT");
            } else {
                System.out.println("UPPER SIDE HIT");
            }
        }

    }



    private void applyForce (DynamicBody object) {
        final Vector2 velocity = object.getVelocity();
        velocity.add(forces);
    }

    private void applyForcePlayer (Player player) {
        final Vector2 velocity = player.getVelocity();
        velocity.add(forces);
    }

    public  <T extends DynamicBody> void addDynamicBody (T body) {
        this.dynamicBodies.add(body);
    }

    public <T extends StaticBody> void addStaticBody (T body) {
        this.staticBodies.add(body);
    }

}
