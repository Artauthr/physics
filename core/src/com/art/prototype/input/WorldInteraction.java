package com.art.prototype.input;

import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Setter;

public class WorldInteraction implements InputProcessor {
    private Vector2 tmp1 = new Vector2();

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            Editor editor = API.get(Editor.class);
            if (editor.getEditorState() != Editor.State.DISABLED) {
                editor.quitMode();
                return true;
            }
            API.get(GameUI.class).setMainLayout();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
        if (button == Input.Buttons.RIGHT) {
//            Vector2 tmp = Pools.obtain(Vector2.class);
//            tmp.set(screenX, screenY);
//
//            extendViewport.unproject(tmp);
//            worldRef.spawnPlatformAt(tmp.x, tmp.y);
//            Pools.free(tmp);
        }

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
                    System.out.println("PLATFORM SPAWNED AT " + tmp);
                    return true;
                case REMOVING:
                    if (currentHoveredObject != null) {
                        API.get(World.class).removeStaticBody(currentHoveredObject);
                    }
                    return true;
                case DISABLED:
                    return false;
                case RESIZING:
                    if (currentHoveredObject != null) {
                        API.get(Editor.class).addGizmoToObject(currentHoveredObject);
                    }
            }
        }
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
        Editor editor = API.get(Editor.class);
        Editor.State editorState = editor.getEditorState();

        timer += Gdx.graphics.getDeltaTime();

        if (timer >= MOUSE_POLL_RATE) {
            findCurrentHovered(screenX, screenY);
            timer = 0;
        }

        switch (editorState) {
            case ADDING:

                return false;
            case REMOVING:
                if (editor.getCurrentHoveredObject() != null) {
                    editor.hideHighlighter();
                }
                return true;
            case DISABLED:
                return false;
        }



        return false;
    }

    private final float MOUSE_POLL_RATE = 0.4f;
    private float timer = 0;

    private StaticBody findCurrentHovered (float screenX, float screenY) {
        tmp1.set(screenX, screenY);
        Graphics2D.get().getGameViewport().unproject(tmp1);
        World world = API.get(World.class);
        Array<StaticBody> staticBodies = world.getStaticBodies();
        Editor editor = API.get(Editor.class);

        for (StaticBody staticBody : staticBodies) {
            Rectangle physicsBodyRect = Utils.getPhysicsBodyRect(staticBody);
            // TODO: 12/30/2023 Optimise??? <- check the date tho
            float rW = (physicsBodyRect.width * 1.2f - physicsBodyRect.width) * 0.5f;
            float rH = (physicsBodyRect.height * 2.1f - physicsBodyRect.height) * 0.5f;
            physicsBodyRect.setSize(physicsBodyRect.width * 1.2f, physicsBodyRect.height * 2.1f);
            physicsBodyRect.setPosition(physicsBodyRect.x - rW, physicsBodyRect.y - rH);

            if (physicsBodyRect.contains(tmp1)) {
                editor.setCurrentHoveredObject(staticBody);
                editor.highlightPhysicsObject(staticBody, Color.RED);
                return staticBody;
            }
        }
        return null;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        CameraController cameraController = API.get(CameraController.class);
        if (amountY != 0 && cameraController.getCameraMode() == CameraController.Mode.MANUAL_CONTROL) {
            API.get(CameraController.class).queueMovement(amountY);
        }
        return false;
    }
}
