package com.kqp.terminus.data;

import com.kqp.terminus.Terminus;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class TerminusDataBlockEntity extends BlockEntity {
    public static final BlockPos DATA_BLOCK_POS = new BlockPos(3, 1, 3);

    public boolean genNewOres = false;

    public TerminusDataBlockEntity() {
        super(Terminus.TBlocks.TERMINUS_DATA_BE_TYPE);
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
