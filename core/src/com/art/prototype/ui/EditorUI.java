package com.art.prototype.ui;

import com.art.prototype.StaticBody;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.ui.buttons.*;
import com.art.prototype.ui.labels.LabelFactory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class EditorUI extends ALayout {
    private FlatTextButton removeEntityButton;
    private FlatTextButton transformButton;
    private FlatTextButton addEntityButton;
    private FlatTextButton goBackButton;
    private FlatIconButton showMoreButton;
    private Label modeLabel;
    private EditorGizmo gizmo;

    public EditorUI () {
        setFillParent(true);

        Table bottom = constructBottomSegment();
        Table top = constructTopSegment();

        gizmo = new EditorGizmo();

        this.top();
        this.add(top).growX();
        this.row();
        this.add().grow();
        this.row();
        this.add(bottom).expand().bottom().padBottom(100);

        addModeLabel();

//        this.getColor().a = 0.45f;

//        this.debugAll();
    }

    private Table constructTopSegment () {
        final Table table = new Table();
        table.pad(30);

        goBackButton = new FlatTextButton(FontSize.SIZE_40, "BACK");
        goBackButton.setOnClick(() -> API.get(GameUI.class).setMainLayout());
        goBackButton.setColor(Colors.CORAL);

        showMoreButton = new FlatIconButton("ui/ui-more-button", 15);


        table.left();
        table.add(showMoreButton).size(85);
        table.add().growX();
        table.add(goBackButton).width(225).height(120);
        return table;
    }

    private void addModeLabel () {
        Table wrapper = new Table();
        wrapper.setFillParent(true);


        this.modeLabel = LabelFactory.create(FontSize.SIZE_40);
        modeLabel.setAlignment(Align.center);

        Label infoLabel = LabelFactory.create(FontSize.SIZE_28, "(press ESC to quit)", Color.WHITE);
        final Table container = new Table();
        container.add(modeLabel);
        container.add(infoLabel).spaceLeft(20).padTop(5);

        wrapper.add(container).expand().top().padTop(35);
        this.addActor(wrapper);
        updateLabel();
    }

    private Table constructBottomSegment () {
        final Table table = new Table();
        table.pad(30);
        removeEntityButton = new FlatTextButton(FontSize.SIZE_40, "REMOVE");
        removeEntityButton.setColor(Colors.CORAL);
        removeEntityButton.setOnClick(() -> API.get(Editor.class).enterState(Editor.State.REMOVING));

        addEntityButton = new FlatTextButton(FontSize.SIZE_40, "ADD");
        addEntityButton.setColor(Colors.CORAL);
        addEntityButton.setOnClick(() -> API.get(Editor.class).enterState(Editor.State.ADDING));

        transformButton = new FlatTextButton(FontSize.SIZE_40, "RESIZE");
        transformButton.setColor(Colors.CORAL);
        transformButton.setOnClick(() -> API.get(Editor.class).enterState(Editor.State.RESIZING));

        table.defaults().size(265, 100).spaceLeft(175);

        table.add(addEntityButton).expandX();
        table.add(removeEntityButton).expandX();
        table.add(transformButton).expandX();

        return table;
    }

    public void updateLabel () {
        Editor.State editorState = API.get(Editor.class).getEditorState();
        String stateName = editorState.getStateName();
        Color color = editorState.getColor();

        this.modeLabel.setText(stateName);
        this.modeLabel.setColor(color);
    }

    public void addTransformGizmo(StaticBody object) {
        gizmo.bindToObject(object);
    }

    public void makeTransparent () {
        this.getColor().a = 0.45f;
    }

    public void revertTransparent() {
        this.getColor().a = 1;
    }
}
