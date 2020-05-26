package com.kqp.awaken.world.gen.feature;

import com.google.common.collect.Lists;
import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.util.GenUtil;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * TODO: figure out how structure features work so I can add these god dang mob spawns
 */
public class VoidTowerFeature extends Feature<DefaultFeatureConfig> {
    private static final List<Biome.SpawnEntry> MONSTER_SPAWNS;

    public VoidTowerFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public List<Biome.SpawnEntry> getMonsterSpawns() {
        return MONSTER_SPAWNS;
    }

    @Override
    public boolean generate(IWorld world, StructureAccessor accessor, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        pos = getValidBlockPos(world, pos);

        if (pos != null) {
            // Keep bumping up index i so we generate flooring on flat surface
            boolean validCheck2 = false;

            for (int i = 0; i < 16; i++) {
                boolean found = true;

                for (BlockPos offset : GenUtil.getDiskOffsets(6)) {
                    if (world.getBlockState(pos.add(offset).add(0, i, 0)).getBlock() != Blocks.AIR) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    validCheck2 = true;
                    pos = pos.add(0, i, 0);
                    break;
                }
            }

            if (validCheck2) {
                // All clear check to generate walling below the floor
                // Keeps going until the tower is completely sealed
                boolean allClear = false;
                int clearIndex = -1;

                while (!allClear) {
                    allClear = true;

                    for (BlockPos offset : GenUtil.getHollowDiskOffsets(6)) {
                        BlockPos current = pos.add(offset).add(0, clearIndex, 0);

                        if (world.getBlockState(current).getBlock() == Blocks.AIR) {
                            allClear = false;
                            world.setBlockState(current, randomBricks(random), 3);
                        }
                    }

                    clearIndex--;
                }

                // Generate flooring
                for (BlockPos offset : GenUtil.getDiskOffsets(6)) {
                    world.setBlockState(pos.add(offset), randomBricks(random), 3);
                }

                // Generate walls
                for (int i = 0; i < 32; i++) {
                    for (BlockPos offset : GenUtil.getHollowDiskOffsets(6)) {
                        world.setBlockState(pos.add(offset).add(0, i, 0), randomBricks(random), 3);
                    }
                }

                return true;
            }
        }

        return false;
    }

    private BlockPos getValidBlockPos(IWorld world, BlockPos pos) {
        BlockPos currentValid = pos;
        int spaceAvailable = 0;

        for (int i = 31; i < 128; i++) {
            BlockState currentBlock = world.getBlockState(pos.add(0, i, 0));

            if (currentBlock == Blocks.AIR.getDefaultState() && spaceAvailable >= 0) {
                spaceAvailable++;

                if (spaceAvailable > 32) {
                    return currentValid;
                }
            } else if (currentBlock == AwakenBlocks.ANCIENT_STONE.getDefaultState()) {
                spaceAvailable = 0;
                currentValid = pos.add(0, i, 0);
            } else {
                spaceAvailable = -1;
                currentValid = null;
            }
        }

        return null;
    }

    public static BlockState randomBricks(Random random) {
        float roll = random.nextFloat();

        if (roll < 0.1F) {
            return AwakenBlocks.CORRUPTED_NULL_STONE_BRICKS.getDefaultState();
        } else if (roll < 0.25F) {
            return AwakenBlocks.GLOWING_NULL_STONE_BRICKS.getDefaultState();
        } else {
            return AwakenBlocks.NULL_STONE_BRICKS.getDefaultState();
        }
    }

    static {
        MONSTER_SPAWNS = Lists.newArrayList(new Biome.SpawnEntry(EntityType.PILLAGER, 1, 3, 6));
    }

    public boolean isInsideStructure(IWorld world, StructureAccessor structureAccessor, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block == AwakenBlocks.CORRUPTED_NULL_STONE_BRICKS
                || block == AwakenBlocks.GLOWING_NULL_STONE_BRICKS
                || block == AwakenBlocks.NULL_STONE_BRICKS;
    }
}
