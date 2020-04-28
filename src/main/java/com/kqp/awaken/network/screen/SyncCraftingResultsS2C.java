package com.kqp.awaken.network.screen;

import com.kqp.awaken.client.screen.AwakenCraftingScreen;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class SyncCraftingResultsS2C extends AwakenPacketS2C {
    public SyncCraftingResultsS2C() {
        super("sync_crafting_results_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        context.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).syncCraftingResultScrollbar();
            }
        });
    }
}
