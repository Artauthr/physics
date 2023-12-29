package com.art.prototype.editor;

import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public class PlatformData {
    private Vector2 pos;
    private Vector2 size;
    private String id;
}
