package com.art.prototype.ui.buttons;

import com.art.prototype.ui.Colors;
import com.art.prototype.ui.FontSize;
import com.art.prototype.ui.labels.LabelFactory;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class FlatTextButton extends FlatButton {
    private Label textLabel;
    private FontSize fontSize;
    private Color color;
    private Color hoverColor = Color.WHITE;

    public FlatTextButton (FontSize fontSize, String text) {
        super();
        this.fontSize = fontSize;
        color = Color.WHITE;
        textLabel = LabelFactory.create(fontSize, text);
        textLabel.setAlignment(Align.center);
        setup();
        onHover = () -> textLabel.setColor(hoverColor);
        onHoverExit = () -> textLabel.setColor(color);
    }

    @Override
    protected void buildContent(Table content) {
        content.add(textLabel).grow();
    }

    public void setText (String text) {
        this.textLabel.setText(text);
    }

    public void setColor (Color color) {
        this.color = color;
        this.textLabel.setColor(color);
    }

    enum Type {
        EMPTY,
        FILLED,

    }

}
