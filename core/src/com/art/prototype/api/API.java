package com.art.prototype.api;

import com.art.prototype.World;
import com.art.prototype.editor.Editor;
import com.art.prototype.input.InputManager;
import com.art.prototype.render.Graphics2D;
import com.art.prototype.resources.ResourceManager;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class API {
    private ObjectMap<Class<?>, Object> map = new ObjectMap<>();

    private static API instance;

    private API () {
        registerClasses();
    }

    private void registerClasses () {
        register(Graphics2D.class);
        register(InputManager.class);
        register(Editor.class);
        register(World.class);
        register(ResourceManager.class);
    }

    public static <T> T get (Class<T> cls) {
        return (T) getInstance().map.get(cls);
    }

    private <T> void register (Class<T> cls) {
        try {
            final T instance = ClassReflection.newInstance(cls);
            map.put(cls, instance);
        } catch (ReflectionException exception) {
            System.err.println(cls.getSimpleName() + " NOT REGISTERED");
        }
    }

    public static <T> void register (T obj) {
        final Class<?> aClass = obj.getClass();
        getInstance().map.put(aClass,obj);
    }

    public static API getInstance() {
        if (instance == null) {
            instance = new API();
        }
        return instance;
    }
}
