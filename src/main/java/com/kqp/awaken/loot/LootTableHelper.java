package com.kqp.awaken.loot;

import com.kqp.awaken.Awaken;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
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
public class LootTableHelper {
    public static HashSet<LootEntry> LOOT_ENTRIES = new HashSet();

    public static void init() {
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
        LOOT_ENTRIES.add(new LootEntry(new Identifier(id), item, chance, 1, 1));
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, int count) {
        LOOT_ENTRIES.add(new LootEntry(new Identifier(id), item, chance, count, count));
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance, int min, int max) {
        LOOT_ENTRIES.add(new LootEntry(new Identifier(id), item, chance, min, max));
    }

    public static void onLootTableLoading(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableLoadingCallback.LootTableSetter lootTableSetter) {
        for (LootEntry lootEntry : LOOT_ENTRIES) {
            if (lootEntry.id.equals(id)) {
                // Builder created with 2 conditions: random chance and player kill required
                FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(lootEntry.chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withFunction(LimitCountLootFunction.builder(BoundedIntUnaryOperator.create(lootEntry.min, lootEntry.max)))
                        .withEntry(ItemEntry.builder(lootEntry.item));

                supplier.withPool(builder);
            }
        }
    }

    /**
     * Data class to represent a loot entry.
     */
    static class LootEntry {
        public Identifier id;
        public ItemConvertible item;
        public float chance;
        public int min, max;

        public LootEntry(Identifier id, ItemConvertible item, float chance, int min, int max) {
            this.id = id;
            this.item = item;
            this.chance = chance;
            this.min = min;
            this.max = max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LootEntry lootEntry = (LootEntry) o;
            return Float.compare(lootEntry.chance, chance) == 0 &&
                    min == lootEntry.min &&
                    max == lootEntry.max &&
                    Objects.equals(id, lootEntry.id) &&
                    Objects.equals(item, lootEntry.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, item, chance, min, max);
        }
    }
}
