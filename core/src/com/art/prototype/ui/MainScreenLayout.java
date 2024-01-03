package com.art.prototype.ui;

import com.art.prototype.api.API;
import com.art.prototype.ui.buttons.FlatTextButton;
import com.art.prototype.ui.editor.EditorUI;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainScreenLayout extends ALayout {
    private Table top;
    private Table center;
    private Table bottom;
    private FlatTextButton openLeftSideButton;

    public MainScreenLayout () {
        this.setFillParent(true);
        this.setTouchable(Touchable.enabled);

        top = constructTopSegment();
        center = constructCenterSegment();
        bottom = constructBottomSegment();

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
        openLeftSideButton = new FlatTextButton(FontSize.SIZE_40, "MENU");

        openLeftSideButton.setOnClick(() -> API.get(GameUI.class).setLayout(EditorUI.class));
        openLeftSideButton.setColor(Colors.CORAL);
        segment.add(openLeftSideButton).expandX().right().padRight(25).width(225).padTop(25).height(120);

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
