package com.kqp.awaken.world.data;

import net.minecraft.nbt.CompoundTag;

public interface AwakenLevelDataTagContainer {
    CompoundTag getAwakenLevelDataTag();

    void setAwakenLevelDataTag(CompoundTag awakenLevelDataTag);
}
