package com.art.prototype.editor;

import com.art.prototype.PhysicsObject;
import com.art.prototype.StaticBody;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;

import java.rmi.server.UID;
import java.util.UUID;

public class Editor {

    @Getter
    private State editorState;

    private Array<Actor> fakeActors;
    private Array<Actor> removeActors;

    private Image highlighter;
    private Table

    public Editor () {
        editorState = State.DISABLED;
        fakeActors = new Array<>();
        removeActors = new Array<>();
    }

    private void setupHighlighter () {
        Drawable drawable = ResourceManager.getDrawable("ui/ui-white-pixel");
        highlighter = new Image(drawable);
        highlighter.getColor().a = 0.5f;

    }

    public void enterAddMode () {
        this.editorState = State.ADDING;
        API.get(World.class);
    }

    public void enterRemoveMode () {
        this.editorState = State.REMOVING;
        World world = API.get(World.class);
        Array<StaticBody> staticBodies = world.getStaticBodies();
    }

    public void quitMode () {
        this.editorState = State.DISABLED;
    }

    public void highlightPhysicsObject (PhysicsObject object, Color color) {
        GameUI gameUI = API.get(GameUI.class);
        Table rootUI = gameUI.getRootUI();
        highlighter.setColor(color);
        highlighter.getColor().a = 0.43f;

        rootUI.addActor(highlighter);

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
        DISABLED,
        ADDING,
        REMOVING,
        ;
    }

}
