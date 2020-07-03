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

    public static final Ability SNORKEL_MASK = register("snorkel_mask_ability");
    public static final Ability BONE_CROWN = register("bone_crown_ability");
    public static final Ability DISCORD_BELT = register("discord_belt_ability");
    public static final Ability DYNAMITE_STICK = register("dynamite_stick_ability");
    public static final Ability LIGHTNING_BOTTLE = register("lightning_bottle_ability");
    public static final Ability RAIN_HAT = register("rain_hat_ability");
    public static final Ability SCORCHED_MASK = register("scorched_mask_ability");
    public static final Ability SHULKER_CHARM = register("shulker_charm_ability");
    public static final Ability SILKY_GLOVE = register("silky_glove_ability");
    public static final Ability NETHERIAN_BELT = register("netherian_belt_ability");
    public static final Ability NO_FALL_DAMAGE = register("no_fall_damage_ability");

    public static void init() {
        EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> {
            ABILITY_MAP.forEach((id, ability) -> {
                components.put(ability.getComponentType(), new AbilityComponent(player, ability));
            });
        });
    }

    private static Ability register(String name) {
        Ability ability = new Ability(ComponentRegistry.INSTANCE.registerIfAbsent(Awaken.id(name), AbilityComponent.class));

        ABILITY_MAP.put(ability.getComponentType().getId(), ability);

        return ability;
    }
}
