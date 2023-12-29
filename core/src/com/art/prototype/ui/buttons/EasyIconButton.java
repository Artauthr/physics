package com.art.prototype.ui.buttons;

import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import lombok.Getter;

public class EasyIconButton extends EasyOffsetButton {
    protected Drawable iconDrawable;
    @Getter
    protected Image icon;
    @Getter
    protected Cell<Image> iconCell;

    public EasyIconButton (Style style, String icon) {
        this.iconDrawable = ResourceManager.getDrawable(icon);
        build(style);
    }

    public EasyIconButton(Style style) {
        build(style);
    }

    @Override
    protected void buildInner (Table container) {
        icon = new Image(iconDrawable, Scaling.fit);
        iconCell = container.add(icon).grow().pad(20);
    }

    public void setIcon (Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
        this.icon.setDrawable(iconDrawable);
    }

    @Override
    public void visuallyEnable() {
        super.visuallyEnable();
        // set default color
        icon.setColor(Color.WHITE);
    }

    @Override
    public void visuallyDisable() {
        super.visuallyDisable();
        // set black and white color
        icon.setColor(Color.RED);
    }
}
