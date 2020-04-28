package com.kqp.awaken.network.screen;

import com.kqp.awaken.client.screen.AwakenCraftingScreen;
import com.kqp.awaken.client.slot.AwakenCraftingResultSlot;
import com.kqp.awaken.network.AwakenPacketS2C;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class SyncCraftingResultSlotS2C extends AwakenPacketS2C {
    public SyncCraftingResultSlotS2C() {
        super("sync_crafting_result_slot_s2c");
    }

    @Override
    public void accept(PacketContext context, PacketByteBuf data) {
        int slotIndex = data.readInt();
        ItemStack stack = data.readItemStack();
        int currentIndex = data.readInt();

        context.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().setStackInSlot(slotIndex, stack);
                ((AwakenCraftingResultSlot) ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().getSlot(slotIndex)).currentIndex = currentIndex;
            }
        });
    }
}
