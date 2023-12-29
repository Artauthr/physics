package com.art.prototype.resources;

import com.art.prototype.api.API;
import com.art.prototype.ui.FontSize;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.Getter;

public class ResourceManager {
    private AssetManager assetManager;
    @Getter
    private Skin uiSkin;
    private TextureAtlas atlas;
    private ObjectMap<Object, Drawable> drawableCache;
    private final ObjectMap<String, ObjectMap<Object, Object>> drawableKeyCache;

    @Getter
    private ObjectMap<FontSize, Label.LabelStyle> styleMap;
    private ObjectMap<FontSize, BitmapFont> sizeFontMap;

    private boolean loading = false;

    public ResourceManager () {
        assetManager = new AssetManager();
        drawableCache = new ObjectMap<>();
        drawableKeyCache = new ObjectMap<>();
        sizeFontMap = new ObjectMap<>();
        styleMap = new ObjectMap<>();
    }

    public void startLoading () {
        assetManager.load("gameassets/gameatlas.atlas", TextureAtlas.class);
        loadFonts();
        loading = true;
    }

    public boolean updateLoading () {
        boolean complete = assetManager.update(16);
        if (complete) {
            if (loading) {
                loading = false;
                finishLoading();
            }
        }
        return complete;
    }

    private void loadFonts () {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Medium.ttf"));
        for (FontSize value : FontSize.values()) {
            int fontSize = value.getSize();
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = fontSize;
            BitmapFont generatedFont = generator.generateFont(parameter);
            sizeFontMap.put(value, generatedFont);
        }
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        generateLabelStyles();
    }

    private void generateLabelStyles () {
        for (ObjectMap.Entry<FontSize, BitmapFont> entry : sizeFontMap) {
            BitmapFont font = entry.value;
            FontSize fontSize = entry.key;

            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
            styleMap.put(fontSize, labelStyle);
        }
    }

    public Drawable obtainDrawable (String region, Color color) {
        Object key = getKeyFromRegionColor(region, color);

        if (drawableCache.containsKey(key)) {
            return drawableCache.get(key);
        }

        Drawable drawable = uiSkin.newDrawable(region, color);
        drawableCache.put(key, drawable);

        return drawable;
    }

    private Object getKeyFromRegionColor(String region, Color color) {
        if(!drawableKeyCache.containsKey(region)) {
            drawableKeyCache.put(region, new ObjectMap<>());
        }
        if(!drawableKeyCache.get(region).containsKey(color)) {
            drawableKeyCache.get(region).put(color, region + color.toString());
        }

        return drawableKeyCache.get(region).get(color);
    }

    private void finishLoading () {
        this.atlas = assetManager.get("gameassets/gameatlas.atlas", TextureAtlas.class);
        this.uiSkin = new Skin(atlas);
    }

    public static Drawable getDrawable (String region, Color color) {
        ResourceManager resourceManager = API.get(ResourceManager.class);
        return resourceManager.obtainDrawable(region, color);
    }

    public static Drawable getDrawable (String region) {
        ResourceManager resourceManager = API.get(ResourceManager.class);
        return resourceManager.obtainDrawable(region, Color.WHITE);
    }

}
