package com.art.prototype.ui.buttons;

import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.Colors;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import lombok.Setter;

public abstract class FlatButton extends Table {
    private Vector2 tmpv = new Vector2();
    private Drawable normalDrawable;
    private Drawable hoverDrawable;
    private Table content;

    protected Runnable onHover;
    protected Runnable onHoverExit;

    @Setter
    protected Runnable onClick;

    @Setter
    protected Runnable onTouchDown;


    public FlatButton() {
        normalDrawable = ResourceManager.getDrawable("ui/square-border-thick-big", Colors.CORAL);
        hoverDrawable = ResourceManager.getDrawable("ui/square-border-thick-big", Color.WHITE); // Replace with your hover drawable
        this.setBackground(normalDrawable);
        this.content = new Table();
    }

    protected void setup () {
        buildContent(content);
        initListeners();
        this.add(content).grow();
        this.setTransform(true);

    }

    protected abstract void buildContent (Table content);

    private float pressDurationCounter = 0;
    private boolean beingPressed;
    private boolean pressReleased;
    private final float MIN_SCALE = 0.76f;
    private float scaleBack = 0;
    private final float SCALE_STEP = 1.2f;
    private boolean isHovering = false;

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setOrigin(0);
        if (beingPressed) {
            pressDurationCounter += SCALE_STEP * delta;
            float sub = 1 - pressDurationCounter;
            float max = Math.max(MIN_SCALE, sub);
            this.setScale(max);
        }

        if (beingPressed && pressReleased) {
            beingPressed = false;
            pressReleased = false;
            scaleBack = 1 - getScaleX();
            pressDurationCounter = 0;
            if (isHovering) {
                onClick.run();
            }
        }

        if (scaleBack > 0) {
            scaleBack -= SCALE_STEP * delta * 2f;
            this.setScale(Math.max(1, scaleBack));
        }

    }


    private void initListeners() {
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                if (pointer == 0) {
                    beingPressed = true;
                    if (onTouchDown != null) {
                        onTouchDown.run();
                    }
                    return true;
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                pressReleased = true;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) { // -1 indicates the mouse moved over the actor without a button pressed
                    FlatButton flatButton = FlatButton.this;
                    flatButton.setBackground(hoverDrawable);
                    isHovering = true;
                    if (flatButton.onHover != null) {
                        flatButton.onHover.run();
                        return;
                    }
                }
//                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) { // -1 indicates the mouse moved away from the actor without a button pressed
                    FlatButton flatButton = FlatButton.this;
                    flatButton.setBackground(normalDrawable);
                    isHovering = false;
                    if (flatButton.onHoverExit != null) {
                        flatButton.onHoverExit.run();
                    }
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });
    }
}

