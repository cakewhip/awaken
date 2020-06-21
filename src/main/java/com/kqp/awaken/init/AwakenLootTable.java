package com.kqp.awaken.init;

import com.google.common.collect.ArrayListMultimap;
import com.kqp.awaken.loot.condition.BiomeSpecificLootCondition;
import com.kqp.awaken.loot.condition.FieryMoonLootCondition;
import com.kqp.awaken.loot.condition.WorldAwakenedLootCondition;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LimitCountLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

/**
 * Class to help add things to the loot table of entities.
 */
public class AwakenLootTable {
    public static ArrayListMultimap<Identifier, FabricLootPoolBuilder> LOOT_MAP = ArrayListMultimap.create();

    public static final LootConditionType WORLD_AWAKENED_CONDITION = register("world_awakened", new WorldAwakenedLootCondition.Serializer());
    public static final LootConditionType FIERY_MOON_CONDITION = register("fiery_moon", new FieryMoonLootCondition.Serializer());
    public static final LootConditionType BIOME_SPECIFIC_CONDITION = register("biome_specific", new BiomeSpecificLootCondition.Serializer());

    public static void init() {
        // Phase 1
        {
            // Reagents
            addLootEntry("minecraft:entities/zombie", AwakenItems.Reagents.NECROTIC_HEART, 1F / 100F);
            addLootEntry("minecraft:entities/creeper", AwakenItems.Reagents.CREEPER_TISSUE, 5F / 100F);
            addLootEntry("minecraft:entities/spider", AwakenItems.Reagents.SPIDER_SILK, 5F / 100F);
            addLootEntry("minecraft:entities/skeleton", AwakenItems.Reagents.BONE_MARROW, 5F / 100F);

            addLootEntry("minecraft:entities/ender_dragon", AwakenItems.Reagents.ENDER_DRAGON_SCALE, 1F, 128);
            addLootEntry("minecraft:entities/wither", AwakenItems.Reagents.WITHER_RIB, 1F, 6);

            // Special Tools
            addLootEntry("minecraft:entities/blaze", AwakenItems.Bows.CINDERED_BOW, 15F / 100F);
            addLootEntry("minecraft:entities/ghast", AwakenItems.Bows.CINDERED_BOW, 20F / 100F);
            addLootEntry("minecraft:entities/slime", AwakenItems.Bows.SLIMEY_BOW, 5F / 100F);
            addLootEntry("minecraft:entities/pillager", AwakenItems.Axes.RAIDERS_AXE, 5F / 100F);
            addLootEntry("minecraft:entities/zombie", AwakenItems.Pickaxes.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/skeleton", AwakenItems.Pickaxes.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/creeper", AwakenItems.Pickaxes.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/spider", AwakenItems.Pickaxes.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/husk", AwakenItems.Shovels.ARCHAEOLOGIST_SPADE, 15F / 100F);
            addLootEntry("minecraft:entities/cave_spider", AwakenItems.Swords.RUSTY_SHANK, 10F / 100F);

            // Special Swords
            addLootEntry("minecraft:entities/drowned", AwakenItems.Swords.ATLANTEAN_SABRE, 10F / 100F);
            addLootEntry("minecraft:entities/guardian", AwakenItems.Swords.ATLANTEAN_SABRE, 15F / 100F);
            addLootEntry("minecraft:entities/elder_guardian", AwakenItems.Swords.ATLANTEAN_SABRE, 20F / 100F);
            addLootEntry("minecraft:entities/wither_skeleton", AwakenItems.Swords.ASHEN_BLADE, 5F / 100F);
            addLootEntry("minecraft:entities/wither", AwakenItems.Swords.ASHEN_BLADE, 33F / 100F);
            addLootEntry("minecraft:entities/stray", AwakenItems.Swords.GLACIAL_SHARD, 15F / 100F);
            addLootEntry("minecraft:entities/enderman", AwakenItems.Swords.ENDERIAN_CUTLASS, 2.5F / 100F);
            addLootEntry("minecraft:entities/endermite", AwakenItems.Swords.ENDERIAN_CUTLASS, 10F / 100F);
            addLootEntry("minecraft:entities/shulker", AwakenItems.Swords.ENDERIAN_CUTLASS, 15F / 100F);
            addLootEntry("minecraft:entities/ender_dragon", AwakenItems.Swords.ENDERIAN_CUTLASS, 25F / 100F);
        }

        // Phase 2
        {
            // Reagents
            addLootEntry("awaken:entities/ancient_demonic_pigman", AwakenItems.Reagents.SMOLDERING_HEART, 5F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_ghast", AwakenItems.Reagents.FIERY_CORE, 10F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_strider", AwakenItems.Reagents.MAGMA_STRAND, 20F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_blaze", AwakenItems.Reagents.CINDERED_SOUL, 20F / 100F, FieryMoonLootCondition.builder());
        }
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, LootCondition.Builder... conditions) {
        FabricLootPoolBuilder fabricLootPoolBuilder =
                FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance).build())
                        .withCondition(KilledByPlayerLootCondition.builder().build())
                        .withEntry(ItemEntry.builder(item).build());

        for (LootCondition.Builder condition : conditions) {
            fabricLootPoolBuilder = fabricLootPoolBuilder.withCondition(condition.build());
        }

        LOOT_MAP.put(new Identifier(id),
                fabricLootPoolBuilder
        );
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, int count, LootCondition.Builder... conditions) {
        FabricLootPoolBuilder fabricLootPoolBuilder =
                FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance).build())
                        .withCondition(KilledByPlayerLootCondition.builder().build())
                        .withFunction(LimitCountLootFunction.builder(BoundedIntUnaryOperator.create(count, count)).build())
                        .withEntry(ItemEntry.builder(item).build());

        for (LootCondition.Builder condition : conditions) {
            fabricLootPoolBuilder = fabricLootPoolBuilder.withCondition(condition.build());
        }

        LOOT_MAP.put(new Identifier(id),
                fabricLootPoolBuilder
        );
    }

    // TODO: the random drop count doesn't work
    public static void addLootEntry(String id, ItemConvertible item, float chance, int min, int max) {
        LOOT_MAP.put(new Identifier(id),
                FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance).build())
                        .withCondition(KilledByPlayerLootCondition.builder().build())
                        .withFunction(SetCountLootFunction.builder(new UniformLootTableRange(min, max)).build())
                        .withEntry(ItemEntry.builder(item).build())
        );
    }

    public static void onLootTableLoading(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter lootTableSetter) {
        LOOT_MAP.forEach((identifier, builder) -> {
            if (identifier.equals(id)) {
                supplier.withPool(builder.build());
            }
        });
    }

    private static LootConditionType register(String id, JsonSerializer<? extends LootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(Awaken.MOD_ID, id),new LootConditionType(serializer));
    }
}
