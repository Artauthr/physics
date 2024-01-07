package com.art.prototype;

import com.art.prototype.collision.CollisionChecker;
import com.art.prototype.collision.Ray2D;
import com.art.prototype.editor.LevelData;
import com.art.prototype.editor.PlatformData;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.ui.Colors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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

    @Getter
    @Setter
    private boolean gravEnabled = false;
    private Vector2 directionTemp = new Vector2();
    private Vector2 originTemp = new Vector2();
    private float accumulator = 0;


    public World () {
        forces = new Vector2(0, GRAVITY);
        allObjects = new Array<>();
        dynamicBodies = new Array<>();
        staticBodies = new Array<>();

        tmp1 = new Rectangle();
        tmp2 = new Rectangle();
    }



    public void doPhysicsStep(float deltaTime) {
        // fixed time step
//        float frameTime = Math.min(deltaTime, 0.15f);
        accumulator += deltaTime;
        while (accumulator >= TIME_STEP) {
            if (gravEnabled) {
                applyForceToAll();
            }
            collisionCheckSingle(deltaTime);
            accumulator -= TIME_STEP;
        }
    }

    private void applyForceToAll () {
        for (DynamicBody dynamicBody : dynamicBodies) {
            applyForce(dynamicBody);
        }
    }


    private void collisionCheckSingle (float deltaTime) {
        if (player.getVelocity().isZero()) return;
        for (StaticBody staticBody : staticBodies) {
            Utils.setupRectangleFor(tmp2, player);
            Utils.setupRectangleFor(tmp1, staticBody);// <- make a class that extends rectangle and pack this into it (have something like ColliderRect.setupFor(staticBody);

            originTemp.set(player.getPos());
            originTemp.x += player.getSize().x * 0.5f;
            originTemp.y += player.getSize().y * 0.5f;

            directionTemp.set(player.getVelocity());
            directionTemp.scl(deltaTime);

            CollisionChecker.RayVsRectResult result = CollisionChecker.rayIntersectsRect(originTemp, directionTemp, tmp1);
            if (result != null) {
                if (result.getNearHitTime() < 1) {
                    System.err.println("FUCKING COLLISION DETECTED");
                    player.getVelocity().setZero();
                }
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

    private void applyForce (DynamicBody object) {
        final Vector2 velocity = object.getVelocity();
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
