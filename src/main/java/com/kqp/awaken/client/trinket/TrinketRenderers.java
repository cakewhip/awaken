package com.kqp.awaken.client.trinket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class TrinketRenderers {
    private static final Map<String, TrinketRenderer> RENDERER_MAP = new HashMap();

    public static void init() {
        RENDERER_MAP.put("wings", new WingsRenderer());
    }

    public static TrinketRenderer getTrinketRenderer(String id) {
        if (!RENDERER_MAP.containsKey(id)) {
            throw new RuntimeException("No renderer with id " + id + " found!");
        }

        return RENDERER_MAP.get(id);
    }
}
