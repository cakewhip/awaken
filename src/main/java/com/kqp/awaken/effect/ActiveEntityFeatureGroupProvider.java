package com.kqp.awaken.effect;

import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface ActiveEntityFeatureGroupProvider {
    List<EntityFeatureGroup> getActiveEntityFeatureGroups(PlayerEntity player);
}
