package com.art.prototype.ui.editor;

import com.art.prototype.StaticBody;
import com.art.prototype.Utils;
import com.art.prototype.api.API;
import com.art.prototype.editor.Editor;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Scaling;
import lombok.Getter;

public class ObjectTransformer extends Table {
    private Array<ResizeHandle> handles;
    private StaticBody binded;
    private Image previewImg;
    private final int CLAMP = 150;
    private Vector2 dragValues = new Vector2();
    private Vector2 startedMovingCoords = new Vector2();
    private Table inside;

    @Getter
    private boolean active;

    public ObjectTransformer() {
        handles = new Array<>();
        previewImg = new Image(ResourceManager.getDrawable("ui/ui-white-pixel"), Scaling.fit);

        final Table top = new Table();
        final Table bottom = new Table();

        for (int i = 0; i < 4; i++) {
            ResizeHandle handle = new ResizeHandle();
            this.handles.add(handle);
        }
        final float PAD = -20;

        ResizeHandle handle = handles.get(0);
        handle.setAlign(Align.topLeft);

        ResizeHandle handle1 = handles.get(1);
        handle1.setAlign(Align.topRight);

        ResizeHandle handle2 = handles.get(2);
        handle2.setAlign(Align.bottomLeft);

        ResizeHandle handle3 = handles.get(3);
        handle3.setAlign(Align.bottomRight);

        inside = new Table();
        inside.setTouchable(Touchable.enabled);

        top.add(handle).expandX().left().pad(PAD);
        top.add(handle1).expandX().right().pad(PAD);
        bottom.add(handle2).expandX().left().pad(PAD);
        bottom.add(handle3).expandX().right().pad(PAD);

        this.add(top).growX();
        this.row();
        this.add(inside).grow().minSize(25,25);
        this.row();
        this.add(bottom).growX();

        initListeners();
    }


    public void bindToObject(StaticBody object) {
        GameUI gameUI = API.get(GameUI.class);
        Table rootUI = gameUI.getRootUI();
        Vector2 unProject = Utils.unProjectScl(object);

        float viewportRatio = Graphics2D.get().getViewportRatio();

        rootUI.addActor(this);
        this.binded = object;
        this.setPosition(unProject.x, unProject.y);
        this.setSize(object.getSize().x * viewportRatio, object.getSize().y * viewportRatio);
        active = true;
    }

    public void unBind () {
        GameUI gameUI = API.get(GameUI.class);
        Table rootUI = gameUI.getRootUI();
        rootUI.removeActor(this);
        active = false;
    }

    public void reEvaluateSizePos () {
        if (binded == null) {
            this.setVisible(false);
            System.err.println("No bound object to re-evaluate");
            return;
        }

        Vector2 unProject = Utils.unProjectScl(binded);

        this.setPosition(unProject.x, unProject.y);
        float viewportRatio = Graphics2D.get().getViewportRatio();
        this.setSize(binded.getSize().x * viewportRatio, binded.getSize().y * viewportRatio);
    }


    /*
    resize can be written in a better way logically,
    but we are keeping this very simple, so I am going to leave this for now
     */
    private void resize (int align, float x, float y) {
        switch (align) {
            case Align.topLeft:
                binded.getSize().x += -x;
                binded.getPos().x += x;

                binded.getSize().y += y;
                break;
            case Align.bottomLeft:
                binded.getSize().x += -x;
                binded.getPos().x += x;

                binded.getSize().y += -y;
                binded.getPos().y += y;
                break;
            case Align.topRight:
                binded.getSize().x += x;

                binded.getSize().y += y;
                break;
            case Align.bottomRight:
                binded.getSize().x += x;

                binded.getSize().y += -y;
                binded.getPos().y += y;
                break;
        }
    }

//    float initialX, float initialY,
    private void moveObject (float targetX, float targetY) {
        System.out.println("x = " + targetX);
        System.out.println("y = " + targetY);

        binded.getPos().add(targetX, targetY);
    }

    private Vector2 flattenDragValues (float x, float y) {
        float flatX = MathUtils.clamp(x, -CLAMP, CLAMP);
        float flatY = MathUtils.clamp(y, -CLAMP, CLAMP);

        float dampAmountX = 0.06f;
        float dampAmountY = 0.01f;

        float amountX = flatX * dampAmountX;
        float amountY = flatY * dampAmountY;

        return dragValues.set(amountX, amountY);
    }



    private void initListeners () {
        for (ResizeHandle handle : handles) {
            handle.addListener(new ClickListener() {
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    super.touchDragged(event, x, y, pointer);
                    if (pointer == Input.Buttons.LEFT) {
                        if (binded == null) return;
                        dragValues.set(flattenDragValues(x, y));
                        resize(handle.getAlign(), dragValues.x, dragValues.y);
                        reEvaluateSizePos();
                    }
                }
            });
        }

        inside.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startedMovingCoords.set(x, y);
                return super.touchDown(event, x, y, pointer, button);
            }


            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (pointer == Input.Buttons.LEFT) {
//                    System.err.println("ITS FUCKING CLICKED");
                    super.touchDragged(event, x, y, pointer);

                    Vector2 pooledVec = Pools.obtain(Vector2.class);

                    pooledVec.set(flattenDragValues(x, y));
                    moveObject(pooledVec.x, pooledVec.y);

                    Pools.free(pooledVec);
                    reEvaluateSizePos();
                }
            }
        });
    }
}

