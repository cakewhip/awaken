package com.kqp.awaken.mixin.world;

import com.kqp.awaken.mixin.world.gen.chunk.ChunkGeneratorAccessor;
import com.kqp.awaken.world.spawning.ConditionalSpawnEntry;
import com.kqp.awaken.world.spawning.SpawnCondition;
import net.minecraft.entity.EntityCategory;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @Inject(method = "pickRandomSpawnEntry", at = @At("RETURN"), cancellable = true)
    private static void applyConditionalSpawns(StructureAccessor structureAccessor, ChunkGenerator<?> chunkGenerator,
                                               EntityCategory category, Random random, BlockPos pos,
                                               CallbackInfoReturnable<Biome.SpawnEntry> callbackInfo) {
        World world = (World) ((ChunkGeneratorAccessor) chunkGenerator).getWorld();
        List<Biome.SpawnEntry> originalList = chunkGenerator.getEntitySpawnList(structureAccessor, category, pos);

        List<Biome.SpawnEntry> filteredList = originalList.stream()
                .filter(spawnEntry -> {
                    if (spawnEntry instanceof ConditionalSpawnEntry) {
                        for (SpawnCondition condition : ((ConditionalSpawnEntry) spawnEntry).getSpawnConditions()) {
                            if (!condition.test(world, pos)) {
                                return true;
                            }
                        }
                    }

                    return false;
                })
                .collect(Collectors.toList());

        callbackInfo.setReturnValue(
                filteredList.isEmpty() ? null : WeightedPicker.getRandom(random, filteredList)
        );
    }
}
