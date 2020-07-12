package com.kqp.awaken.init;

import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.entity.mob.DireWolfEntity;
import com.kqp.awaken.entity.mob.EnderAgentEntity;
import com.kqp.awaken.entity.mob.RadianceEntity;
import com.kqp.awaken.entity.mob.RaptorChickenEntity;
import com.kqp.awaken.entity.mob.RenegadeEntity;
import com.kqp.awaken.entity.mob.SpiderSacEntity;
import com.kqp.awaken.entity.mob.VagabondEntity;
import com.kqp.awaken.entity.mob.VoidGhostEntity;
import com.kqp.awaken.entity.projectile.RadianceLightEntity;
import com.kqp.awaken.world.spawning.BoneCrownSpawnCondition;
import com.kqp.awaken.world.spawning.CaveSpawnCondition;
import com.kqp.awaken.world.spawning.ConditionalSpawnEntry;
import com.kqp.awaken.world.spawning.FieryMoonSpawnCondition;
import com.kqp.awaken.world.spawning.PostAwakeningSpawnCondition;
import com.kqp.awaken.world.spawning.SpawnCondition;
import com.kqp.awaken.world.spawning.SurfaceSpawnCondition;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
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
    public static final EntityType<RaptorChickenEntity> RAPTOR_CHICKEN = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("raptor_chicken"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RaptorChickenEntity::new)
                    .dimensions(EntityDimensions.fixed(0.95F, 1.65F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<DireWolfEntity> DIRE_WOLF = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("dire_wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DireWolfEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5F, 1F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<SpiderSacEntity> SPIDER_SAC = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("spider_sac"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpiderSacEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8F, 0.8F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<VoidGhostEntity> VOID_GHOST = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("void_ghost"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidGhostEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.9F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<AbominationEntity> ABOMINATION = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("abomination"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AbominationEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F * 3F, 1.95F * 3F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<RadianceEntity> RADIANCE = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("radiance"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RadianceEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9F, 3.5F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<RadianceLightEntity> RADIANCE_LIGHT = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("radiance_light"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, RadianceLightEntity::factory)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<RenegadeEntity> RENEGADE = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("renegade"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RenegadeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.95F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<VagabondEntity> VAGABOND = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("vagabond"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VagabondEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6F, 1.95F))
                    .trackable(72, 3)
                    .build()
    );

    public static final EntityType<EnderAgentEntity> ENDER_AGENT = Registry.register(
            Registry.ENTITY_TYPE,
            Awaken.id("ender_agent"),
            
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EnderAgentEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75F, 1.0F))
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

            register(RADIANCE, 0xFFFFFF, 0x000000, RadianceEntity.createRadianceAttributes());

            register(RENEGADE, 0xFFFFFF, 0xFFFFFF, RenegadeEntity.createRenegadeAttributes());
            addHostileSpawn(spawnEntry(RENEGADE, 200, 1, 1, SurfaceSpawnCondition.INSTANCE), biome ->
                    biome.getCategory() == Biome.Category.MESA || biome.getCategory() == Biome.Category.SAVANNA
            );

            register(VAGABOND, 0xFFFFFF, 0xFFFFFF, VagabondEntity.createVagabondAttributes());
            addHostileSpawn(spawnEntry(VAGABOND, 200, 1, 1, SurfaceSpawnCondition.INSTANCE), biome ->
                    biome.getCategory() == Biome.Category.MESA || biome.getCategory() == Biome.Category.SAVANNA
            );

            register(ENDER_AGENT, 0xFFFFFF, 0xFFFFFF, EnderAgentEntity.createEnderAgentAttributes());
            addHostileSpawn(spawnEntry(ENDER_AGENT, 700, 4, 8), biome -> true);
        }

        // Fiery Moon
        {
            addHostileSpawn(spawnEntry(EntityType.ZOMBIFIED_PIGLIN, 600, 4, 4, FieryMoonSpawnCondition.INSTANCE), biome -> true);
            addHostileSpawn(spawnEntry(EntityType.BLAZE, 600, 3, 3, FieryMoonSpawnCondition.INSTANCE), biome -> true);
            addHostileSpawn(spawnEntry(EntityType.GHAST, 450, 1, 1, FieryMoonSpawnCondition.INSTANCE), biome -> true);
            addHostileSpawn(spawnEntry(EntityType.ZOGLIN, 300, 1, 2, FieryMoonSpawnCondition.INSTANCE), biome -> true);
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
