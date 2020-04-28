package com.kqp.awaken.network.screen.navigation;

import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.network.AwakenPacketC2S;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class CloseCraftingC2S extends AwakenPacketC2S {
    public CloseCraftingC2S() {
        super("close_crafting_c2s");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        double mouseX = data.readDouble();
        double mouseY = data.readDouble();

        context.getTaskQueue().execute(() -> {
            ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
            player.closeHandledScreen();

            AwakenNetworking.CLOSE_CRAFTING_S2C.sendToPlayer(player, (buf) -> {
                buf.writeDouble(mouseX);
                buf.writeDouble(mouseY);
            });
        });
    }
}
