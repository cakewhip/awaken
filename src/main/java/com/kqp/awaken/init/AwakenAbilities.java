package com.kqp.awaken.init;

import com.kqp.awaken.ability.Ability;
import com.kqp.awaken.ability.AbilityComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static final Ability WORLD_RING = register("world_ring_ability");

    public static List<BiomeRingAbility> BIOME_RINGS = new ArrayList();
    public static final Ability SUMMIT_RING = registerBiomeRingAbility("summit")
            .addFrom(Biome.Category.EXTREME_HILLS);
    public static final Ability ARID_RING = registerBiomeRingAbility("arid")
            .addFrom(Biome.Category.DESERT);
    public static final Ability WOODEN_RING = registerBiomeRingAbility("wooden")
            .addFrom(Biome.Category.FOREST)
            .addFrom(Biome.Category.PLAINS);
    public static final Ability FROZEN_RING = registerBiomeRingAbility("frozen")
            .addFrom(Biome.Category.TAIGA)
            .addFrom(Biome.Category.ICY);
    public static final Ability FUNGAL_RING = registerBiomeRingAbility("fungal")
            .addFrom(Biome.Category.MUSHROOM)
            .addFrom(Biome.Category.SWAMP);
    public static final Ability ARCHAIC_RING = registerBiomeRingAbility("archaic")
            .addFrom(Biome.Category.MESA)
            .addFrom(Biome.Category.SAVANNA);
    public static final Ability AQUATIC_RING = registerBiomeRingAbility("aquatic")
            .addFrom(Biome.Category.OCEAN)
            .addFrom(Biome.Category.RIVER)
            .addFrom(Biome.Category.BEACH);
    public static final Ability BRAMBLE_RING = registerBiomeRingAbility("bramble")
            .addFrom(Biome.Category.JUNGLE);
    public static final Ability PEARL_RING = registerBiomeRingAbility("pearl")
            .addFrom(Biome.Category.THEEND);
    public static final Ability EMBER_RING = registerBiomeRingAbility("ember")
            .addFrom(Biome.Category.NETHER);

    public static final Ability BLUEPRINT = register("blueprint_ability");

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

    private static BiomeRingAbility registerBiomeRingAbility(String name) {
        BiomeRingAbility ability = new BiomeRingAbility(ComponentRegistry.INSTANCE.registerIfAbsent(Awaken.id(name + "_ring_ability"), AbilityComponent.class));

        ABILITY_MAP.put(ability.getComponentType().getId(), ability);

        return ability;
    }

    public static class BiomeRingAbility extends Ability {
        public final Set<Biome> biomes = new HashSet();

        public BiomeRingAbility(ComponentType<AbilityComponent> componentType) {
            super(componentType);

            BIOME_RINGS.add(this);
        }

        public BiomeRingAbility add(Biome biome) {
            biomes.add(biome);

            return this;
        }

        public BiomeRingAbility addFrom(Biome.Category category) {
            Registry.BIOME.forEach(biome -> {
                if (biome.getCategory() == category) {
                    biomes.add(biome);
                }
            });

            return this;
        }
    }
}
