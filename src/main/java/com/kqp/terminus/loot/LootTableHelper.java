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

public class LootTableHelper implements LootTableLoadingCallback {
    public static HashSet<LootEntry> LOOT_ENTRIES = new HashSet();

    public static void init() {
        addLootEntry("minecraft:guardian", Terminus.TItems.ATLANTEAN_SABRE, 0.0025F);
        addLootEntry("minecraft:elder_guardian", Terminus.TItems.ATLANTEAN_SABRE, 0.005F);
        addLootEntry("minecraft:drowned", Terminus.TItems.ATLANTEAN_SABRE, 0.0025F);
        addLootEntry("minecraft:wither_skeleton", Terminus.TItems.ASHEN_BLADE, 0.005F);
        addLootEntry("minecraft:wither", Terminus.TItems.ASHEN_BLADE, 0.25F);
        addLootEntry("minecraft:enderman", Terminus.TItems.ENDERIAN_CUTLASS, 0.001F);
        addLootEntry("minecraft:shulker", Terminus.TItems.ENDERIAN_CUTLASS, 0.005F);
        addLootEntry("minecraft:endermite", Terminus.TItems.ENDERIAN_CUTLASS, 0.01F);
    }

    public static void addLootEntry(Identifier id, ItemConvertible item, float chance) {
        LOOT_ENTRIES.add(new LootEntry(id, item, chance));
    }

    public static void addLootEntry(String id, ItemConvertible item, float chance) {
        addLootEntry(new Identifier(id), item, chance);
    }

    @Override
    public void onLootTableLoading(ResourceManager resourceManager, LootManager lootManager, Identifier id, FabricLootSupplierBuilder supplier, LootTableSetter lootTableSetter) {
        for (LootEntry lootEntry : LOOT_ENTRIES) {
            if (lootEntry.id.equals(id)) {
                FabricLootPoolBuilder builder = FabricLootPoolBuilder.builder()
                        .withRolls(ConstantLootTableRange.create(1))
                        .withCondition(RandomChanceLootCondition.builder(lootEntry.chance))
                        .withCondition(KilledByPlayerLootCondition.builder())
                        .withEntry(ItemEntry.builder(lootEntry.item));

                supplier.withPool(builder);
            }
        }
    }

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
