package com.art.prototype.input;

import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.World;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import lombok.Setter;

public class WorldInteraction implements InputProcessor {

    @Setter
    private World worldRef;

    @Setter
    private ExtendViewport extendViewport;

    private Vector2 tmp1 = new Vector2();

    @Override
    public boolean keyDown(int keycode) {
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
            Vector2 tmp = Pools.obtain(Vector2.class);
            tmp.set(screenX, screenY);

            extendViewport.unproject(tmp);
            worldRef.spawnPlatformAt(tmp.x, tmp.y);
            Pools.free(tmp);
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

        switch (editorState) {
            case ADDING:

                break;
            case REMOVING:
                tmp1.set(screenX, screenY);
                extendViewport.unproject(tmp1);
                World world = API.get(World.class);
                Array<StaticBody> staticBodies = world.getStaticBodies();

                for (StaticBody staticBody : staticBodies) {
                    Rectangle physicsBodyRect = Utils.getPhysicsBodyRect(staticBody);
                    if (physicsBodyRect.contains(tmp1)) {
                        editor.highlightRed();
                    }
                }
                break;
            case DISABLED:
                return false;
        }



        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
