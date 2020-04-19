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

public class WorldAwakenedLootCondition implements LootCondition {
    private static final WorldAwakenedLootCondition INSTANCE = new WorldAwakenedLootCondition();

    private WorldAwakenedLootCondition() {
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.LAST_DAMAGE_PLAYER);
    }

    public boolean test(LootContext lootContext) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(lootContext.getWorld());

        return awakenLevelData.isWorldAwakened();
    }

    public static Builder builder() {
        return () -> INSTANCE;
    }

    public static class Factory extends LootCondition.Factory<WorldAwakenedLootCondition> {
        public Factory() {
            super(new Identifier("world_awakened"), WorldAwakenedLootCondition.class);
        }

        public void toJson(JsonObject jsonObject, WorldAwakenedLootCondition bloodMoonLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        public WorldAwakenedLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return WorldAwakenedLootCondition.INSTANCE;
        }
    }
}
