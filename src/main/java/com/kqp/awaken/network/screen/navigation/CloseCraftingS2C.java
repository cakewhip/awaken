package com.kqp.awaken.network.screen.navigation;

import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;

public class CloseCraftingS2C extends AwakenPacketS2C {
    public CloseCraftingS2C() {
        super("close_crafting_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        double mouseX = data.readDouble();
        double mouseY = data.readDouble();

        context.getTaskQueue().execute(() -> {
            closeCraftingMenu(mouseX, mouseY);
        });
    }

    @Environment(EnvType.CLIENT)
    private static void closeCraftingMenu(double mouseX, double mouseY) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        player.closeScreen();
        MinecraftClient.getInstance().openScreen(new InventoryScreen(player));

        InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX, mouseY);
    }
}
