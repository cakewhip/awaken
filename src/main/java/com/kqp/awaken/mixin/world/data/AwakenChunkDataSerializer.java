package com.kqp.awaken.mixin.world.data;

import com.kqp.awaken.world.data.AwakenTemporalChunkData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Used to store chunk data like if new ores were generated or not.
 */
@Mixin(ChunkSerializer.class)
public class AwakenChunkDataSerializer {
    @Inject(method = "deserialize", at = @At(value = "RETURN"))
    private static void deserializeAwakenData(ServerWorld serverWorld, StructureManager structureManager, PointOfInterestStorage pointOfInterestStorage, ChunkPos chunkPos, CompoundTag compoundTag, CallbackInfoReturnable<ProtoChunk> callbackInfo) {
        if (serverWorld.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenTemporalChunkData.ChunkData awakenChunkData = new AwakenTemporalChunkData.ChunkData();

            if (compoundTag.contains("awaken_data")) {
                awakenChunkData.deserialize(compoundTag.getCompound("awaken_data"));
            }

            AwakenTemporalChunkData.CHUNK_DATA_MAP.put(chunkPos, awakenChunkData);
        }
    }

    @Inject(method = "serialize", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void serializeAwakenData(ServerWorld serverWorld, Chunk chunk, CallbackInfoReturnable<CompoundTag> callbackInfo, ChunkPos chunkPos, CompoundTag compoundTag, CompoundTag compoundTag2) {
        if (serverWorld.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenTemporalChunkData.ChunkData chunkData = AwakenTemporalChunkData.CHUNK_DATA_MAP.get(chunkPos);

            if (chunkData != null) {
                CompoundTag awakenTag = new CompoundTag();
                chunkData.serialize(awakenTag);

                compoundTag.put("awaken_data", awakenTag);
            }
        }
    }
}
