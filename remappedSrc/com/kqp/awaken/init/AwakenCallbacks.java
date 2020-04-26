package com.kqp.awaken.init;

import com.kqp.awaken.data.AwakenLevelData;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.world.dimension.DimensionType;

public class AwakenCallbacks {
    public static void init() {
        Awaken.info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                if (world.getDimension().getType() == DimensionType.OVERWORLD) {
                    AwakenLevelData.getFor(world).tick(world.getServer());
                }
            }
        });

        LootTableLoadingCallback.EVENT.register(AwakenLootTable::onLootTableLoading);
    }
}
