package com.kqp.awaken.world;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Random;

/**
 * Used to generate ore veins.
 */
public class AwakenOreGen {
    public static final Random RANDOM = new Random();

    public static void generate(WorldChunk chunk, int maxY, int n, Block block) {
        for (int i = 0; i < n; i++) {
            genVein(chunk, block,
                    new BlockPos(RANDOM.nextInt(16), 4 + RANDOM.nextInt(maxY - 4), RANDOM.nextInt(16))
            );
        }
    }

    private static void genVein(WorldChunk chunk, Block block, BlockPos pos) {
        int orientationX = RANDOM.nextBoolean() ? 1 : -1;
        int orientationY = RANDOM.nextBoolean() ? 1 : -1;
        int orientationZ = RANDOM.nextBoolean() ? 1 : -1;

        switch (RANDOM.nextInt(4)) {
            case 0:
                placeBlock(chunk, pos.add(0, 0, 0), block);
                placeBlock(chunk, pos.add(orientationX, 0, 0), block);
                placeBlock(chunk, pos.add(orientationX, 0, orientationZ), block);
                placeBlock(chunk, pos.add(0, 0, orientationZ), block);
                placeBlock(chunk, pos.add(0, 1, 0), block);
                placeBlock(chunk, pos.add(orientationX, 1, 0), block);
                placeBlock(chunk, pos.add(orientationX, 1, orientationZ), block);
                placeBlock(chunk, pos.add(0, 1, orientationZ), block);
                break;
            default:
                placeBlock(chunk, pos.add(0, 0, 0), block);
                placeBlock(chunk, pos.add(orientationX, 0, 0), block);
                placeBlock(chunk, pos.add(0, orientationY, 0), block);
                placeBlock(chunk, pos.add(orientationX, orientationY, orientationZ), block);
                break;
        }
    }

    private static void placeBlock(WorldChunk chunk, BlockPos pos, Block block) {
        if (chunk.getBlockState(pos).getBlock() == Blocks.STONE) {
            chunk.setBlockState(pos, block.getDefaultState(), false);
        }
    }
}
