package com.art.prototype;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

public class DynamicBody extends PhysicsObject {

    @Getter
    protected Vector2 velocity = new Vector2();
}
