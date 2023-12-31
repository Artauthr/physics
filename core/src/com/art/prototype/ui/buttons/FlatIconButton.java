package com.art.prototype.ui.buttons;

import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

public class FlatIconButton extends FlatButton {
    private Image image;
    private float pad;

    public FlatIconButton (String iconPath, float pad) {
        Drawable drawable = ResourceManager.getDrawable(iconPath);
        image = new Image(drawable, Scaling.fit);
        this.pad = pad;
        setup();
    }

    @Override
    protected void buildContent(Table content) {
        content.add(image).grow().pad(pad);
    }
}
