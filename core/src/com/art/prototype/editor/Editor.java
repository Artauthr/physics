package com.art.prototype.editor;

import com.art.prototype.World;
import com.art.prototype.api.API;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class Editor {
    public Editor () {

    }

    public void saveToFile () {
        LevelData levelData = new LevelData();
        levelData.setWorldRef(API.get(World.class));
        saveLevelData(levelData);
        System.out.println("=======================LEVEL SAVED=======================");
    }

    public void saveLevelData(LevelData levelData) {
        Json json = new Json();
        String jsonData = json.toJson(levelData); // Serialize your object

        FileHandle file = Gdx.files.local("levelData.json"); // Create a FileHandle
        file.writeString(jsonData, false); // Write the string to a file
    }

    public LevelData loadLevelData (String path) {
        FileHandle file = Gdx.files.local(path);
        if (!file.exists()) {
            // Handle the case where the file does not exist
            Gdx.app.error("LoadLevelData", "File not found");
            return null;
        }

        String jsonData = file.readString();

        Json json = new Json();
        return json.fromJson(LevelData.class, jsonData);
    }




}
