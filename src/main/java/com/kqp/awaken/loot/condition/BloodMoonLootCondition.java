package com.kqp.awaken.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kqp.awaken.data.AwakenLevelData;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;

import java.util.Set;

public class BloodMoonLootCondition implements LootCondition {
    private static final BloodMoonLootCondition INSTANCE = new BloodMoonLootCondition();

    private BloodMoonLootCondition() {
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext lootContext) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(lootContext.getWorld());

        return awakenLevelData.isBloodMoonActive();
    }

    public static LootCondition.Builder builder() {
        return () -> INSTANCE;
    }

    public static class Factory extends LootCondition.Factory<BloodMoonLootCondition> {
        public Factory() {
            super(new Identifier("blood_moon_active"), BloodMoonLootCondition.class);
        }

        public void toJson(JsonObject jsonObject, BloodMoonLootCondition bloodMoonLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        public BloodMoonLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return BloodMoonLootCondition.INSTANCE;
        }
    }
}
