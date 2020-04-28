package com.kqp.awaken.network.screen;

import com.kqp.awaken.network.AwakenPacketC2S;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;

public class SyncLookUpScrollbarC2S extends AwakenPacketC2S {
    public SyncLookUpScrollbarC2S() {
        super("sync_look_up_scrollbar_c2s");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        float scrollPosition = data.readFloat();

        context.getTaskQueue().execute(() -> {
            if (context.getPlayer().currentScreenHandler instanceof AwakenCraftingScreenHandler) {
                ((AwakenCraftingScreenHandler) context.getPlayer().currentScreenHandler).scrollLookUpResults(scrollPosition);
            }
        });
    }
}
