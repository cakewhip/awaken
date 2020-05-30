package com.kqp.awaken.init;

import com.kqp.awaken.ability.Ability;
import com.kqp.awaken.ability.AbilityComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class AwakenAbilities {
    public static final Map<Identifier, Ability> ABILITY_MAP = new HashMap();
    public static final Ability SNORKEL_MASK_EFFECT = register("snorkel_mask_effect");

    public static void init() {
        EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> {
            ABILITY_MAP.forEach((id, ability) -> {
                components.put(ability.getComponentType(), new AbilityComponent(player, ability));
            });
        });
    }

    private static Ability register(String name) {
        Ability ability = new Ability(ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(Awaken.MOD_ID, name), AbilityComponent.class));

        ABILITY_MAP.put(ability.getComponentType().getId(), ability);

        return ability;
    }
}
