package com.kqp.terminus.loot;

import com.kqp.terminus.Terminus;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.HashSet;

/**
 * Class to help add things to the loot table of entities.
 */
public class LootTableHelper implements LootTableLoadingCallback {
    public static HashSet<LootEntry> LOOT_ENTRIES = new HashSet();

    public static void init() {
        addLootEntry("minecraft:guardian", Terminus.TItems.ATLANTEAN_SABRE, 25F / 100F);
        addLootEntry("minecraft:elder_guardian", Terminus.TItems.ATLANTEAN_SABRE, 1F / 100F);
        addLootEntry("minecraft:drowned", Terminus.TItems.ATLANTEAN_SABRE, 0.25F / 100F);
        addLootEntry("minecraft:wither_skeleton", Terminus.TItems.ASHEN_BLADE, 1F / 100F);
        addLootEntry("minecraft:wither", Terminus.TItems.ASHEN_BLADE, 33F / 100F);
        addLootEntry("minecraft:stray", Terminus.TItems.GLACIAL_SHARD, 2.5F / 100F);
        addLootEntry("minecraft:enderman", Terminus.TItems.ENDERIAN_CUTLASS, 1F / 100F);
        addLootEntry("minecraft:shulker", Terminus.TItems.ENDERIAN_CUTLASS, 5F / 100F);
        addLootEntry("minecraft:endermite", Terminus.TItems.ENDERIAN_CUTLASS, 1F / 100F);
        addLootEntry("minecraft:ender_dragon", Terminus.TItems.ENDERIAN_CUTLASS, 25F / 100F);

        addLootEntry("minecraft:blaze", Terminus.TItems.CINDERED_BOW, 2.5F / 100F);
        addLootEntry("minecraft:ghast", Terminus.TItems.CINDERED_BOW, 5F / 100F);
        addLootEntry("minecraft:slime", Terminus.TItems.SLIMEY_BOW, 5F / 100F);
        addLootEntry("minecraft:pillager", Terminus.TItems.RAIDERS_AXE, 2.5F / 100F);
        addLootEntry("minecraft:zombie", Terminus.TItems.ESCAPE_PLAN, 0.1F / 100F);
        addLootEntry("minecraft:skeleton", Terminus.TItems.ESCAPE_PLAN, 0.1F / 100F);
        addLootEntry("minecraft:creeper", Terminus.TItems.ESCAPE_PLAN, 0.1F / 100F);
        addLootEntry("minecraft:spider", Terminus.TItems.ESCAPE_PLAN, 0.1F / 100F);
        addLootEntry("minecraft:husk", Terminus.TItems.ARCHAEOLOGIST_SPADE, 5F / 100F);
        addLootEntry("minecraft:cave_spider", Terminus.TItems.RUSTY_SHANK, 2.5F / 100F);
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance) {
        LOOT_ENTRIES.add(new LootEntry(new Identifier(id), item, chance));
    }

    @Override
    public void onLootTableLoading(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableSetter lootTableSetter) {
        for (LootEntry lootEntry : LOOT_ENTRIES) {
            if (lootEntry.id.equals(id)) {
                // Builder created with 2 conditions: random chance and player kill required
                FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(lootEntry.chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
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

        public LootEntry(Identifier id, ItemConvertible item, float chance) {
            this.id = id;
            this.item = item;
            this.chance = chance;
        }
    }
}
