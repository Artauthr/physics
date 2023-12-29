package com.art.prototype.ui;

import com.art.prototype.api.API;
import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public enum Squircle {
    SQUIRCLE_10(15, "ui-white-squircle"),
    SQUIRCLE_15(20, "ui-white-squircle"),
    SQUIRCLE_20(35, "ui-white-squircle"),
    SQUIRCLE_25(25, "ui-white-squircle"),
    SQUIRCLE_35(35, "ui-white-squircle"),
    SQUIRCLE_40(40, "ui-white-squircle"),
    SQUIRCLE_50(50, "ui-white-squircle"),

    LEAF_35(35, "ui-white-leaf"),
    LEAF_50(50, "ui-white-leaf"),

    SQUIRCLE_35_BORDER(35, "ui-white-squircle-border"),
    SQUIRCLE_50_BORDER(50, "ui-white-squircle-border"),

    SQUIRCLE_35_BTM(35, "ui-white-squircle-bottom"),
    SQUIRCLE_40_BTM(40, "ui-white-squircle-bottom"),
    SQUIRCLE_50_BTM(50, "ui-white-squircle-bottom"),
    SQUIRCLE_50_TOP(50, "ui-white-squircle-top");

    private int radius;
    private final String name;

    Squircle(int radius, String name) {
        this.radius = radius;
        this.name = name;
    }

    public Drawable getDrawable() {
        return getDrawable(Color.WHITE);
    }

    public Drawable getDrawable(Color color) {
        return API.get(ResourceManager.class).obtainDrawable("ui/" + name + "-" + radius, color);
    }

    public static Drawable getSquircle (int radius, Color color) {
        return ResourceManager.getDrawable("ui/ui-white-squircle-" + radius, color);
    }

    public static Drawable getSquircleBtm (int radius, Color color) {
        return ResourceManager.getDrawable("ui/ui-white-squircle-bottom-" + radius, color);
    }

    public static Drawable getBorder (int radius, Color color) {
        return ResourceManager.getDrawable("ui/ui-white-squircle-border-" + radius, color);
    }

    public static Drawable getLeaf (int radius, Color color) {
        return ResourceManager.getDrawable("ui/ui-white-leaf-" + radius, color);
    }
}
