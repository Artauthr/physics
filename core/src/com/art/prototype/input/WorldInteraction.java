package com.art.prototype.input;

import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.ui.Colors;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class WorldInteraction implements InputProcessor {
    private Vector2 tmp1 = new Vector2();

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Editor editor = API.get(Editor.class);
            if (editor.getEditorState() != Editor.State.DISABLED) {
                editor.quitMode();
            } else {
                API.get(GameUI.class).setMainLayout();
            }
            return true;
        }
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Editor editor = API.get(Editor.class);
        Editor.State editorState = editor.getEditorState();

        if (editor.getCurrentHoveredObject() == null) {
            editor.hideHighlighter();
            return false;
        } else {
            editor.highlightPhysicsObject(editor.getCurrentHoveredObject(), Colors.SKY);
            return false;
        }

//        switch (editorState) {
//            case ADDING:
//            case DISABLED:
//                return false;
//            case REMOVING:
//                editor.highlightPhysicsObject(editor.getCurrentHoveredObject(), Color.RED);
//                return true;
//            case TRANSFORMING:
//
//                return true;
//        }
//        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        CameraController cameraController = API.get(CameraController.class);
        if (amountY != 0 && cameraController.getCameraMode() == CameraController.Mode.MANUAL_CONTROL) {
            API.get(CameraController.class).queueMovement(amountY);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Editor editor = API.get(Editor.class);
            Editor.State editorState = editor.getEditorState();
            StaticBody currentHoveredObject = editor.getCurrentHoveredObject();
            switch (editorState) {
                case ADDING:
                    Vector2 tmp = Pools.obtain(Vector2.class);
                    tmp.set(screenX, screenY);
                    Graphics2D.get().getGameViewport().unproject(tmp);
                    API.get(World.class).spawnPlatformAt(tmp.x, tmp.y);
                    return true;
                case REMOVING:
                    if (currentHoveredObject != null) {
                        API.get(World.class).removeStaticBody(currentHoveredObject);
                        return true;
                    }
                    break;
                case DISABLED:
                    return false;
                case TRANSFORMING:
                    if (currentHoveredObject != null) {
                        API.get(Editor.class).addTransformerToObject(currentHoveredObject);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }



// ------------UNUSED--------------------------------------------------
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
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

}
