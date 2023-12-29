package com.art.prototype.editor;

import com.art.prototype.StaticBody;
import com.art.prototype.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import lombok.Data;

@Data
public class LevelData implements Json.Serializable {
    private World worldRef;
    private Array<PlatformData> platformDataArray = new Array<>();

    @Override
    public void write(Json json) {
        json.writeArrayStart("platforms");
        for (StaticBody staticBody : worldRef.getStaticBodies()) {
            json.writeObjectStart();
            json.writeValue("pos", staticBody.getPos(), Vector2.class);
            json.writeValue("size", staticBody.getSize());
            json.writeValue("id", staticBody.getUuid());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }



    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue jsonValue = jsonData.get("platforms");
        for (JsonValue value : jsonValue) {
            final PlatformData platformData = json.readValue(PlatformData.class, value);
            platformDataArray.add(platformData);
        }
    }
}
