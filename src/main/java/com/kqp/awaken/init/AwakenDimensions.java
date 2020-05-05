package com.kqp.awaken.init;

import com.kqp.awaken.world.chunk.AwakenChunkGeneratorType;
import com.kqp.awaken.world.chunk.NullSpaceChunkGenerator;
import com.kqp.awaken.world.chunk.NullSpaceChunkGeneratorConfig;
import com.kqp.awaken.world.dimension.NullSpaceDimension;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.chunk.CavesChunkGenerator;
import net.minecraft.world.gen.chunk.CavesChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

import java.util.function.Supplier;

public class AwakenDimensions {
    public static final FabricDimensionType NULL_SPACE = FabricDimensionType.builder()
            .defaultPlacer((oldEntity, destinationWorld, portalDir, horizontalOffset, verticalOffset) -> {
                BlockPos pos = destinationWorld.getTopPosition(Heightmap.Type.WORLD_SURFACE, BlockPos.ORIGIN);
                return new BlockPattern.TeleportTarget(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), oldEntity.getVelocity(), (int) oldEntity.yaw);
            })
            .factory(NullSpaceDimension::new)
            .skyLight(false)
            .buildAndRegister(new Identifier(Awaken.MOD_ID, "null"));


    public static final ChunkGeneratorType<CavesChunkGeneratorConfig, CavesChunkGenerator> NULL_SPACE_CHUNK_GENERATOR = registerChunkGenerator(
            "null_space",
            NullSpaceChunkGenerator::new,
            NullSpaceChunkGeneratorConfig::new,
            false
    );

    public static void init() {
    }

    private static <C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> ChunkGeneratorType<C, T> registerChunkGenerator(String name, AwakenChunkGeneratorType.GeneratorFactory<C, T> factory, Supplier<C> config, boolean buffetScreenOption) {
        ChunkGeneratorType<C, T> generator = new AwakenChunkGeneratorType<>(factory, buffetScreenOption, config);
        Registry.register(Registry.CHUNK_GENERATOR_TYPE, new Identifier(Awaken.MOD_ID, name), generator);

        return generator;
    }
}
