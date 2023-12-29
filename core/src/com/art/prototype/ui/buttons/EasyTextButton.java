//package com.art.prototype.ui.buttons;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.scenes.scene2d.ui.Cell;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import lombok.Getter;
//
//public class EasyTextButton extends EasyOffsetButton {
//    protected final FontSize fontSize;
//    protected String text;
//    @Getter
//    private Label label;
//    protected Color enabledLabelColor;
//    @Getter
//    private Cell<Label> labelCell;
//
//    public EasyTextButton(Style style, String text) {
//        this.style = style;
//        this.text = text;
//        build(style);
//    }
//
//    @Override
//    protected void buildInner(Table container) {
//        label = Labels.make(fontSize, FontType.BOLD, text);
//        enabledLabelColor = Color.WHITE;
//        labelCell = container.add(label).expand().center().pad(20).padLeft(30).padRight(30);
//    }
//
//    public void setTextColor(Color color) {
//        this.enabledLabelColor = color;
//        if (enabled) {
//            label.setColor(color);
//        }
//    }
//
//    public void setTextColorAsBackground () {
//        final Color enabledBackground = getStyle().getEnabledBackground();
//        setTextColor(enabledBackground);
//    }
//
//    public void setText (String text) {
//        this.text = text;
//        label.setText(text);
//    }
//
//    @Override
//    public void disable() {
//        super.disable();
//        label.setColor(ColorLibrary.OLD_SILVER.getColor());
//    }
//
//    @Override
//    public void enable() {
//        super.enable();
//        label.setColor(enabledLabelColor);
//    }
//}
