package com.kqp.awaken.init;

import com.kqp.awaken.client.screen.AwakenCraftingScreenHandler;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AwakenNetworking {
    // TODO: move C2S IDs to client entry point
    // TODO: label directions on variables (see open/close crafting IDs)
    public static final Identifier SYNC_CRAFTING_SCROLLBAR_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_scrollbar");
    public static final Identifier SYNC_CRAFTING_RESULTS_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_results");
    public static final Identifier SYNC_CRAFTING_RESULT_SLOT_ID = new Identifier(Awaken.MOD_ID, "sync_crafting_result_slot");
    public static final Identifier SYNC_LOOK_UP_SCROLLBAR_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_scrollbar");
    public static final Identifier SYNC_LOOK_UP_RESULTS_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_results");
    public static final Identifier SYNC_LOOK_UP_RESULT_SLOT_ID = new Identifier(Awaken.MOD_ID, "sync_look_up_result_slot");


    public static final Identifier OPEN_CRAFTING_C2S_ID = new Identifier(Awaken.MOD_ID, "open_crafting_c2s");
    public static final Identifier OPEN_CRAFTING_S2C_ID = new Identifier(Awaken.MOD_ID, "open_crafting_s2c");
    public static final Identifier CLOSE_CRAFTING_C2S_ID = new Identifier(Awaken.MOD_ID, "close_crafting_c2s");
    public static final Identifier CLOSE_CRAFTING_S2C_ID = new Identifier(Awaken.MOD_ID, "close_crafting_s2c");

    public static final Identifier SYNC_LEVEL_DATA_S2C_ID = new Identifier(Awaken.MOD_ID, "sync_level_data_s2c");

    public static void init() {
        Awaken.info("Initializing networking");

        // Receives the client's scroll bar position and updates the server-side screenHandler
        ServerSidePacketRegistry.INSTANCE.register(SYNC_CRAFTING_SCROLLBAR_ID, (packetContext, data) -> {
            float scrollPosition = data.readFloat();

            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().currentScreenHandler instanceof AwakenCraftingScreenHandler) {
                    ((AwakenCraftingScreenHandler) packetContext.getPlayer().currentScreenHandler).scrollOutputs(scrollPosition);
                }
            });
        });

        ServerSidePacketRegistry.INSTANCE.register(SYNC_LOOK_UP_SCROLLBAR_ID, (packetContext, data) -> {
            float scrollPosition = data.readFloat();

            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().currentScreenHandler instanceof AwakenCraftingScreenHandler) {
                    ((AwakenCraftingScreenHandler) packetContext.getPlayer().currentScreenHandler).scrollLookUpResults(scrollPosition);
                }
            });
        });

        // Packets for the server to coordinate the navigation between the inventory and the crafting screen
        {
            ServerSidePacketRegistry.INSTANCE.register(OPEN_CRAFTING_C2S_ID, ((packetContext, packetByteBuf) -> {
                int syncId = packetByteBuf.readInt();
                double mouseX = packetByteBuf.readDouble();
                double mouseY = packetByteBuf.readDouble();

                packetContext.getTaskQueue().execute(() -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                    player.closeCurrentScreen();

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(syncId);
                    buf.writeDouble(mouseX);
                    buf.writeDouble(mouseY);

                    player.currentScreenHandler = new AwakenCraftingScreenHandler(syncId, player.inventory);

                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, OPEN_CRAFTING_S2C_ID, buf);

                });
            }));

            ServerSidePacketRegistry.INSTANCE.register(CLOSE_CRAFTING_C2S_ID, ((packetContext, packetByteBuf) -> {
                double mouseX = packetByteBuf.readDouble();
                double mouseY = packetByteBuf.readDouble();

                packetContext.getTaskQueue().execute(() -> {
                    ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                    player.closeHandledScreen();

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeDouble(mouseX);
                    buf.writeDouble(mouseY);

                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, CLOSE_CRAFTING_S2C_ID, buf);
                });
            }));
        }
    }
}
