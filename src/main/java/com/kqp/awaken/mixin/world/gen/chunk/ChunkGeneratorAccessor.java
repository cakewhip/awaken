package com.kqp.awaken.mixin.world.gen.chunk;

import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Mutable
    @Accessor
    IWorld getWorld();
}
