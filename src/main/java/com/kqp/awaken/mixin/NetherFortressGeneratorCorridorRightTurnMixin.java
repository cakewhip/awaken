package com.kqp.awaken.mixin;

import com.kqp.awaken.init.AwakenBlocks;
import net.minecraft.structure.NetherFortressGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(NetherFortressGenerator.CorridorRightTurn.class)
public abstract class NetherFortressGeneratorCorridorRightTurnMixin extends StructurePiece {
    @Shadow
    private boolean containsChest;

    protected NetherFortressGeneratorCorridorRightTurnMixin(StructurePieceType type, int length) {
        super(type, length);
    }

    @Inject(method = "generate", at = @At("RETURN"))
    private void generateEnderianHellForge(IWorld world, StructureAccessor structureAccessor, ChunkGenerator<?> chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable callbackInfo) {
        if (!this.containsChest && random.nextFloat() < (15F / 100F)) {
            BlockPos forgeBlockPos = new BlockPos(this.applyXTransform(3, 3), this.applyYTransform(2), applyZTransform(3, 3));
            world.setBlockState(forgeBlockPos, AwakenBlocks.ENDERIAN_HELL_FORGE.getDefaultState(), 2);
        }
    }
}
