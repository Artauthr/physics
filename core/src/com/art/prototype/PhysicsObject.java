package com.art.prototype;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

public class PhysicsObject {
    protected Vector2 pos = new Vector2();

    @Getter
    @Setter
    protected Vector2 size = new Vector2();

}
