package com.kqp.awaken.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Consumer;

public class JsonUtil {
    public static void optionalElement(JsonObject parent, String key, Consumer<JsonElement> callback) {
        if (parent.has(key)) {
            callback.accept(parent.get(key));
        }
    }
}
