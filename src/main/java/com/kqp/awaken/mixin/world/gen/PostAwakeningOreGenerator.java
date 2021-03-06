package com.kqp.awaken.mixin.world.gen;

import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.world.AwakenOreGen;
import com.kqp.awaken.world.data.AwakenLevelData;
import com.kqp.awaken.world.data.AwakenTemporalChunkData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to generate post-awakening ores.
 */
@Mixin(WorldChunk.class)
public abstract class PostAwakeningOreGenerator implements Chunk {
    @Inject(method = "loadToWorld", at = @At("HEAD"))
    public void loadToWorld(CallbackInfo callbackInfo) {
        WorldChunk chunk = (WorldChunk) (Object) this;

        AwakenTemporalChunkData.ChunkData awakenData = AwakenTemporalChunkData.CHUNK_DATA_MAP.get(chunk.getPos());
        if (awakenData != null) {
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(chunk.getWorld().getServer());

            if (awakenLevelData.isWorldAwakened()) {
                if (!awakenData.genNewOres) {
                    awakenData.genNewOres = true;

                    AwakenOreGen.generate(chunk, 64, 12, 12, AwakenBlocks.SALVIUM_ORE);
                    AwakenOreGen.generate(chunk, 64, 12, 12, AwakenBlocks.VALERIUM_ORE);
                }
            }
        }
    }
}
