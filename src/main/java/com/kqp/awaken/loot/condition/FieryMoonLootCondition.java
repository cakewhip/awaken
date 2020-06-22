package com.kqp.awaken.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kqp.awaken.init.AwakenLootTable;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;
import net.minecraft.world.dimension.DimensionType;

import java.util.Set;

public class FieryMoonLootCondition implements LootCondition {
    private static final FieryMoonLootCondition INSTANCE = new FieryMoonLootCondition();

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.THIS_ENTITY);
    }

    public boolean test(LootContext lootContext) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(lootContext.getWorld().getServer());
        boolean inOverworld = lootContext.get(LootContextParameters.THIS_ENTITY).world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY;

        return awakenLevelData.isFieryMoonActive() && inOverworld;
    }

    public static LootCondition.Builder builder() {
        return () -> INSTANCE;
    }

    @Override
    public LootConditionType getType() {
        return AwakenLootTable.FIERY_MOON_CONDITION;
    }

    public static class Serializer implements JsonSerializer<FieryMoonLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, FieryMoonLootCondition fieryMoonLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        @Override
        public FieryMoonLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return FieryMoonLootCondition.INSTANCE;
        }
    }
}
