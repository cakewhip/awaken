package com.kqp.awaken.ability;

import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class AbilityComponent implements EntitySyncedComponent {
    public boolean flag;

    public PlayerEntity player;
    public Ability ability;

    public AbilityComponent(PlayerEntity player, Ability ability) {
        this.player = player;
        this.ability = ability;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        flag = compoundTag.getBoolean("Flag");
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putBoolean("Flag", flag);

        return compoundTag;
    }

    @Override
    public Entity getEntity() {
        return player;
    }

    @Override
    public ComponentType<?> getComponentType() {
        return ability.getComponentType();
    }
}
