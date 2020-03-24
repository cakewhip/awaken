package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.data.TerminusDataBlockEntity;
import com.kqp.terminus.world.TerminusOreGen;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kqp.terminus.data.TerminusDataBlockEntity.DATA_BLOCK_POS;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin implements Chunk {

    @Inject(at = @At("HEAD"), method = "loadToWorld")
    public void loadToWorld(CallbackInfo callbackInfo) {
        WorldChunk chunk = (WorldChunk) (Object) this;

        TerminusDataBlockEntity dataBe = getTerminusDataBlockEntity(chunk);

        if (Terminus.worldProperties.isWorldAwakened()) {
            if (!dataBe.genNewOres) {
                dataBe.genNewOres = true;
                TerminusOreGen.generate(chunk, 32, 8, Terminus.OreGroups.MOONSTONE.ORE);
                TerminusOreGen.generate(chunk, 32, 8, Terminus.OreGroups.SUNSTONE.ORE);
            }
        }
    }

    private TerminusDataBlockEntity getTerminusDataBlockEntity(WorldChunk chunk) {
        BlockEntity be = chunk.getBlockEntity(DATA_BLOCK_POS);

        if (be == null || !(be instanceof TerminusDataBlockEntity)) {
            be = new TerminusDataBlockEntity();

            chunk.setBlockState(DATA_BLOCK_POS, Blocks.BEDROCK.getDefaultState(), false);
            chunk.setBlockEntity(DATA_BLOCK_POS, be);
        }

        return (TerminusDataBlockEntity) be;
    }
}
