package com.art.prototype.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainScreenLayout extends Table {
    private Table content;
    private Table top;
    private Table center;
    private Table bottom;
    private Button button;

    public MainScreenLayout () {
        content = new Table();
        content.setFillParent(true);

        top = constructTopSegment();
        center = constructCenterSegment();
        bottom = constructBottomSegment();
    }

    private Table constructTopSegment () {
        final Table segment = new Table();
        button = new Button();

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
