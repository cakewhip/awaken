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
    public static final Ability BONE_CROWN_EFFECT = register("bone_crown_effect");
    public static final Ability DISCORD_BELT_EFFECT = register("discord_belt_effect");
    public static final Ability DYNAMITE_STICK_EFFECT = register("dynamite_stick_effect");
    public static final Ability LIGHTNING_BOTTLE_EFFECT = register("lightning_bottle_effect");
    public static final Ability RAIN_HAT_EFFECT = register("rain_hat_effect");
    public static final Ability SCORCHED_MASK_EFFECT = register("scorched_mask_effect");
    public static final Ability SHULKER_CHARM_EFFECT = register("shulker_charm_effect");
    public static final Ability SILKY_GLOVE_EFFECT = register("silky_glove_effect");

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
