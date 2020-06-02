package com.kqp.awaken.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataUtil {
    private static final Gson GSON = new GsonBuilder().create();

    public static JsonObject getJsonObject(StaticDataItem staticDataItem) throws IOException {
        InputStream inputStream = staticDataItem.createInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);

        return GSON.fromJson(reader, JsonObject.class);
    }

    public static Identifier getStrippedIdentifier(Identifier id) {
        String namespace = id.getNamespace();
        String name = id.getPath();

        name = name.replace(".json", "");

        int slashIndex = name.indexOf('/');

        while (slashIndex != -1) {
            name = name.substring(slashIndex + 1);
            slashIndex = name.indexOf('/');
        }

        return new Identifier(namespace, name);
    }
}
