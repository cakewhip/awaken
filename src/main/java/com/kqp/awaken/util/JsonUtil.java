package com.kqp.awaken.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kqp.awaken.data.event.Event;
import com.kqp.awaken.data.json.AbstractAdapter;
import com.kqp.awaken.data.trigger.Trigger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Utility class for saving and loading JSON files.
 */
public final class JsonUtil {
    private static final String EVENT_DIR = "/data/awaken/event/";

    public static Event readEvent(String name) {
        InputStreamReader input = new InputStreamReader(JsonUtil.class.getResourceAsStream(EVENT_DIR + name + ".json"));
        return getGson().fromJson(input, Event.class);
    }

    public static void save(File file, Object obj) {
        checkFileExistence(file);

        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = getGson();
            gson.toJson(obj, writer);
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T load(File file, Class<? extends T> clazz, T defaultInst) {
        if (!file.exists()) {
            save(file, defaultInst);
        }

        Gson gson = getGson();
        try {
            return gson.fromJson(new FileReader(file), clazz);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return defaultInst;
    }

    private static void checkFileExistence(File file) {
        try {
            if (file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Trigger.class, new AbstractAdapter<Trigger>("com.kqp.awaken.data.trigger."))
                .registerTypeAdapter(Event.class, new AbstractAdapter<Event>("com.kqp.awaken.data.event."))
                .setPrettyPrinting()
                .create();
    }

    private static final ArrayList<Event> EVENT_LIST_TEMPLATE = new ArrayList<Event>();
}
