package com.kqp.awaken.init;

import com.kqp.awaken.world.data.AwakenLevelData;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;

public class AwakenCallbacks {
    public static void init() {
        Awaken.info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                if (world.getDimension().isOverworld()) {
                    AwakenLevelData.getFor(world.getServer()).tick(world.getServer());
                }
            }
        });

        LootTableLoadingCallback.EVENT.register(AwakenLootTable::onLootTableLoading);
    }
}
