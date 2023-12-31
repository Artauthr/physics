package com.art.prototype.input;

import com.art.prototype.api.API;
import com.art.prototype.ui.GameUI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class InputManager {
    private InputMultiplexer multiplexer;

    private OrderedMap<Class<? extends InputProcessor>, InputProcessor> map;

    public InputManager () {
        map = new OrderedMap<>();
        multiplexer = new InputMultiplexer();
        setAsGlobalInputProcessor();
    }

    public void addUIProcessor() {
        Stage stage = API.get(GameUI.class).getStage();
        map.put(stage.getClass(), stage);
        multiplexer.addProcessor(stage);
    }

    private void setAsGlobalInputProcessor() {
        for (ObjectMap.Entry<Class<? extends InputProcessor>, InputProcessor> entry : map) {
            multiplexer.addProcessor(entry.value);
        }
        Gdx.input.setInputProcessor(multiplexer);
        System.err.println("INPUT PROC SET");
    }

    public <T extends InputProcessor> void registerProcessor (Class<T> cls) {
        try {
            final InputProcessor instance = ClassReflection.newInstance(cls);
            map.put(cls, instance);
        } catch (ReflectionException exception){
            System.err.println("couldn't register processor " + cls.getSimpleName());
        }
    }

    public void enableAll() {
        multiplexer.clear();
        for (ObjectMap.Entry<Class<? extends InputProcessor>, InputProcessor> entry : map) {
            multiplexer.addProcessor(entry.value);
        }
    }

    public void disableInputProcessor (Class<? extends InputProcessor> cls) {
        InputProcessor inputProcessor = map.get(cls);
        multiplexer.removeProcessor(inputProcessor);
    }


}
