package com.kqp.awaken.network.screen.navigation;

import com.kqp.awaken.client.screen.AwakenCraftingScreen;
import com.kqp.awaken.init.client.AwakenClient;
import com.kqp.awaken.network.AwakenPacketS2C;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;

public class OpenCraftingS2C extends AwakenPacketS2C {
    public OpenCraftingS2C() {
        super("open_crafting_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int syncId = data.readInt();
        double mouseX = data.readDouble();
        double mouseY = data.readDouble();

        context.getTaskQueue().execute(() -> {
            openCraftingMenu(syncId, mouseX, mouseY);
        });
    }

    /**
     * Finally opens the crafting menu.
     * {@link AwakenClient#triggerOpenCraftingMenu()} needs to be invoked first.
     *
     * @param syncId
     * @param mouseX
     * @param mouseY
     */
    private static void openCraftingMenu(int syncId, double mouseX, double mouseY) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        AwakenCraftingScreenHandler screenHandler = new AwakenCraftingScreenHandler(syncId, player.inventory);

        player.closeScreen();
        player.currentScreenHandler = screenHandler;

        MinecraftClient.getInstance().openScreen(new AwakenCraftingScreen(
                screenHandler,
                player.inventory));

        InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX, mouseY);
    }
}
