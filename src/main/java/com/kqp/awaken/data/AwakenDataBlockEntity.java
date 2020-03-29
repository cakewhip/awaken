package com.kqp.awaken.data;

import com.kqp.awaken.Awaken;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

/**
 * BlockEntity used to hold chunk data.
 * Kinda hacky, but it works for now.
 * TODO: find a better way of storing chunk data, perhaps a mixin to inject into the chunk NBT
 */
public class AwakenDataBlockEntity extends BlockEntity {
    /**
     * Where to find this block entity.
     */
    public static final BlockPos DATA_BLOCK_POS = new BlockPos(3, 1, 3);

    /**
     * Flag to determine whether the chunk has generated new awakening ores.
     */
    public boolean genNewOres = false;

    public AwakenDataBlockEntity() {
        super(Awaken.TBlocks.AWAKEN_DATA_BE_TYPE);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);

        genNewOres = tag.getBoolean("genNewOres");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putBoolean("getNewOres", genNewOres);

        return super.toTag(tag);
    }
}
