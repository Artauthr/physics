package com.art.prototype.ui.labels;

import com.art.prototype.api.API;
import com.art.prototype.resources.ResourceManager;
import com.art.prototype.ui.FontSize;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelFactory {
    public static Label create (FontSize fontSize, String text, Color color) {
        ResourceManager resourceManager = API.get(ResourceManager.class);
        Label.LabelStyle labelStyle = resourceManager.getStyleMap().get(fontSize);
        Label label = new Label(text, labelStyle);
        label.setColor(color);
        return label;
    }

    public static Label create (FontSize fontSize, String text) {
        return create(fontSize, text, Color.WHITE);
    }

    public static Label create (FontSize fontSize) {
        return create(fontSize, "", Color.WHITE);
    }
}
