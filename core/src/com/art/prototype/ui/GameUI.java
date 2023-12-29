package com.art.prototype.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import sun.jvm.hotspot.utilities.BitMap;

public class GameUI {
    @Getter
    private Stage stage;
    private Table rootUI;

    public GameUI (Viewport viewport, Batch batch) {
        stage = new Stage(viewport, batch);

        rootUI = new Table();
        rootUI.setFillParent(true);

        stage.addActor(rootUI);

        BitmapFont font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.GREEN);
        Label label = new Label("Test", labelStyle);
        label.setFontScale(1f);
        label.setAlignment(Align.center);

        rootUI.add(label).grow();
        rootUI.row();
        rootUI.debugAll();
    }


    public void act () {
        stage.act();
    }

    public void draw () {
        stage.getViewport().apply();
        stage.draw();
    }
}
