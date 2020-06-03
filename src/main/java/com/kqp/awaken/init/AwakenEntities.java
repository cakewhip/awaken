package com.kqp.awaken.init;

import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.entity.mob.DireWolfEntity;
import com.kqp.awaken.entity.mob.RaptorChickenEntity;
import com.kqp.awaken.entity.mob.SpiderSacEntity;
import com.kqp.awaken.entity.mob.VoidGhostEntity;
import com.kqp.awaken.world.spawning.BoneCrownSpawnCondition;
import com.kqp.awaken.world.spawning.CaveSpawnCondition;
import com.kqp.awaken.world.spawning.ConditionalSpawnEntry;
import com.kqp.awaken.world.spawning.FieryMoonSpawnCondition;
import com.kqp.awaken.world.spawning.PostAwakeningSpawnCondition;
import com.kqp.awaken.world.spawning.SpawnCondition;
import com.kqp.awaken.world.spawning.SurfaceSpawnCondition;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import java.util.function.Predicate;

public class AwakenEntities {
    // TODO: add killer bunny, hostile mob in savannahs
    // TODO: add mummies, hostile mob in deserts
    // TODO: add tarantulas, hostile mob in deserts
    // TODO: add pharaohs, hostile mob in deserts
    // TODO: add ender agent, hostile mob in strongholds
    // TODO: add ice devil, hostile mob in ice biomes
    // TODO: add wind spirit, hostile mob at high elevations
    // TODO: add witch doctor, hostile mob in swamps
    // TODO: add bullet ant, hostile mob in jungles
    // TODO: add dart frog, hostile mob in jungles
    // TODO: add pirate ghost, hostile mob in deep oceans and frozen oceans
    // TODO: add fungal assassin, hostile mob in mushroom biomes and dark forests
    // TODO: add woodland spirit, hostile mob in dark forests
    // TODO: add renegade, hostile mob in badlands
    // TODO: add vagabond, neutral mob in badlands

    public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "raptor_chicken"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RaptorChickenEntity::new)
                    .size(EntityDimensions.fixed(0.95F, 1.65F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "dire_wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DireWolfEntity::new)
                    .size(EntityDimensions.fixed(1.5F, 1F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<SpiderSacEntity> SPIDER_SAC = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "spider_sac"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpiderSacEntity::new)
                    .size(EntityDimensions.fixed(0.8F, 0.8F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<VoidGhostEntity> VOID_GHOST = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "void_ghost"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidGhostEntity::new)
                    .size(EntityDimensions.fixed(0.6F, 1.9F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<AbominationEntity> ABOMINATION = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Awaken.MOD_ID, "abomination"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AbominationEntity::new)
                    .size(EntityDimensions.fixed(0.6F * 3F, 1.95F * 3F))
                    .trackable(72, 3)
                    .build()
    );

    public static void init() {
        Awaken.info("Initializing entities");

        // Trinket Effects
        {
            // Apply wither rib effect
            addHostileSpawn(spawnEntry(EntityType.SKELETON, 150, 4, 4, BoneCrownSpawnCondition.INSTANCE), biome -> {
                        for (Biome.SpawnEntry entry : biome.getEntitySpawnList(SpawnGroup.MONSTER)) {
                            if (entry.type == EntityType.SKELETON) {
                                return true;
                            }
                        }

                        return false;
                    }
            );
        }

        // Phase 2 Mobs
        {
            register(RAPTOR_CHICKEN, 0x9C0202, 0x610000, RaptorChickenEntity.createRaptorChickenAttributes());
            addHostileSpawn(spawnEntry(RAPTOR_CHICKEN, 100, 1, 3, PostAwakeningSpawnCondition.INSTANCE),
                    biome -> biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_EDGE || biome == Biomes.JUNGLE_HILLS
            );

            register(DIRE_WOLF, 0xD6E9FF, 0x97ADCC, DireWolfEntity.createDireWolfAttributes());
            addHostileSpawn(spawnEntry(DIRE_WOLF, 100, 1, 3, PostAwakeningSpawnCondition.INSTANCE, SurfaceSpawnCondition.INSTANCE),
                    biome -> biome.getTemperature() <= 0.05F
            );

            register(SPIDER_SAC, 0x000000, 0xFFFFFF, SpiderSacEntity.createSpiderSacAttributes());
            addHostileSpawn(spawnEntry(SPIDER_SAC, 25, 1, 1, CaveSpawnCondition.INSTANCE), biome -> true);

            register(VOID_GHOST, 0x0000000, 0xFFFFFF, VoidGhostEntity.createVoidGhostAttributes());

            register(ABOMINATION, 0xFFFFFF, 0x000000, AbominationEntity.createAbominationAttributes());
        }

        // Fiery Moon
        {
            addHostileSpawn(spawnEntry(EntityType.ZOMBIFIED_PIGLIN, 300, 4, 4, FieryMoonSpawnCondition.INSTANCE), biome -> true);
            addHostileSpawn(spawnEntry(EntityType.ZOGLIN, 250, 4, 4, FieryMoonSpawnCondition.INSTANCE), biome -> true);
        }
    }

    private static <T extends LivingEntity> void register(EntityType<T> type, int primaryColor, int secondaryColor, DefaultAttributeContainer.Builder attributeBuilder) {
        Registry.register(Registry.ITEM, new Identifier(EntityType.getId(type).toString() + "_spawn_egg"),
                new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Settings().group(ItemGroup.MISC))
        );

        FabricDefaultAttributeRegistry.register(type, attributeBuilder);
    }

    private static Biome.SpawnEntry spawnEntry(EntityType<?> type, int weight, int min, int max, SpawnCondition... spawnConditions) {
        ConditionalSpawnEntry spawnEntry = new ConditionalSpawnEntry(type, weight, min, max);

        for (SpawnCondition spawnCondition : spawnConditions) {
            spawnEntry.addCondition(spawnCondition);
        }

        return spawnEntry;
    }

    private static void addHostileSpawn(Biome.SpawnEntry spawnEntry, Predicate<Biome> biomePredicate) {
        for (Biome biome : Biome.BIOMES) {
            if (biomePredicate.test(biome)) {
                System.out.println("adding " + spawnEntry.type.getTranslationKey() + " to " + biome.getTranslationKey());
                biome.getEntitySpawnList(SpawnGroup.MONSTER).add(spawnEntry);
            }
        }
    }
}
