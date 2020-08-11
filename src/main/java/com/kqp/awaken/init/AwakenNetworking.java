package com.kqp.awaken.init;

import com.kqp.awaken.network.AwakenPacket;
import com.kqp.awaken.network.entity.AbominationSmashAttackS2C;
import com.kqp.awaken.network.entity.AbominationSpawnSpawnlingsS2C;
import com.kqp.awaken.network.entity.BossDespawningS2C;
import com.kqp.awaken.network.entity.SpawnEntityPacketS2C;
import com.kqp.awaken.network.entity.player.PlayerInputSyncC2S;
import com.kqp.awaken.network.world.SyncLevelDataS2C;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class AwakenNetworking {
    // Server to Client
    public static final SyncLevelDataS2C SYNC_LEVEL_DATA_S2C = new SyncLevelDataS2C();

    public static final AbominationSmashAttackS2C ABOMINATION_SMASH_ATTACK_S2C = new AbominationSmashAttackS2C();
    public static final AbominationSpawnSpawnlingsS2C ABOMINATION_SPAWN_SPAWNLINGS_S2C = new AbominationSpawnSpawnlingsS2C();
    public static final BossDespawningS2C BOSS_DESPAWNING_S2C = new BossDespawningS2C();

    public static final SpawnEntityPacketS2C SPAWN_ENTITY_PACKET_S2C = new SpawnEntityPacketS2C();


    // Client to Server

    public static final PlayerInputSyncC2S PLAYER_INPUT_SYNC_C2S = new PlayerInputSyncC2S();

    public static void init() {
        Awaken.info("Initializing networking");

        register(PLAYER_INPUT_SYNC_C2S);
    }

    private static void register(AwakenPacket packet) {
        ServerSidePacketRegistry.INSTANCE.register(packet.id, packet::accept);
    }
}
