package com.art.prototype;

import com.art.prototype.api.API;
import com.art.prototype.editor.LevelData;
import com.art.prototype.editor.PlatformData;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.ui.Colors;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Getter;
import lombok.Setter;

public class World {
    /*
    Physics world that applies forces to all objects in it;
    checks collision between objects, feeds the homeless and cures cancer
     */
    private Vector2 forces;
    private Array<PhysicsObject> allObjects;
    private Array<DynamicBody> dynamicBodies;

    @Getter
    private Array<StaticBody> staticBodies;

    @Getter
    @Setter
    private Player player;

    //PARAMS
    private final float GRAVITY = -4.8f;

    //temp collider rectangles
    private Rectangle tmp1;
    private Rectangle tmp2;

    private final float TIME_STEP = 1 / 60f;

    @Setter
    private Rectangle lastDebugRect;


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
        float frameTime = Math.min(deltaTime, 0.15f);
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
                    dynamicBody.velocity.setZero();
                    dynamicBody.pos.y = staticBody.pos.y + staticBody.size.y;

                }
            }
        }
    }

    private void collisionCheckSingle () {
        applyForcePlayer(player);
        for (StaticBody staticBody : staticBodies) {
//            Utils.set(tmp2, player, player.getNextFramePos());
            Utils.setupRectangleFor(tmp2, player);
            Utils.setupRectangleFor(tmp1, staticBody);
            if (Intersector.overlaps(tmp1, tmp2)) {
                player.getVelocity().y = 0;
                player.getVelocity().x = 0;
                player.pos.y = staticBody.pos.y + staticBody.size.y;
//                player.setJumpAmount(76f); //hardcoded get from player class
            }
        }
    }

    public void loadFromLevelData (LevelData levelData, boolean resetPlayer) {
        this.staticBodies.clear();
        final Array<PlatformData> data = levelData.getPlatformDataArray();
        for (PlatformData datum : data) {
            final Platform platform = Platform.fromPlatformData(datum);
            this.staticBodies.add(platform);
        }
        if (resetPlayer) {
            player.reset();
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

    public void spawnPlatformAt (float x, float y) {
        Platform platform = new Platform();
        platform.setPos(x, y);
        platform.setSize(10, 6);
        staticBodies.add(platform);
    }

    public void spawnPlatformAt (Vector2 point) {
        spawnPlatformAt(point.x, point.y);
    }

    public void draw (ShapeRenderer shapeRenderer) {
        player.draw(shapeRenderer);
        shapeRenderer.setColor(Colors.TOXIC_GREEN);
        for (StaticBody staticBody : staticBodies) {
            shapeRenderer.rect(staticBody.pos.x, staticBody.pos.y, staticBody.getSize().x, staticBody.getSize().y);
        }
    }

    public void drawDebug (ShapeRenderer shapeRenderer) {
        if (lastDebugRect != null) {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.rect(lastDebugRect.x, lastDebugRect.y, lastDebugRect.width, lastDebugRect.height);
        }
    }


    public StaticBody getPlatformAt (float x, float y) {
        for (StaticBody staticBody : staticBodies) {
            Utils.setupRectangleFor(tmp1, staticBody);
            if (tmp1.contains(x, y)) {
                return staticBody;
            }
        }
        return null;
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

    public void removeStaticBody(StaticBody currentHoveredObject) {
        System.out.println(this.staticBodies.removeValue(currentHoveredObject, false));
    }
}
