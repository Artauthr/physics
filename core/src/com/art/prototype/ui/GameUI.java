package com.art.prototype.ui;

import com.art.prototype.api.API;
import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;

public class GameUI {
    @Getter
    private Stage stage;

    @Getter
    private Table rootUI;
    private MainScreenLayout mainScreenLayout;
    private Table layoutParent;
    private Cell<Table> layoutCell;

    public GameUI (Viewport viewport, Batch batch) {
        stage = new Stage(viewport, batch);

        rootUI = new Table();
        rootUI.setFillParent(true);
        rootUI.setTouchable(Touchable.enabled);

        layoutParent = new Table();
        layoutParent.setFillParent(true);
        layoutCell = layoutParent.add().grow();

//        Label.LabelStyle labelStyle = API.get(ResourceManager.class).getStyleMap().get(FontSize.SIZE_28);
//        Label label = new Label("PEE-PEE-POO-POO", labelStyle);

        mainScreenLayout = new MainScreenLayout();
        rootUI.addActor(layoutParent);
        setLayout(mainScreenLayout);

//        rootUI.add(label).expand();

        stage.addActor(rootUI);

        BitmapFont font = new BitmapFont();
    }

    public void setLayout (Table layoutTable) {
        this.layoutCell.setActor(layoutTable);
    }


    public void act () {
        stage.act();
    }

    public void draw () {
        stage.getViewport().apply();
        stage.draw();
    }
}
