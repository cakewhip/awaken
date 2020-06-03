package com.kqp.awaken.ability;

import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Wrapper class around ComponentType, purely because I hate typing out ComponentType<AbilityComponent>.
 */
public class Ability {
    private final ComponentType<AbilityComponent> componentType;

    public Ability(ComponentType<AbilityComponent> componentType) {
        this.componentType = componentType;
    }

    public AbilityComponent get(PlayerEntity player) {
        return componentType.get(player);
    }

    public ComponentType<AbilityComponent> getComponentType() {
        return componentType;
    }

    @Environment(EnvType.CLIENT)
    public String getTranslationKey() {
        return "ability." + componentType.getId().getPath();
    }
}
