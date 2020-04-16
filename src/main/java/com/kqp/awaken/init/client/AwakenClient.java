package com.kqp.awaken.init.client;

import com.kqp.awaken.client.entity.DireWolfRenderer;
import com.kqp.awaken.client.entity.RaptorChickenRenderer;
import com.kqp.awaken.client.screen.AwakenCraftingScreen;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import com.kqp.awaken.client.slot.AwakenCraftingResultSlot;
import com.kqp.awaken.client.slot.AwakenLookUpResultSlot;
import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.util.MouseUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.Random;

/**
 * Entry-point for clients.
 */
public class AwakenClient implements ClientModInitializer {
    /**
     * Used to generate syncIds.
     */
    private static final Random RANDOM = new Random();

    @Override
    public void onInitializeClient() {
        initEntityRenderers();
        initNetworking();
    }

    public static void initEntityRenderers() {
        EntityRendererRegistry.INSTANCE.register(AwakenEntities.RAPTOR_CHICKEN, (dispatcher, context) ->
                new RaptorChickenRenderer(dispatcher)
        );

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.DIRE_WOLF, (dispatcher, context) ->
                new DireWolfRenderer(dispatcher)
        );
    }

    /**
     * Registers packet listeners.
     */
    public static void initNetworking() {
        // Server request to client to get the scroll bar position
        ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.SYNC_CRAFTING_RESULTS_ID, (packetContext, data) -> packetContext.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).syncCraftingResultScrollbar();
            }
        }));

        // Server sends what ItemStack is in what slot of the Awaken crafting GUI
        ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.SYNC_CRAFTING_RESULT_SLOT_ID, (packetContext, data) -> {
            int slotIndex = data.readInt();
            ItemStack stack = data.readItemStack();
            int currentIndex = data.readInt();

            packetContext.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                    ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().setStackInSlot(slotIndex, stack);
                    ((AwakenCraftingResultSlot) ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().getSlot(slotIndex)).currentIndex = currentIndex;
                }
            });
        });

        ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.SYNC_LOOK_UP_RESULTS_ID, (packetContext, data) -> packetContext.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).syncLookUpResultScrollbar();
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.SYNC_LOOK_UP_RESULT_SLOT_ID, (packetContext, data) -> {
            int slotIndex = data.readInt();
            ItemStack stack = data.readItemStack();
            int currentIndex = data.readInt();

            packetContext.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().currentScreen instanceof AwakenCraftingScreen) {
                    ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().setStackInSlot(slotIndex, stack);
                    ((AwakenLookUpResultSlot) ((AwakenCraftingScreen) MinecraftClient.getInstance().currentScreen).getScreenHandler().getSlot(slotIndex)).currentIndex = currentIndex;
                }
            });
        });

        // Packets for the client to coordinate the navigation between the inventory and the crafting screen
        {
            ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.OPEN_CRAFTING_S2C_ID, ((packetContext, packetByteBuf) -> {
                int syncId = packetByteBuf.readInt();
                double mouseX = packetByteBuf.readDouble();
                double mouseY = packetByteBuf.readDouble();

                packetContext.getTaskQueue().execute(() -> {
                    openCraftingMenu(syncId, mouseX, mouseY);
                });
            }));

            ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.CLOSE_CRAFTING_S2C_ID, ((packetContext, packetByteBuf) -> {
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

        // Sync level data from server to clients
        ClientSidePacketRegistry.INSTANCE.register(AwakenNetworking.SYNC_LEVEL_DATA_S2C_ID, ((packetContext, data) -> {
            CompoundTag awakenLevelDataTag = data.readCompoundTag();

            packetContext.getTaskQueue().execute(() -> {
                ((AwakenLevelDataContainer) MinecraftClient.getInstance().world.getLevelProperties()).setAwakenServerLevelData(new AwakenLevelData(awakenLevelDataTag));
            });
        }));
    }

    /**
     * Begins the process of opening the crafting menu from the inventory screen.
     * It is a lot more complicated than initially thought due to how screen handlers and screens need to be closed.
     */
    public static void triggerOpenCraftingMenu() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(RANDOM.nextInt());
        buf.writeDouble(MouseUtil.getMouseX());
        buf.writeDouble(MouseUtil.getMouseY());

        ClientSidePacketRegistry.INSTANCE.sendToServer(AwakenNetworking.OPEN_CRAFTING_C2S_ID, buf);
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
        AwakenCraftingScreenHandler screenHandler = new AwakenCraftingScreenHandler(syncId, player.inventory);

        player.closeScreen();
        player.currentScreenHandler = screenHandler;

        MinecraftClient.getInstance().openScreen(new AwakenCraftingScreen(
                screenHandler,
                player.inventory));

        InputUtil.setCursorParameters(MinecraftClient.getInstance().getWindow().getHandle(), 212993, mouseX, mouseY);
    }
}
