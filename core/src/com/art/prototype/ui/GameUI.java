package com.art.prototype.ui;

import com.art.prototype.StaticBody;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;

public class GameUI {
    @Getter
    private Stage stage;

    @Getter
    private Table rootUI;
    private MainScreenLayout mainScreenLayout;
    private Table layoutParent;

    @Getter
    private Cell<Table> layoutCell;

    private ObjectMap<Class<? extends ALayout>, ALayout> layoutsCacheMap;

    public GameUI (Viewport viewport, Batch batch) {
        layoutsCacheMap = new ObjectMap<>();

        stage = new Stage(viewport, batch);

        rootUI = new Table();
        rootUI.setFillParent(true);
        rootUI.setTouchable(Touchable.enabled);

        layoutParent = new Table();
        layoutParent.setFillParent(true);
        layoutCell = layoutParent.add().grow();

//        Label.LabelStyle labelStyle = API.get(ResourceManager.class).getStyleMap().get(FontSize.SIZE_28);
//        Label label = new Label("PEE-PEE-POO-POO", labelStyle);


        rootUI.addActor(layoutParent);
        setLayout(MainScreenLayout.class);

//        rootUI.add(label).expand();

        stage.addActor(rootUI);

        BitmapFont font = new BitmapFont();
    }

//    public void res

    public <T extends ALayout> T getLayout (Class<? extends ALayout> cls) {
        if (!layoutsCacheMap.containsKey(cls)) {
            try {
                ALayout layoutInstance = ClassReflection.newInstance(cls);
                layoutsCacheMap.put(cls, layoutInstance);
            } catch (ReflectionException exception) {
                exception.printStackTrace();
            }
        }
        return (T) layoutsCacheMap.get(cls);
    }

    public void setLayout (Class<? extends ALayout> cls) {
        ALayout layout = getLayout(cls);
        this.layoutCell.setActor(layout);
    }


    public void act () {
        stage.act();
    }

    public void draw () {
        stage.getViewport().apply();
        stage.draw();
    }

    public void setMainLayout() {
        if (this.layoutCell.getActor() != mainScreenLayout) {
            this.layoutCell.setActor(mainScreenLayout);
        }
    }
}
