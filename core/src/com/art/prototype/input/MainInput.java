package com.art.prototype.input;

import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.editor.LevelData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;

public class MainInput implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.B) {
            API.get(Editor.class).saveToFile();
            return true;
        }

        if (keycode == Input.Keys.F) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            return true;
        }

        if (keycode == Input.Keys.V) {
            World world = API.get(World.class);
            world.setGravEnabled(!world.isGravEnabled());
            System.out.println("world.isGravEnabled() = " + world.isGravEnabled());
            return true;
        }

        if (keycode == Input.Keys.NUM_4) {
            Gdx.graphics.setWindowedMode(1280, 720);
            return true;
        }

        if (keycode == Input.Keys.C) {
            String directoryPath = "savedLevels";

            // Get the directory as a FileHandle
            FileHandle dirHandle = Gdx.files.internal(directoryPath);

            // List all files in the directory
            FileHandle[] files = dirHandle.list();

            // Iterate through the files and print their names
            String name = "";
            if (files.length > 0) {
                name = files[0].file().getName();
            }

            String finalName = directoryPath + "/" + name;

            final LevelData levelData = API.get(Editor.class).loadLevelData(finalName);
            API.get(World.class).loadFromLevelData(levelData,true);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
