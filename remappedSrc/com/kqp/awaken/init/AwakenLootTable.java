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
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditions;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LimitCountLootFunction;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

/**
 * Class to help add things to the loot table of entities.
 */
public class AwakenLootTable {
    public static ArrayListMultimap<Identifier, FabricLootPoolBuilder> LOOT_MAP = ArrayListMultimap.create();

    public static void init() {
        // Custom Loot Conditions
        LootConditions.register(new WorldAwakenedLootCondition.Factory());
        LootConditions.register(new FieryMoonLootCondition.Factory());
        LootConditions.register(new BiomeSpecificLootCondition.Factory());

        // Phase 1
        {
            // Reagents
            addLootEntry("minecraft:entities/zombie", AwakenItems.NECROTIC_HEART, 1F / 100F);
            addLootEntry("minecraft:entities/creeper", AwakenItems.CREEPER_TISSUE, 5F / 100F);
            addLootEntry("minecraft:entities/spider", AwakenItems.SPIDER_SILK, 5F / 100F);
            addLootEntry("minecraft:entities/skeleton", AwakenItems.BONE_MARROW, 5F / 100F);

            addLootEntry("minecraft:entities/ender_dragon", AwakenItems.ENDER_DRAGON_SCALE, 1F, 128);
            addLootEntry("minecraft:entities/wither", AwakenItems.WITHER_RIB, 1F, 6);

            // Special Tools
            addLootEntry("minecraft:entities/blaze", AwakenItems.CINDERED_BOW, 15F / 100F);
            addLootEntry("minecraft:entities/ghast", AwakenItems.CINDERED_BOW, 20F / 100F);
            addLootEntry("minecraft:entities/slime", AwakenItems.SLIMEY_BOW, 5F / 100F);
            addLootEntry("minecraft:entities/pillager", AwakenItems.RAIDERS_AXE, 5F / 100F);
            addLootEntry("minecraft:entities/zombie", AwakenItems.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/skeleton", AwakenItems.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/creeper", AwakenItems.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/spider", AwakenItems.ESCAPE_PLAN, 2.5F / 100F);
            addLootEntry("minecraft:entities/husk", AwakenItems.ARCHAEOLOGIST_SPADE, 15F / 100F);
            addLootEntry("minecraft:entities/cave_spider", AwakenItems.RUSTY_SHANK, 10F / 100F);

            // Special Swords
            addLootEntry("minecraft:entities/drowned", AwakenItems.ATLANTEAN_SABRE, 10F / 100F);
            addLootEntry("minecraft:entities/guardian", AwakenItems.ATLANTEAN_SABRE, 15F / 100F);
            addLootEntry("minecraft:entities/elder_guardian", AwakenItems.ATLANTEAN_SABRE, 20F / 100F);
            addLootEntry("minecraft:entities/wither_skeleton", AwakenItems.ASHEN_BLADE, 5F / 100F);
            addLootEntry("minecraft:entities/wither", AwakenItems.ASHEN_BLADE, 33F / 100F);
            addLootEntry("minecraft:entities/stray", AwakenItems.GLACIAL_SHARD, 15F / 100F);
            addLootEntry("minecraft:entities/enderman", AwakenItems.ENDERIAN_CUTLASS, 2.5F / 100F);
            addLootEntry("minecraft:entities/endermite", AwakenItems.ENDERIAN_CUTLASS, 10F / 100F);
            addLootEntry("minecraft:entities/shulker", AwakenItems.ENDERIAN_CUTLASS, 15F / 100F);
            addLootEntry("minecraft:entities/ender_dragon", AwakenItems.ENDERIAN_CUTLASS, 25F / 100F);
        }

        // Phase 2
        {
            // Reagents
            addLootEntry("awaken:entities/ancient_demonic_pigman", AwakenItems.SMOLDERING_HEART, 5F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_ghast", AwakenItems.FIERY_CORE, 10F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_strider", AwakenItems.MAGMA_STRAND, 20F / 100F, FieryMoonLootCondition.builder());
            addLootEntry("awaken:entities/ancient_demonic_blaze", AwakenItems.CINDERED_SOUL, 20F / 100F, FieryMoonLootCondition.builder());
        }
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, LootCondition.Builder... conditions) {
        FabricLootPoolBuilder fabricLootPoolBuilder =
                FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withEntry(ItemEntry.builder(item));

        for (LootCondition.Builder condition : conditions) {
            fabricLootPoolBuilder = fabricLootPoolBuilder.withCondition(condition);
        }

        LOOT_MAP.put(new Identifier(id),
                fabricLootPoolBuilder
        );
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, int count, LootCondition.Builder... conditions) {
        FabricLootPoolBuilder fabricLootPoolBuilder =
                FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withFunction(LimitCountLootFunction.builder(BoundedIntUnaryOperator.create(count, count)))
                        .withEntry(ItemEntry.builder(item));

        for (LootCondition.Builder condition : conditions) {
            fabricLootPoolBuilder = fabricLootPoolBuilder.withCondition(condition);
        }

        LOOT_MAP.put(new Identifier(id),
                fabricLootPoolBuilder
        );
    }

    // TODO: the random drop count doesn't work
    public static void addLootEntry(String id, ItemConvertible item, float chance, int min, int max) {
        LOOT_MAP.put(new Identifier(id),
                FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withFunction(LimitCountLootFunction.builder(BoundedIntUnaryOperator.create(min, max)))
                        .withEntry(ItemEntry.builder(item))
        );
    }

    public static void onLootTableLoading(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter lootTableSetter) {
        LOOT_MAP.forEach((identifier, builder) -> {
            if (identifier.equals(id)) {
                supplier.withPool(builder);
            }
        });
    }
}
