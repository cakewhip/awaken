package com.kqp.awaken.network.screen.navigation;

import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.network.AwakenPacketC2S;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class OpenCraftingC2S extends AwakenPacketC2S {
    public OpenCraftingC2S() {
        super("open_crafting_c2s");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int syncId = data.readInt();
        double mouseX = data.readDouble();
        double mouseY = data.readDouble();

        context.getTaskQueue().execute(() -> {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();

            player.closeCurrentScreen();

            player.currentScreenHandler = new AwakenCraftingScreenHandler(syncId, player.inventory);

            AwakenNetworking.OPEN_CRAFTING_S2C.sendToPlayer(player, (buf) -> {
                buf.writeInt(syncId);
                buf.writeDouble(mouseX);
                buf.writeDouble(mouseY);
            });
        });
    }
}
