package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.kqp.awaken.data.AwakenTemporalChunkData;
import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.world.AwakenOreGen;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to:
 * Save/load Awaken chunk data.
 * Gen new stuff post-awakening.
 */
@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements Chunk {
    @Inject(at = @At("HEAD"), method = "loadToWorld")
    public void loadToWorld(CallbackInfo callbackInfo) {
        WorldChunk chunk = (WorldChunk) (Object) this;

        AwakenTemporalChunkData.ChunkData awakenData = AwakenTemporalChunkData.CHUNK_DATA_MAP.get(chunk.getPos());
        if (awakenData != null) {
            AwakenLevelData awakenLevelData = ((AwakenLevelDataContainer) chunk.getWorld().getLevelProperties()).getAwakenLevelData();

            if (awakenLevelData.isWorldAwakened()) {
                if (!awakenData.genNewOres) {
                    awakenData.genNewOres = true;

                    AwakenOreGen.generate(chunk, 64, 12, 12, AwakenBlocks.SALVIUM.ORE);
                    AwakenOreGen.generate(chunk, 64, 12, 12, AwakenBlocks.VALERIUM.ORE);
                }
            }
        }
    }
}
