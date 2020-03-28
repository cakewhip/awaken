package com.kqp.terminus.client;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.TerminusCraftingContainer;
import com.kqp.terminus.client.container.TerminusResultSlot;
import com.kqp.terminus.client.screen.TerminusCraftingScreen;
import com.kqp.terminus.util.MouseUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.PacketByteBuf;

import java.util.Random;

/**
 * Entry-point for clients.
 */
public class TerminusClient implements ClientModInitializer {
    /**
     * Used to generate syncIds.
     */
    private static final Random RANDOM = new Random();

    @Override
    public void onInitializeClient() {
        initNetworking();
    }

    /**
     * Registers packet listeners.
     */
    public static void initNetworking() {
        // Server request to client to get the scroll bar position
        ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.SYNC_RESULTS_ID, (packetContext, data) -> packetContext.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof TerminusCraftingScreen) {
                ((TerminusCraftingScreen) MinecraftClient.getInstance().currentScreen).syncScrollbar();
            }
        }));

        // Server sends what ItemStack is in what slot of the Terminus crafting GUI
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

        // Packets for the client to coordinate the navigation between the inventory and the crafting screen
        {
            ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.OPEN_CRAFTING_S2C_ID, ((packetContext, packetByteBuf) -> {
                int syncId = packetByteBuf.readInt();
                double mouseX = packetByteBuf.readDouble();
                double mouseY = packetByteBuf.readDouble();

                packetContext.getTaskQueue().execute(() -> {
                    openCraftingMenu(syncId, mouseX, mouseY);
                });
            }));

            ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.CLOSE_CRAFTING_S2C_ID, ((packetContext, packetByteBuf) -> {
                double mouseX = packetByteBuf.readDouble();
                double mouseY = packetByteBuf.readDouble();

                packetContext.getTaskQueue().execute(() -> {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    player.closeScreen();
                    MinecraftClient.getInstance().openScreen(new InventoryScreen(player));

                    InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX, mouseY);
                });
            }));
        }
    }

    /**
     * Begins the process of opening the crafting menu from the inventory screen.
     * It is a lot more complicated than initially thought due to how containers and screens need to be closed.
     */
    public static void triggerOpenCraftingMenu() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(RANDOM.nextInt());
        buf.writeDouble(MouseUtil.getMouseX());
        buf.writeDouble(MouseUtil.getMouseY());

        ClientSidePacketRegistry.INSTANCE.sendToServer(Terminus.TNetworking.OPEN_CRAFTING_C2S_ID, buf);
    }

    /**
     * Finally opens the crafting menu.
     * {@link #triggerOpenCraftingMenu()} needs to be invoked first.
     *
     * @param syncId
     * @param mouseX
     * @param mouseY
     */
    private static void openCraftingMenu(int syncId, double mouseX, double mouseY) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        TerminusCraftingContainer container = new TerminusCraftingContainer(syncId, player.inventory);

        player.closeScreen();
        player.container = container;
        MinecraftClient.getInstance().openScreen(new TerminusCraftingScreen(
                container,
                player.inventory));

        InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX, mouseY);
    }
}
