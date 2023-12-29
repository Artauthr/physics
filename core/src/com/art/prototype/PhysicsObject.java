package com.art.prototype;

import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class PhysicsObject {
    @Getter
    private String uuid;

    protected PhysicsObject () {
        this.uuid = UUID.randomUUID().toString();
    }

    @Getter
    @Setter
    protected Vector2 pos = new Vector2();

    @Getter
    @Setter
    protected Vector2 size = new Vector2();

    public void setPos (float x, float y) {
        pos.set(x,y);
    }

    public void setSize (float x, float y) {
        size.set(x,y);
    }

}
