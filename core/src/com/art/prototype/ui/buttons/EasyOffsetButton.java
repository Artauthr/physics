package com.art.prototype.ui.buttons;

import com.art.prototype.ui.Squircle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import lombok.Getter;
import lombok.Setter;

public class EasyOffsetButton extends Table {

    @Getter
    protected Table frontTable;
    private float offset;
    private float pad;
    private ClickListener listener;
    private Cell<Table> frontCell;
    private Table loadingSpineWrapper;

    @Setter @Getter
    protected Runnable onClick;
    @Setter
    private Runnable onTouchDown;
    @Setter
    private float pressDuration = 0.05f;
    private boolean pressing;
    private boolean shouldRelease;
    private boolean clicked;
    private boolean releasing;
    private boolean needsConfirmation = false;
    private String confirmDialogHeaderText;
    private String confirmDialogBodyText;
    private float timer;

    private final Vector2 size = new Vector2();

    @Getter
    protected boolean enabled = true;
    @Getter
    protected boolean visuallyEnabled = true;
    @Getter
    protected Style style;

    // notifications
    @Setter
    private int notificationAlignment = Align.topLeft;
    @Setter
    private float notificationOffsetX = 30;
    @Setter
    private float notificationOffsetY = 30;

    public EasyOffsetButton () {

    }

    public void build (Style style) {
        build();
        setStyle(style);
    }

    public void build() {
        frontTable = new Table();
        frontCell = add(frontTable).grow();
        buildInner(frontTable);

        initListeners();
    }

    protected void buildInner(Table container) {

    }

