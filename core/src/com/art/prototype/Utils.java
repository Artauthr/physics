package com.art.prototype;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Utils {
    private static Rectangle tmp;
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
}
