package com.kqp.awaken.mixin.world.data;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.mojang.datafixers.DataFixer;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.timer.Timer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(LevelProperties.class)
public class AwakenLevelDataImplementer implements AwakenLevelDataContainer {
    private AwakenLevelData awakenLevelData = new AwakenLevelData();

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;Lcom/mojang/datafixers/DataFixer;ILnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void loadAwakenData(DataFixer dataFixer,
                                int dataVersion,
                                CompoundTag playerData,
                                boolean modded,
                                int spawnX,
                                int spawnY,
                                int spawnZ,
                                long time,
                                long timeOfDay,
                                int version,
                                int clearWeatherTime,
                                int rainTime,
                                boolean raining,
                                int thunderTime,
                                boolean thundering,
                                boolean initialized,
                                boolean difficultyLocked,
                                WorldBorder.Properties worldBorder,
                                int wanderingTraderSpawnDelay,
                                int wanderingTraderSpawnChance,
                                @Nullable UUID wanderingTraderId,
                                LinkedHashSet<String> serverBrands,
                                LinkedHashSet<String> enabledDataPacks,
                                Set<String> disabledDataPacks,
                                Timer<MinecraftServer> scheduledEvents,
                                @Nullable CompoundTag customBossEvents,
                                CompoundTag compoundTag,
                                LevelInfo levelInfo,
                                CallbackInfo callbackInfo) {
        CompoundTag awakenWorldDataTag = compoundTag.getCompound("AwakenLevelData");
        awakenLevelData = new AwakenLevelData(awakenWorldDataTag);
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void writeAwakenData(CompoundTag levelTag, CompoundTag playerTag, CallbackInfo callbackInfo) {
        CompoundTag awakenWorldDataTag = new CompoundTag();
        awakenLevelData.writeToTag(awakenWorldDataTag);
        levelTag.put("AwakenLevelData", awakenWorldDataTag);
    }

    @Override
    public AwakenLevelData getAwakenLevelData() {
        return awakenLevelData;
    }

    @Override
    public void setAwakenServerLevelData(AwakenLevelData awakenLevelData) {
        this.awakenLevelData = awakenLevelData;
    }
}
