package com.kqp.awaken.loot.condition;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;

import java.util.Set;

public class FieryMoonLootCondition implements LootCondition {
    private static final FieryMoonLootCondition INSTANCE = new FieryMoonLootCondition();

    private FieryMoonLootCondition() {
    }

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

    public static class Factory extends LootCondition.Factory<FieryMoonLootCondition> {
        public Factory() {
            super(new Identifier("fiery_moon_active"), FieryMoonLootCondition.class);
        }

        public void toJson(JsonObject jsonObject, FieryMoonLootCondition fieryMoonLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        public FieryMoonLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return FieryMoonLootCondition.INSTANCE;
        }
    }
}
