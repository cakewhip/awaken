package com.kqp.awaken.world.placer;

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.dimension.DimensionType;

public class OverworldPlacer implements EntityPlacer {
    @Override
    public BlockPattern.TeleportTarget placeEntity(Entity entity, ServerWorld serverWorld, Direction direction, double v, double v1) {
        ServerWorld overworld = serverWorld.getServer().getWorld(DimensionType.OVERWORLD);

        BlockPos loc = overworld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, overworld.getSpawnPos());

        if (entity instanceof ServerPlayerEntity) {
            loc = ((ServerPlayerEntity) entity).getSpawnPointPosition();
        }

        return new BlockPattern.TeleportTarget(new Vec3d(loc.getX(), loc.getY(), loc.getZ()), Vec3d.ZERO, 0);
    }
}
