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

import java.util.Set;

public class WorldAwakenedLootCondition implements LootCondition {
    private static final WorldAwakenedLootCondition INSTANCE = new WorldAwakenedLootCondition();

    private WorldAwakenedLootCondition() {
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    @Override
    public boolean test(LootContext lootContext) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(lootContext.getWorld().getServer());

        return awakenLevelData.isWorldAwakened();
    }

    public static Builder builder() {
        return () -> INSTANCE;
    }

    @Override
    public LootConditionType getType() {
        return AwakenLootTable.WORLD_AWAKENED_CONDITION;
    }

    public static class Serializer implements JsonSerializer<WorldAwakenedLootCondition> {
        @Override
        public void toJson(JsonObject jsonObject, WorldAwakenedLootCondition fieryMoonLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        @Override
        public WorldAwakenedLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return WorldAwakenedLootCondition.INSTANCE;
        }
    }
}
