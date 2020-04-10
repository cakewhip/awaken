package com.kqp.awaken.init;

import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;

public class AwakenCallbacks {
    public static void init() {
        Awaken.info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                if (Awaken.worldProperties != null) {
                    Awaken.worldProperties.tick();
                }
            }
        });

        LootTableLoadingCallback.EVENT.register(AwakenLootTable::onLootTableLoading);
    }
}
