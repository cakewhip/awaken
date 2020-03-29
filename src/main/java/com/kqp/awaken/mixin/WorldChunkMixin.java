package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.data.AwakenDataBlockEntity;
import com.kqp.awaken.world.AwakenOreGen;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kqp.awaken.data.AwakenDataBlockEntity.DATA_BLOCK_POS;

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

        AwakenDataBlockEntity dataBe = getAwakenDataBlockEntity(chunk);

        if (Awaken.worldProperties.isWorldAwakened()) {
            if (!dataBe.genNewOres) {
                dataBe.genNewOres = true;
                AwakenOreGen.generate(chunk, 32, 4, Awaken.Groups.MOONSTONE.ORE);
                AwakenOreGen.generate(chunk, 32, 4, Awaken.Groups.SUNSTONE.ORE);
            }
        }
    }

    private AwakenDataBlockEntity getAwakenDataBlockEntity(WorldChunk chunk) {
        BlockEntity be = chunk.getBlockEntity(DATA_BLOCK_POS);

        if (be == null || !(be instanceof AwakenDataBlockEntity)) {
            be = new AwakenDataBlockEntity();

            chunk.setBlockState(DATA_BLOCK_POS, Blocks.BEDROCK.getDefaultState(), false);
            chunk.setBlockEntity(DATA_BLOCK_POS, be);
        }

        return (AwakenDataBlockEntity) be;
    }
}
