package com.kqp.awaken.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonSerializer;

import java.util.Set;

public class FieryMoonLootCondition implements LootCondition {
    private static final FieryMoonLootCondition INSTANCE = new FieryMoonLootCondition();

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext lootContext) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(lootContext.getWorld().getServer());

        return awakenLevelData.isFieryMoonActive();
    }

    public static LootCondition.Builder builder() {
        return () -> INSTANCE;
    }

    @Override
    public LootConditionType getType() {
        return null;
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
