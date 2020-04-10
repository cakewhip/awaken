package com.kqp.awaken.loot;

import com.google.common.collect.ArrayListMultimap;
import com.kqp.awaken.Awaken;
import com.kqp.awaken.loot.condition.BloodMoonLootCondition;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.LootConditions;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LimitCountLootFunction;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Objects;

/**
 * Class to help add things to the loot table of entities.
 */
public class TLootTableHandler {
    public static ArrayListMultimap<Identifier, FabricLootPoolBuilder> LOOT_MAP = ArrayListMultimap.create();

    public static void init() {
        // Custom Loot Conditions
        LootConditions.register(new BloodMoonLootCondition.Factory());

        // Phase 1
        {
            // Reagents
            addLootEntry("minecraft:entities/ender_dragon", Awaken.TItems.ENDER_DRAGON_SCALE, 1F, 128);
            addLootEntry("minecraft:entities/wither", Awaken.TItems.WITHER_RIB, 1F, 6);

            // Special Tools
            addLootEntry("minecraft:entities/blaze", Awaken.TItems.CINDERED_BOW, 10F / 100F);
            addLootEntry("minecraft:entities/ghast", Awaken.TItems.CINDERED_BOW, 15F / 100F);
            addLootEntry("minecraft:entities/slime", Awaken.TItems.SLIMEY_BOW, 5F / 100F);
            addLootEntry("minecraft:entities/pillager", Awaken.TItems.RAIDERS_AXE, 2.5F / 100F);
            addLootEntry("minecraft:entities/zombie", Awaken.TItems.ESCAPE_PLAN, 0.1F / 100F);
            addLootEntry("minecraft:entities/skeleton", Awaken.TItems.ESCAPE_PLAN, 0.1F / 100F);
            addLootEntry("minecraft:entities/creeper", Awaken.TItems.ESCAPE_PLAN, 0.1F / 100F);
            addLootEntry("minecraft:entities/spider", Awaken.TItems.ESCAPE_PLAN, 0.1F / 100F);
            addLootEntry("minecraft:entities/husk", Awaken.TItems.ARCHAEOLOGIST_SPADE, 10F / 100F);
            addLootEntry("minecraft:entities/cave_spider", Awaken.TItems.RUSTY_SHANK, 10F / 100F);

            // Special Swords
            addLootEntry("minecraft:entities/drowned", Awaken.TItems.ATLANTEAN_SABRE, 1F / 100F);
            addLootEntry("minecraft:entities/guardian", Awaken.TItems.ATLANTEAN_SABRE, 5F / 100F);
            addLootEntry("minecraft:entities/elder_guardian", Awaken.TItems.ATLANTEAN_SABRE, 15F / 100F);
            addLootEntry("minecraft:entities/wither_skeleton", Awaken.TItems.ASHEN_BLADE, 1F / 100F);
            addLootEntry("minecraft:entities/wither", Awaken.TItems.ASHEN_BLADE, 33F / 100F);
            addLootEntry("minecraft:entities/stray", Awaken.TItems.GLACIAL_SHARD, 5F / 100F);
            addLootEntry("minecraft:entities/enderman", Awaken.TItems.ENDERIAN_CUTLASS, 0.1F / 100F);
            addLootEntry("minecraft:entities/endermite", Awaken.TItems.ENDERIAN_CUTLASS, 1F / 100F);
            addLootEntry("minecraft:entities/shulker", Awaken.TItems.ENDERIAN_CUTLASS, 2.5F / 100F);
            addLootEntry("minecraft:entities/ender_dragon", Awaken.TItems.ENDERIAN_CUTLASS, 5F / 100F);
        }
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance) {
        LOOT_MAP.put(new Identifier(id),
                FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withEntry(ItemEntry.builder(item))
        );
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, int count) {
        LOOT_MAP.put(new Identifier(id),
                FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withFunction(LimitCountLootFunction.builder(BoundedIntUnaryOperator.create(count, count)))
                        .withEntry(ItemEntry.builder(item))
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
