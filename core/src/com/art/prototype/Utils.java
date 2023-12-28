package com.art.prototype;

import com.badlogic.gdx.math.Rectangle;

public class Utils {
    public static void setupRectangleFor (Rectangle rect, PhysicsObject object) {
        rect.set(object.pos.x, object.pos.y, object.size.x, object.size.y);
    }

    public static void setupRectangleFor (Rectangle rect, Player player) {
        rect.set(player.pos.x, player.pos.y, player.size.x, player.size.y);
    }
}
