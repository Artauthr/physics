package com.art.prototype.ui.editor;

import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import lombok.Getter;
import lombok.Setter;

public class ResizeHandle extends Table {
    @Getter
    @Setter
    private int align;

    public ResizeHandle () {
        Drawable drawable = ResourceManager.getDrawable("ui/ui-circle");
        Image img = new Image(drawable, Scaling.fit);
        this.add(img).grow();
    }
}
