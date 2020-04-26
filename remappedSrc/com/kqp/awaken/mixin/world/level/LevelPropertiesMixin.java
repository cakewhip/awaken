package com.kqp.awaken.mixin.world.level;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.data.AwakenLevelDataContainer;
import com.mojang.datafixers.DataFixer;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
@Implements(@Interface(iface = AwakenLevelDataContainer.class, prefix = "vw$"))
public class LevelPropertiesMixin implements AwakenLevelDataContainer {
    private AwakenLevelData awakenLevelData = new AwakenLevelData();

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;Lcom/mojang/datafixers/DataFixer;ILnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void loadAwakenData(CompoundTag tag, DataFixer dataFixer, int dataVersion, @Nullable CompoundTag playerData, CallbackInfo callbackInfo) {
        CompoundTag awakenWorldDataTag = tag.getCompound("AwakenLevelData");
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
