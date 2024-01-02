package com.art.prototype.editor;

import com.art.prototype.PhysicsObject;
import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.ALayout;
import com.art.prototype.ui.Colors;
import com.art.prototype.ui.EditorUI;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Editor {

    @Getter
    private State editorState;

    private Array<Actor> fakeActors;
    private Array<Actor> removeActors;

    private Image highlighter;
    private Table highlightWrap;

    private StaticBody currentTransformObject;

    @Getter
    @Setter
    private StaticBody currentHoveredObject;

    @Getter
    @Setter
    private boolean followMouseRect = false;

    public Editor () {
        editorState = State.DISABLED;
        fakeActors = new Array<>();
        removeActors = new Array<>();
    }

    public void setupHighlighter () {
        Drawable drawable = ResourceManager.getDrawable("ui/ui-white-pixel");
        highlighter = new Image(drawable);
        highlighter.getColor().a = 0.5f;

        highlightWrap = new Table();
        highlightWrap.setFillParent(true);

        highlightWrap.add(highlighter);
    }

    public void enterState (State state) {
        this.editorState = state;
        onModeChanged();
    }

    public void onModeChanged () {
        EditorUI editorUI = API.get(GameUI.class).getLayout(EditorUI.class);
        editorUI.updateLabel();

        if (!(this.editorState == State.DISABLED)) {
            editorUI.makeTransparent();
        } else {
            editorUI.revertTransparent();
        }
    }

    public void quitMode () {
        this.editorState = State.DISABLED;
        onModeChanged();
    }

    public void highlightPhysicsObject (PhysicsObject object, Color color) {
        highlighter.setVisible(true);
        GameUI gameUI = API.get(GameUI.class);
        Table rootUI = gameUI.getRootUI();
        highlighter.setColor(color);
        highlighter.getColor().a = 0.12f;

        rootUI.addActor(highlighter);

        Vector2 unProject = Utils.unProjectScl(object);

        highlighter.setPosition(unProject.x, unProject.y);
        Graphics2D graphics = API.get(Graphics2D.class);
        float v1 = graphics.getGameViewport().getWorldWidth() + graphics.getGameViewport().getWorldHeight();
        float v2 = graphics.getUiViewport().getWorldWidth() + graphics.getUiViewport().getWorldHeight();
        float viewportRatio = v2 / v1;
        highlighter.setSize(object.getSize().x * viewportRatio, object.getSize().y * viewportRatio);

    }

    public void hideHighlighter () {
        highlighter.setVisible(false);
    }


    public void addGizmoToObject (StaticBody body) {
        EditorUI editorUI = API.get(GameUI.class).getLayout(EditorUI.class);
        editorUI.addTransformGizmo(body);
        this.currentTransformObject = body;
    }

    public void saveToFile () {
        LevelData levelData = new LevelData();
        levelData.setWorldRef(API.get(World.class));
        saveLevelData(levelData);
        System.out.println("=======================LEVEL SAVED=======================");
    }

    public void saveLevelData(LevelData levelData) {
        Json json = new Json();
        String jsonData = json.toJson(levelData); // Serialize your object
        String name = UUID.randomUUID().toString();
        FileHandle file = Gdx.files.local("savedLevels/" + name + ".json"); // Create a FileHandle
        file.writeString(jsonData, false); // Write the string to a file
    }

    public LevelData loadLevelData (String path) {
        FileHandle file = Gdx.files.local(path);
        if (!file.exists()) {
            // Handle the case where the file does not exist
            Gdx.app.error("LoadLevelData", "File not found");
            return null;
        }

        String jsonData = file.readString();

        Json json = new Json();
        return json.fromJson(LevelData.class, jsonData);
    }


    public enum State {
        DISABLED("Editor Mode", Color.WHITE),
        ADDING("Adding mode", Color.GREEN),
        REMOVING("Removing mode", Colors.DARKISH_RED),
        RESIZING("Resizing mode", Colors.SKY);
        ;

        @Getter
        private String stateName;
        @Getter
        private Color color;

        State (String stateName, Color color) {
            this.stateName = stateName;
            this.color = color;
        }
    }

}