    private void initListeners () {
        listener = new ClickListener () {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isAnimating()) return false;

                // get the size before animations
                size.set(getWidth(), getHeight());

                pressing = true;
                if (onTouchDown != null) onTouchDown.run();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pressing) {
                    // schedule release
                    shouldRelease = true;
                } else releasing = true;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                clicked = true;
            }
        };
        addListener(listener);
        setTouchable(Touchable.enabled);
    }

    private boolean isAnimating () {
        return pressing || releasing;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!pressing && shouldRelease) {
            releasing = true; shouldRelease = false;
        }

        if (!isAnimating()) return;

        timer += delta;

        if (pressing) {
            final float padBottom = MathUtils.clamp(Interpolation.sineIn.apply(offset, 0, timer / pressDuration), 0, offset);
            final float padTop = offset - padBottom;
            setHeight(size.y - padTop);
            frontCell.padBottom(padBottom + pad);

            if (timer >= pressDuration)  {
                setHeight(size.y - offset);
                frontCell.padBottom(pad);
                pressing = false;
                timer = 0;
            }
        } else if (releasing) {
            final float padBottom = MathUtils.clamp(Interpolation.sineOut.apply(0, offset, timer / pressDuration), 0, offset);
            final float padTop = offset - padBottom;
            setHeight(size.y - padTop);
            frontCell.padBottom(padBottom + pad);

            if (timer >= pressDuration)  {
                setHeight(size.y);
                frontCell.padBottom(offset + pad);
                releasing = false;
                timer = 0;

                if (clicked) {
                    clicked = false;
                    triggerClicked();
                }
            }
        }

        invalidate();
    }

    protected void triggerClicked () {
        if (onClick == null) return;
        onClick.run();
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public void enable () {
        this.enabled = true;
        setTouchable(Touchable.enabled);
        visuallyEnable();
    }

    public void disable () {
        this.enabled = false;
        setTouchable(Touchable.disabled);
        visuallyDisable();
    }

    public void visuallyEnable() {
        this.visuallyEnabled = true;
        // update background
        frontTable.setBackground(style.getInnerBackground(true));
        setBackground(style.getOuterBackground(true));
    }

    public void visuallyDisable() {
        this.visuallyEnabled = false;
        // update background
        frontTable.setBackground(style.getInnerBackground(false));
        setBackground(style.getOuterBackground(false));
    }

    protected void updateVisually () {
        if (enabled) {
            visuallyEnable();
        }  else {
            visuallyDisable();
        }
    }

    public void setStyle (Style style) {
        this.style = style;

        final Squircle outer = style.outer;
        if (outer == null) {
            setBackground((Drawable) null);
        } else {
            setBackground(outer.getDrawable(style.enabledBackground));
        }

        final Squircle inner = style.inner;
        if (inner == null) {
            this.frontTable.setBackground((Drawable) null);
        } else {
            this.frontTable.setBackground(inner.getDrawable(style.enabledForeground));
        }

        this.offset = style.offset;
        this.pad = style.pad;
        this.frontCell.pad(pad).padBottom(offset + pad);
    }

    public void setPad (float pad) {
        this.pad = pad;
        this.frontCell.pad(pad).padBottom(offset + pad);
        this.updateVisually();
    }

    public void setOffset (float offset) {
        this.offset = offset;
        this.frontCell.padBottom(offset + pad);
        this.updateVisually();
    }

    public void addConfirmDialog (String headerText, String bodyText) {
        this.needsConfirmation = true;
        this.confirmDialogHeaderText = headerText;
        this.confirmDialogBodyText = bodyText;
    }


    public enum Style {
        BLUE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 8, 22),
        RED(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#470909"), Color.valueOf("#a12828"), 8, 22),
        GREEN(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#0c4d1d"), Color.valueOf("#28a148"), 8, 22),

        ;

        private final Squircle outer;
        private final Squircle inner;
        @Getter
        private final Color enabledBackground;
        private final Color enabledForeground;
        @Getter
        private final Color disabledBackground;
        private final Color disabledForeground;
        private final float pad;
        private final float offset;

        Style(Squircle outer, Squircle inner, Color enabledBackground, Color enabledForeground, float pad, float offset) {
            this(outer, inner, enabledBackground, enabledForeground, null, null, pad, offset);
        }

        Style(Squircle outer, Squircle inner, Color enabledBackground, Color enabledForeground, Color disabledBackground, Color disabledForeground, float pad, float offset) {
            this.outer = outer;
            this.inner = inner;
            this.enabledBackground = enabledBackground;
            this.enabledForeground = enabledForeground;
            this.disabledBackground = disabledBackground == null ? Color.GRAY : disabledBackground ;
            this.disabledForeground = disabledForeground == null ? Color.LIGHT_GRAY : disabledForeground;
            this.pad = pad;
            this.offset = offset;
        }

        public Drawable getInnerBackground (boolean enabled) {
            if (inner == null) return null;
            if (enabled) {
                return inner.getDrawable(enabledForeground);
            }
            return inner.getDrawable(disabledForeground);
        }

        public Drawable getOuterBackground (boolean enabled) {
            if (outer == null) return null;
            if (enabled) {
                return outer.getDrawable(enabledBackground);
            }
            return outer.getDrawable(disabledBackground);
        }
    }


    // click box
    private Table clickBox;
    private float clickBoxPadX;
    private float clickBoxPadY;
    private float clickBoxOffsetX;
    private float clickBoxOffsetY;

    public void initClickBox () {
        if (clickBox != null) return;
        clickBox = new Table();
        addActor(clickBox);
        clickBox.setTouchable(Touchable.enabled);
    }

    public void setClickBoxPad (float pad) {
        setClickBoxPad(pad, pad);
    }

    public void setClickBoxPad (float padX, float padY) {
        if (clickBox == null) initClickBox();
        this.clickBoxPadX = padX;
        this.clickBoxPadY = padY;

        updateClickBox();
    }

    public void setClickBoxOffset (float offset) {
        setClickBoxOffset(offset, offset);
    }

    public void setClickBoxOffset (float offsetX, float offsetY) {
        if (clickBox == null) initClickBox();
        this.clickBoxOffsetX = offsetX;
        this.clickBoxOffsetY = offsetY;

        updateClickBox();
    }

    protected void updateClickBox () {
        final float width = clickBoxPadX + getWidth();
        final float height = clickBoxPadY + getHeight();
        clickBox.setSize(width, height);

        final float x = -clickBoxPadX / 2.0f + clickBoxOffsetX;
        final float y = -clickBoxPadY / 2.0f + clickBoxOffsetY;
        clickBox.setPosition(x, y);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        if (clickBox != null) {
            addActor(clickBox);
        }
    }
}
