package com.art.prototype.ui.buttons;

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

    NotificationWidget notificationWidget;
    @Override
    public void addNotificationWidget(NotificationWidget widget) {
        this.notificationWidget = widget;
        addActor(widget);
        updateNotificationWidgetPosition();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (notificationWidget != null) {
            updateNotificationWidgetPosition();
        }

        if (clickBox != null) {
            updateClickBox();
        }
    }

    private void updateNotificationWidgetPosition () {
        if (notificationWidget == null) return;

        final float posX;
        final float posY;

        if (Align.isTop(notificationAlignment)) {
            posY = getHeight() - notificationWidget.getHeight() / 2.0f - notificationOffsetY;
        } else {
            posY = - notificationWidget.getHeight() / 2.0f + notificationOffsetY;
        }

        if (Align.isLeft(notificationAlignment)) {
            posX = -notificationWidget.getWidth() / 2.0f + notificationOffsetX;
        } else {
            posX = getWidth() - notificationWidget.getWidth() / 2.0f - notificationOffsetX;
        }

        notificationWidget.setPosition(posX, posY);
    }

    @Override
    public boolean isShowNumber() {
        return false;
    }

    @Override
    public boolean isShowNotification() {
        return true;
    }

    public void setLoadingMode () {
        disable();
        frontTable.setVisible(false);
        SpineActor spineActor = new SpineActor();
        spineActor.setSkeletonRenderer(GameUI.get().getSkeletonRenderer());
        spineActor.setFromAssetRepository("loading-dot");
        spineActor.playAnimation("animation");
        spineActor.setSpineScale(0.22f, 0, this.frontTable.getHeight() / 2 - this.offset);


        loadingSpineWrapper = new Table();
        loadingSpineWrapper.setFillParent(true);
        loadingSpineWrapper.add(spineActor).growY();

        this.addActor(loadingSpineWrapper);
    }

    public void removeLoadingMode () {
        enable();
        this.removeActor(loadingSpineWrapper);
        frontTable.setVisible(true);
    }


    public enum Style {
        YELLOW_BIG(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, ColorLibrary.YELLOW_BTN_BG.getColor(), ColorLibrary.YELLOW_BTN_FG.getColor(), 8, 22),
        BLUE_BIG(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 8, 22),
        BLUE_SQUARE_SMALL(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 8, 22),
        BLUE_SQUARE(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 8, 30),
        BLUE_HUGE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 10, 35),
        BLUE_SQUARE_SMALL_BOLD(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("1b6197"), Color.valueOf("#59a4f0"), 10, 12),
        RED(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#b34140"), Color.valueOf("#d95454"), 8, 25),
        RED_HUGE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#b34140"), Color.valueOf("#d95454"), 8, 35),
        RED_SQUARE(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#b34140"), Color.valueOf("#d95454"), 8, 30),
        RED_35_25_8_22(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#b34140"), Color.valueOf("#d95454"), 8, 22),
        RED_BRIGHTER(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#ba2f2f"), Color.valueOf("#d85554"), 8, 22),
        RED_VERY_BRIGHT(Squircle.SQUIRCLE_40, Squircle.SQUIRCLE_35, Color.valueOf("ba2f2f"), Color.valueOf("fe3f3f"), 8, 22),
        GREEN_BIG(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 8, 22),
        GREEN_HUGE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 10, 50),
        GREEN_MID(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 8, 35),
        GREEN_SMALL(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 8, 25),
        GREEN_TINY(Squircle.SQUIRCLE_25, Squircle.SQUIRCLE_15, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 5, 16),
        GREEN_SUPER_TINY(Squircle.SQUIRCLE_25, Squircle.SQUIRCLE_15, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 5, 10),
        GREEN_SQUARE(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 8, 30),
        GREEN_40_35_7_13(Squircle.SQUIRCLE_40, Squircle.SQUIRCLE_35, Color.valueOf("#519f4f"), Color.valueOf("#7ed97b"), 7, 13),

        ORANGE_BIG(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#ca6a1c"), Color.valueOf("#f7a92e"), 8, 22),
        YELLOW_SQUARE_SMALL(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#bb7d2b"), Color.valueOf("#e5ad4b"), 8, 22),
        YELLOW_SQUARE(Squircle.SQUIRCLE_35, Squircle.SQUIRCLE_25, Color.valueOf("#bb7d2b"), Color.valueOf("#e5ad4b"), 8, 30),

        MAIN_UI_WHITE_GRAY(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#918377"), Color.valueOf("#f4e6de"), 8, 20),
        MAIN_UI_WHITE_BLUE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#346097"), Color.valueOf("#f4e6de"), 8, 20),
        MAIN_UI_BLUE_BLUE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#346097"), Color.valueOf("#63a8f8"), 8, 20),
        MAIN_UI_GREEN_GREEN(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#54802e91d573"), Color.valueOf("#91d573"), 8, 20),

        SELECT_LANGUAGE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#918377"), Color.valueOf("#f4e6de"), Color.valueOf("#4589d1"), Color.valueOf("#59a4f0"), 6, 34),

        REFRESH_BUTTON(Squircle.SQUIRCLE_25, Squircle.SQUIRCLE_15, Color.valueOf("#568667"), Color.valueOf("#7ab58e"), 0, 10),

        PURPLE_WHITE(Squircle.SQUIRCLE_50, Squircle.SQUIRCLE_40, Color.valueOf("#7040a2"), Color.valueOf("#e1cfe9"), 8, 20),
        BLUE_WHITE_SMALL(Squircle.SQUIRCLE_40, Squircle.SQUIRCLE_35, Color.valueOf("#346097"), Color.WHITE, 8, 25),
        EMPTY(null, null, Color.WHITE, Color.WHITE, 0, 0),
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
            this.disabledBackground = disabledBackground == null ? ColorLibrary.OLD_SILVER.getColor() : disabledBackground ;
            this.disabledForeground = disabledForeground == null ? ColorLibrary.SILVER_CHALICE.getColor() : disabledForeground;
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
