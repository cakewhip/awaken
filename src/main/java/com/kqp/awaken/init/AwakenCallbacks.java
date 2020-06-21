package com.kqp.awaken.init;

import com.kqp.awaken.world.data.AwakenLevelData;
import nerdhub.cardinal.components.api.event.LevelComponentCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.world.dimension.DimensionType;

public class AwakenCallbacks {
    public static void init() {
        Awaken.info("Initializing callbacks");

        WorldTickCallback.EVENT.register((world) -> {
            if (!world.isClient) {
                if (world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
                    AwakenLevelData.getFor(world.getServer()).tick(world.getServer());
                }
            }
        });

        LootTableLoadingCallback.EVENT.register(AwakenLootTable::onLootTableLoading);

        LevelComponentCallback.EVENT.register((levelProperties, comps) -> {
            comps.put(AwakenLevelData.LEVEL_DATA, new AwakenLevelData());
        });
    }
}
