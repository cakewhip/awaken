package com.kqp.awaken.world.spawning;

import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;
import java.util.Set;

public class ConditionalSpawnEntry extends Biome.SpawnEntry {
    private final Set<SpawnCondition> spawnConditions;

    public ConditionalSpawnEntry(EntityType<?> type, int weight, int minGroupSize, int maxGroupSize) {
        super(type, weight, minGroupSize, maxGroupSize);

        this.spawnConditions = new HashSet();
    }

    public ConditionalSpawnEntry addCondition(SpawnCondition spawnCondition) {
        this.spawnConditions.add(spawnCondition);

        return this;
    }

    public Set<SpawnCondition> getSpawnConditions() {
        return this.spawnConditions;
    }
}
