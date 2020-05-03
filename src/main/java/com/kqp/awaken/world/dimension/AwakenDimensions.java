package com.kqp.awaken.world.dimension;

import com.kqp.awaken.init.Awaken;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class AwakenDimensions {
    public static final FabricDimensionType NULL = FabricDimensionType.builder()
            .defaultPlacer((oldEntity, destinationWorld, portalDir, horizontalOffset, verticalOffset) -> {
                BlockPos pos = destinationWorld.getTopPosition(Heightmap.Type.WORLD_SURFACE, BlockPos.ORIGIN);
                return new BlockPattern.TeleportTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), oldEntity.getVelocity(), (int) oldEntity.yaw);
            })
            .factory(NullDimension::new)
            .skyLight(false)
            .buildAndRegister(new Identifier(Awaken.MOD_ID, "null"));

    /*
    public static final ChunkGeneratorType<ChunkGeneratorConfig, NullChunkGenerator> NULL_CHUNK_GENERATOR = Registry.register(
            Registry.CHUNK_GENERATOR_TYPE,
            new Identifier(Awaken.MOD_ID, "null"),
            new ChunkGeneratorType<>(
                    NullChunkGenerator::new,
                    false,
                    ChunkGeneratorConfig::new
            )
    );
     */

    public static void init() {

    }
}
