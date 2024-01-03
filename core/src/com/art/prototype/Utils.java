package com.art.prototype;

import com.art.prototype.api.API;
import com.art.prototype.render.Graphics2D;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Utils {
    private static Rectangle tmp = new Rectangle();
    private static Vector2 tmpVec = new Vector2();
    public static ShapeRenderer shapeRenderer;

    public static void setupRectangleFor (Rectangle rect, PhysicsObject object) {
        rect.set(object.pos.x, object.pos.y, object.size.x, object.size.y);
    }

    public static void setupRectangleFor (Rectangle rect, Player player) {
        rect.set(player.pos.x, player.pos.y, player.size.x, player.size.y);
    }

    public static void setupPlayerDelayedRectangle (Rectangle rect, Player player, Vector2 nextFramePos) {
        rect.set(nextFramePos.x, nextFramePos.y, player.size.x, player.size.y);
    }

    public static Rectangle getPhysicsBodyRect (PhysicsObject object) {
        return tmp.set(object.pos.x, object.pos.y, object.size.x, object.size.y);
    }

    public static Vector2 unProjectScl(PhysicsObject object) {
        Vector2 cpy = object.getPos().cpy();
        API.get(Graphics2D.class).getGameViewport().project(cpy);
        cpy.scl(GlobalVariables.UNITS_PER_PIXEL_UI);
        return cpy;
    }

    public static Vector2 unProject(PhysicsObject object) {
        Vector2 cpy = object.getPos().cpy();
        API.get(Graphics2D.class).getGameViewport().project(cpy);
        return cpy;
    }

    public static Vector2 middleOfGameScreen () {
        ExtendViewport gameViewport = Graphics2D.get().getGameViewport();
        float x = gameViewport.getWorldWidth() * 0.5f;
        float y = gameViewport.getWorldHeight() * 0.5f;
        return tmpVec.set(x, y);
    }
}
