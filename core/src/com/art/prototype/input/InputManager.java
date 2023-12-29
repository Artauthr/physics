package com.art.prototype.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class InputManager {
    private InputMultiplexer multiplexer;

    private ObjectMap<Class<InputProcessor>, InputProcessor> map;

    public InputManager () {
        multiplexer = new InputMultiplexer();
    }

    public void registerProcessor (Class<InputProcessor> cls) {
        try {
            final InputProcessor instance = ClassReflection.newInstance(cls);
            map.put(cls, instance);
        } catch (ReflectionException exception){

        }

    }
}
