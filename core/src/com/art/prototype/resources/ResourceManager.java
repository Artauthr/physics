package com.art.prototype.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.Getter;

public class ResourceManager {
    private AssetManager assetManager;

    @Getter
    private Skin uiSkin;

    private TextureAtlas atlas;

    public ResourceManager () {
        assetManager = new AssetManager();

    }

    private void load () {
        assetManager.update();
    }

    private void finishLoading () {
        this.atlas = assetManager.get("gameassets/gameatlas.atlas", TextureAtlas.class);

        this.uiSkin = new Skin(atlas);
    }
}
