package com.kqp.terminus.client;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.TerminusCraftingContainer;
import com.kqp.terminus.client.container.TerminusResultSlot;
import com.kqp.terminus.client.screen.TerminusCraftingScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class TerminusClient implements ClientModInitializer {
    private static final Random RANDOM = new Random();

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.SYNC_RESULTS_ID, (packetContext, data) -> packetContext.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof TerminusCraftingScreen) {
                ((TerminusCraftingScreen) MinecraftClient.getInstance().currentScreen).syncScrollbar();
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.SYNC_RESULT_SLOT_ID, (packetContext, data) -> {
            int slotIndex = data.readInt();
            ItemStack stack = data.readItemStack();
            int currentIndex = data.readInt();

            packetContext.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().currentScreen instanceof TerminusCraftingScreen) {
                    ((TerminusCraftingScreen) MinecraftClient.getInstance().currentScreen).getContainer().setStackInSlot(slotIndex, stack);
                    ((TerminusResultSlot) ((TerminusCraftingScreen) MinecraftClient.getInstance().currentScreen).getContainer().getSlot(slotIndex)).currentIndex = currentIndex;
                }
            });
        });
    }

    public static void openCraftingMenu() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        int syncId = RANDOM.nextInt();
        TerminusCraftingContainer container = new TerminusCraftingContainer(syncId, player.inventory);
        player.container = container;

        MinecraftClient.getInstance().openScreen(new TerminusCraftingScreen(
                container,
                player.inventory));

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(syncId);

        ClientSidePacketRegistry.INSTANCE.sendToServer(Terminus.TNetworking.OPEN_CRAFTING_ID, buf);
    }
}
