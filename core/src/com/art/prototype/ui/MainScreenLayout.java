package com.art.prototype.ui;

import com.art.prototype.api.API;
import com.art.prototype.ui.buttons.EasyIconButton;
import com.art.prototype.ui.buttons.EasyOffsetButton;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainScreenLayout extends Table {
    private Table content;
    private Table top;
    private Table center;
    private Table bottom;
    private Button button;
    private EasyIconButton openLeftSideButton;
    private EditorUI editorUI;

    public MainScreenLayout () {
        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);

        top = constructTopSegment();
        center = constructCenterSegment();
        bottom = constructBottomSegment();

        editorUI = new EditorUI();

        this.top();
        this.defaults().growX();

        this.add(top);
        this.row();
        this.add(center);
        this.row();
        this.add(bottom);
    }

    private Table constructTopSegment () {
        final Table segment = new Table();
        openLeftSideButton = new EasyIconButton(EasyOffsetButton.Style.BLUE, "ui/ui-white-pixel");
        openLeftSideButton.setIcon(null);

        openLeftSideButton.setOnClick(() -> API.get(GameUI.class).setLayout(editorUI));
        segment.add(openLeftSideButton).expandX().right().padRight(25).width(150).padTop(25).height(150);

        return segment;
    }

    private Table constructCenterSegment () {
        final Table segment = new Table();
        return segment;
    }

    private Table constructBottomSegment () {
        final Table segment = new Table();
        return segment;
    }
}
