package com.art.prototype.ui;

import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.api.API;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

public class EditorGizmo extends Table {
    private Array<Image> knobs;

    public EditorGizmo () {
        knobs = new Array<>();
        this.setFillParent(true);
        Drawable drawable = ResourceManager.getDrawable("ui/ui-circle");

        for (int i = 0; i < 4; i++) {
            Image knob = new Image(drawable, Scaling.fit);
            knobs.add(knob);
            this.add(knob).align(i);
            this.debugAll();
        }
    }


    public void bindToObject(StaticBody object) {
        GameUI gameUI = API.get(GameUI.class);
        Table rootUI = gameUI.getRootUI();
        Vector2 unProject = Utils.unProjectScl(object);

        Graphics2D graphics = API.get(Graphics2D.class);
        float v1 = graphics.getGameViewport().getWorldWidth() + graphics.getGameViewport().getWorldHeight();
        float v2 = graphics.getUiViewport().getWorldWidth() + graphics.getUiViewport().getWorldHeight();
        float viewportRatio = v2 / v1;

        rootUI.addActor(this);
        this.setPosition(unProject.x, unProject.y);
//        this.setSize(object.getSize().x * viewportRatio, object.getSize().y * viewportRatio);
    }
}

