package com.kqp.awaken.world.placer;

import com.kqp.awaken.init.AwakenDimensions;
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class NullSpacePlacer implements EntityPlacer {
    @Override
    public BlockPattern.TeleportTarget placeEntity(Entity entity, ServerWorld serverWorld, Direction direction, double v, double v1) {
        ServerWorld nullSpace = serverWorld.getServer().getWorld(AwakenDimensions.NULL_SPACE);

        BlockPos loc = new BlockPos(entity.getX() - entity.getX() % 5, 224, entity.getZ() - entity.getZ() % 5);

        generateSpawnRoom(nullSpace, loc);

        entity.fallDistance = 0F;

        return new BlockPattern.TeleportTarget(new Vec3d(loc.getX(), loc.getY() + 1, loc.getZ()), Vec3d.ZERO, 0);
    }

    private void generateSpawnRoom(ServerWorld world, BlockPos cent) {
        if (world.getBlockState(cent).getBlock() != Blocks.BEDROCK) {
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    for (int k = 0; k < 4; k++) {
                        BlockPos pos = cent.add(i, k, j);

                        if (k == 0) {
                            world.setBlockState(pos, Blocks.BEDROCK.getDefaultState());
                        } else {
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }

            world.setBlockState(cent.add(0, 1, 0), Blocks.TORCH.getDefaultState());
        }
    }
}
