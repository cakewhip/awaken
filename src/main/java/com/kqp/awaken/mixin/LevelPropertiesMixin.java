package com.kqp.awaken.mixin;

import com.kqp.awaken.data.AwakenWorldData;
import com.kqp.awaken.data.AwakenWorldDataProvider;
import com.kqp.awaken.init.Awaken;
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
@Implements(@Interface(iface = AwakenWorldDataProvider.class, prefix = "vw$"))
public class LevelPropertiesMixin implements AwakenWorldDataProvider {
    private AwakenWorldData awakenWorldData;

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;Lcom/mojang/datafixers/DataFixer;ILnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void loadAwakenData(CompoundTag tag, DataFixer dataFixer, int dataVersion, @Nullable CompoundTag playerData, CallbackInfo callbackInfo) {
        CompoundTag awakenWorldDataTag = tag.getCompound("AwakenWorldData");
        awakenWorldData = new AwakenWorldData(awakenWorldDataTag);
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void writeAwakenData(CompoundTag levelTag, CompoundTag playerTag, CallbackInfo callbackInfo) {
        CompoundTag awakenWorldDataTag = new CompoundTag();
        awakenWorldData.writeToTag(awakenWorldDataTag);
        levelTag.put("AwakenWorldData", awakenWorldDataTag);
    }

    @Override
    public AwakenWorldData getAwakenWorldData() {
        return awakenWorldData;
    }
}
