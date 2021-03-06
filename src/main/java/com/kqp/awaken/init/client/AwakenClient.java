package com.kqp.awaken.init.client;

import com.kqp.awaken.client.entity.AbominationRenderer;
import com.kqp.awaken.client.entity.DireWolfRenderer;
import com.kqp.awaken.client.entity.GenericAgentRenderer;
import com.kqp.awaken.client.entity.GenericBipedRenderer;
import com.kqp.awaken.client.entity.RadianceLightRenderer;
import com.kqp.awaken.client.entity.RadianceRenderer;
import com.kqp.awaken.client.entity.RaptorChickenRenderer;
import com.kqp.awaken.client.entity.SpiderSacRenderer;
import com.kqp.awaken.client.entity.VoidGhostRenderer;
import com.kqp.awaken.client.trinket.TrinketRenderers;
import com.kqp.awaken.init.AwakenEntities;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.network.AwakenPacket;
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
        TrinketRenderers.init();
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

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.SPIDER_SAC, (dispatcher, context) ->
                new SpiderSacRenderer(dispatcher)
        );

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.VOID_GHOST, (dispatcher, context) ->
                new VoidGhostRenderer(dispatcher)
        );

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.ABOMINATION, (dispatcher, context) ->
                new AbominationRenderer(dispatcher)
        );

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.RADIANCE, (dispatcher, context) ->
                new RadianceRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.RADIANCE_LIGHT, (dispatcher, context) ->
                new RadianceLightRenderer(dispatcher));

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.RENEGADE, (dispatcher, context) ->
                new GenericBipedRenderer(dispatcher, "renegade"));

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.VAGABOND, (dispatcher, context) ->
                new GenericBipedRenderer(dispatcher, "vagabond"));

        EntityRendererRegistry.INSTANCE.register(AwakenEntities.ENDER_AGENT, (dispatcher, context) ->
                new GenericAgentRenderer(dispatcher, "ender_agent"));
    }

    /**
     * Registers packet listeners.
     */
    public static void initNetworking() {
        // Sync level data from server to clients
        register(AwakenNetworking.SYNC_LEVEL_DATA_S2C);


        register(AwakenNetworking.ABOMINATION_SMASH_ATTACK_S2C);
        register(AwakenNetworking.ABOMINATION_SPAWN_SPAWNLINGS_S2C);
        register(AwakenNetworking.BOSS_DESPAWNING_S2C);

        register(AwakenNetworking.SPAWN_ENTITY_PACKET_S2C);
    }

    private static void register(AwakenPacket packet) {
        ClientSidePacketRegistry.INSTANCE.register(packet.id, packet::accept);
    }
}
