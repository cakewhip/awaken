package com.kqp.awaken.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;

/**
 * Stores chunk data when it's loaded in.
 * Only because it's hard to access a chunk's compound tag after it's been deserialized.
 */
public class AwakenTemporalChunkData {
    public static HashMap<ChunkPos, ChunkData> CHUNK_DATA_MAP = new HashMap();

    public static class ChunkData {
        public boolean genNewOres;

        public ChunkData() {
        }

        public ChunkData deserialize(CompoundTag compoundTag) {
            genNewOres = compoundTag.getBoolean("genNewOres");

            return this;
        }

        public void serialize(CompoundTag compoundTag) {
            compoundTag.putBoolean("genNewOres", genNewOres);
        }
    }
}
