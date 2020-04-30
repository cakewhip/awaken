package com.kqp.awaken.init;

import com.kqp.awaken.network.AwakenPacket;
import com.kqp.awaken.network.entity.AbominationDespawningS2C;
import com.kqp.awaken.network.entity.AbominationSmashAttackS2C;
import com.kqp.awaken.network.entity.AbominationSpawnSpawnlingsS2C;
import com.kqp.awaken.network.recipe.SyncRecipesS2C;
import com.kqp.awaken.network.screen.SyncCraftingResultSlotS2C;
import com.kqp.awaken.network.screen.SyncCraftingResultsS2C;
import com.kqp.awaken.network.screen.SyncCraftingScrollbarC2S;
import com.kqp.awaken.network.screen.SyncLookUpResultSlotS2C;
import com.kqp.awaken.network.screen.SyncLookUpResultsS2C;
import com.kqp.awaken.network.screen.SyncLookUpScrollbarC2S;
import com.kqp.awaken.network.screen.navigation.CloseCraftingC2S;
import com.kqp.awaken.network.screen.navigation.CloseCraftingS2C;
import com.kqp.awaken.network.screen.navigation.OpenCraftingC2S;
import com.kqp.awaken.network.screen.navigation.OpenCraftingS2C;
import com.kqp.awaken.network.world.SyncLevelDataS2C;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class AwakenNetworking {
    // Server to Client
    public static final SyncCraftingResultsS2C SYNC_CRAFTING_RESULTS_S2C = new SyncCraftingResultsS2C();
    public static final SyncCraftingResultSlotS2C SYNC_CRAFTING_RESULT_SLOT_S2C = new SyncCraftingResultSlotS2C();
    public static final SyncLookUpResultsS2C SYNC_LOOK_UP_RESULTS_S2C = new SyncLookUpResultsS2C();
    public static final SyncLookUpResultSlotS2C SYNC_LOOK_UP_RESULT_SLOT_S2C = new SyncLookUpResultSlotS2C();

    public static final OpenCraftingS2C OPEN_CRAFTING_S2C = new OpenCraftingS2C();
    public static final CloseCraftingS2C CLOSE_CRAFTING_S2C = new CloseCraftingS2C();

    public static final SyncLevelDataS2C SYNC_LEVEL_DATA_S2C = new SyncLevelDataS2C();

    public static final SyncRecipesS2C SYNC_RECIPES_S2C = new SyncRecipesS2C();

    public static final AbominationSmashAttackS2C ABOMINATION_SMASH_ATTACK_S2C = new AbominationSmashAttackS2C();
    public static final AbominationSpawnSpawnlingsS2C ABOMINATION_SPAWN_SPAWNLINGS_S2C = new AbominationSpawnSpawnlingsS2C();
    public static final AbominationDespawningS2C ABOMINATION_DESPAWNING_S2C = new AbominationDespawningS2C();


    // Client to Server
    public static final SyncCraftingScrollbarC2S SYNC_CRAFTING_SCROLLBAR_C2S = new SyncCraftingScrollbarC2S();
    public static final SyncLookUpScrollbarC2S SYNC_LOOK_UP_SCROLLBAR_C2S = new SyncLookUpScrollbarC2S();

    public static final OpenCraftingC2S OPEN_CRAFTING_C2S = new OpenCraftingC2S();
    public static final CloseCraftingC2S CLOSE_CRAFTING_C2S = new CloseCraftingC2S();

    public static void init() {
        Awaken.info("Initializing networking");

        register(SYNC_CRAFTING_SCROLLBAR_C2S);
        register(SYNC_LOOK_UP_SCROLLBAR_C2S);

        register(OPEN_CRAFTING_C2S);
        register(CLOSE_CRAFTING_C2S);
    }

    private static void register(AwakenPacket packet) {
        ServerSidePacketRegistry.INSTANCE.register(packet.id, packet::accept);
    }
}
