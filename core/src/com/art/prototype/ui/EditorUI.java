package com.art.prototype.ui;

import com.art.prototype.ui.buttons.EasyIconButton;
import com.art.prototype.ui.buttons.EasyOffsetButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class EditorUI extends Table {
    private EasyIconButton removeEntityButton;
    private EasyIconButton addEntityButton;
    private EasyIconButton saveButton;
    private EasyIconButton showLevelsButton;

    public EditorUI () {
        setFillParent(true);

        Table bottom = constructBottomSegment();
        Table top = constructTopSegment();

        this.top();

        this.add(top).growX();
        this.row();
        this.add().grow();
        this.row();
        this.add(bottom).expand().bottom();

//        this.debugAll();
    }

    private Table constructTopSegment () {
        final Table table = new Table();
        table.pad(30);

        saveButton = new EasyIconButton(EasyOffsetButton.Style.BLUE);
        showLevelsButton = new EasyIconButton(EasyOffsetButton.Style.GREEN);

        final Table wrapper = new Table();
        wrapper.add(saveButton);
        wrapper.add().grow();

        table.left();
        table.add(showLevelsButton).expandX().left();
        table.add(wrapper).growX();


        return table;
    }

    private Table constructBottomSegment () {
        final Table table = new Table();
        table.pad(30);
        removeEntityButton = new EasyIconButton(EasyOffsetButton.Style.RED);
        addEntityButton = new EasyIconButton(EasyOffsetButton.Style.GREEN);

        table.add(addEntityButton).expandX().right().size(265, 175);
        table.add(removeEntityButton).expandX().left().spaceLeft(150).size(265, 175);

        return table;
    }

}
