package com.art.prototype.collision;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import lombok.Getter;

public class Ray2D implements Pool.Poolable {

    @Getter
    private Vector2 origin;

    @Getter
    private Vector2 direction;

    public Ray2D () {
        this.origin = new Vector2();
        this.direction = new Vector2();
    }


    @Override
    public void reset() {
        this.origin.setZero();
        this.direction.setZero();
    }
}
