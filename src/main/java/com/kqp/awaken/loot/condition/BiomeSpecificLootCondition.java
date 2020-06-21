package com.kqp.awaken.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kqp.awaken.init.AwakenLootTable;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;
import java.util.Set;

/**
 * Loot condition for killing mobs within a specific set of biomes.
 * See {@link net.minecraft.world.biome.Biomes} for vanilla biome IDs.
 */
public class BiomeSpecificLootCondition implements LootCondition {
    private final HashSet<Identifier> biomes;

    private BiomeSpecificLootCondition(HashSet<Identifier> biomes) {
        this.biomes = biomes;
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
        Biome biome = lootContext.getWorld().getBiome(entity.getBlockPos());
        Identifier biomeId = Registry.BIOME.getId(biome);

        return biomes.contains(biomeId);
    }

    public HashSet<Identifier> getBiomes() {
        return this.biomes;
    }

    public static LootCondition.Builder create(Identifier... biomeIds) {
        return () -> {
            HashSet<Identifier> biomeSet = new HashSet();

            for (Identifier biomeId : biomeIds) {
                biomeSet.add(biomeId);
            }

            return new BiomeSpecificLootCondition(biomeSet);
        };
    }

    @Override
    public LootConditionType getType() {
        return AwakenLootTable.BIOME_SPECIFIC_CONDITION;
    }

    public static class Serializer implements JsonSerializer<BiomeSpecificLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, BiomeSpecificLootCondition biomeSpecificLootCondition, JsonSerializationContext jsonSerializationContext) {
            JsonArray biomeArray = new JsonArray();

            for (Identifier biomeId : biomeSpecificLootCondition.getBiomes()) {
                biomeArray.add(biomeId.toString());
            }

            jsonObject.add("biomes", biomeArray);
        }

        @Override
        public BiomeSpecificLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            HashSet<Identifier> biomes = new HashSet();

            JsonArray jsonBiomeArray = jsonObject.getAsJsonArray("biomes");

            jsonBiomeArray.forEach(element -> {
                biomes.add(new Identifier(element.getAsString()));
            });

            return new BiomeSpecificLootCondition(biomes);
        }
    }
}
