package com.art.prototype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Platform extends StaticBody {

    public Platform () {
    }

    public void draw (ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
    }

    public static Platform MAKE_GROUND_PLATFORM () {
        Platform platform = new Platform();
        platform.pos.y = 0;
        platform.size.x = 100;
        platform.size.y = 0.5f;
        return platform;
    }

    public static Platform MAKE_RANDOM () {
        Platform platform = new Platform();
        platform.pos.x = MathUtils.random(0, 100);
        platform.pos.y = 7;
        platform.size.x = MathUtils.random(10, 15);
        platform.size.y = 0.5f;
        return platform;
    }
}
