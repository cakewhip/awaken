package com.kqp.awaken.item.armor;

import com.kqp.awaken.effect.EntityFeatureGroup;

import java.util.Optional;

public interface ArmorEFGMutator {
    void setBonus(EntityFeatureGroup efg);

    Optional<EntityFeatureGroup> getItemMods();

    void setSetBonus(EntityFeatureGroup efg);

    Optional<EntityFeatureGroup> getSetMods();
}
