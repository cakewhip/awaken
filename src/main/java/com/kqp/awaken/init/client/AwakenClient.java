package com.kqp.awaken.init.client;

import com.kqp.awaken.client.entity.AbominationRenderer;
import com.kqp.awaken.client.entity.DireWolfRenderer;
import com.kqp.awaken.client.entity.RaptorChickenRenderer;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.network.AwakenPacket;
import com.kqp.awaken.util.MouseUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

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

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.ABOMINATION, (dispatcher, context) ->
                new AbominationRenderer(dispatcher)
        );
    }

    /**
     * Registers packet listeners.
     */
    public static void initNetworking() {
        // Server request to client to get the scroll bar position
        register(AwakenNetworking.SYNC_CRAFTING_RESULTS_S2C);
        // Server sends what ItemStack is in what slot of the Awaken crafting GUI
        register(AwakenNetworking.SYNC_CRAFTING_RESULT_SLOT_S2C);
        register(AwakenNetworking.SYNC_LOOK_UP_RESULTS_S2C);
        register(AwakenNetworking.SYNC_LOOK_UP_RESULT_SLOT_S2C);

        // Packets for the client to coordinate the navigation between the inventory and the crafting screen
        register(AwakenNetworking.OPEN_CRAFTING_S2C);
        register(AwakenNetworking.CLOSE_CRAFTING_S2C);

        // Sync level data from server to clients
        register(AwakenNetworking.SYNC_LEVEL_DATA_S2C);

        // Sync recipes from server to clients
        register(AwakenNetworking.SYNC_RECIPES_S2C);


        register(AwakenNetworking.SPAWN_ABOMINATION_PARTICLE_S2C);
    }

    private static void register(AwakenPacket packet) {
        ClientSidePacketRegistry.INSTANCE.register(packet.id, packet::accept);
    }

    /**
     * Begins the process of opening the crafting menu from the inventory screen.
     * It is a lot more complicated than initially thought due to how screen handlers and screens need to be closed.
     */
    public static void triggerOpenCraftingMenu() {
        AwakenNetworking.OPEN_CRAFTING_C2S.sendToServer((buf) -> {
            buf.writeInt(RANDOM.nextInt());
            buf.writeDouble(MouseUtil.getMouseX());
            buf.writeDouble(MouseUtil.getMouseY());
        });
    }
}
