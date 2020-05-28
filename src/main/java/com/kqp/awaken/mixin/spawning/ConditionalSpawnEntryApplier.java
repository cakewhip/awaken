package com.kqp.awaken.mixin.spawning;

import com.kqp.awaken.world.spawning.ConditionalSpawnEntry;
import com.kqp.awaken.world.spawning.SpawnCondition;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
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

/**
 * Used to apply conditional spawn entries.
 */
@Mixin(SpawnHelper.class)
public class ConditionalSpawnEntryApplier {
    @Inject(method = "pickRandomSpawnEntry", at = @At("RETURN"), cancellable = true)
    private static void applyConditionalSpawns(ServerWorld world,
                                               StructureAccessor structureAccessor,
                                               ChunkGenerator chunkGenerator,
                                               SpawnGroup spawnGroup,
                                               Random random,
                                               BlockPos pos,
                                               CallbackInfoReturnable<Biome.SpawnEntry> callbackInfo) {
        List<Biome.SpawnEntry> originalList = chunkGenerator.getEntitySpawnList(world.getBiome(pos), structureAccessor, spawnGroup, pos);

        List<Biome.SpawnEntry> filteredList = originalList.stream()
                .filter(spawnEntry -> {
                    if (spawnEntry instanceof ConditionalSpawnEntry) {
                        for (SpawnCondition condition : ((ConditionalSpawnEntry) spawnEntry).getSpawnConditions()) {
                            if (!condition.test(world, pos)) {
                                return false;
                            }
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());

        callbackInfo.setReturnValue(
                filteredList.isEmpty() ? null : WeightedPicker.getRandom(random, filteredList)
        );
    }
}
