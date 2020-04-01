package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.data.AwakenWorldData;
import com.kqp.awaken.util.JsonUtil;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PlayerSaveHandler;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

/**
 * Used to save Awaken world data.
 */
@Mixin(WorldSaveHandler.class)
public abstract class WorldSaveHandlerMixin implements PlayerSaveHandler {
    @Shadow
    @Final
    private File worldDir;

    @Inject(at = @At("HEAD"), method = "saveWorld")
    public void saveWorld(LevelProperties levelProperties, @Nullable CompoundTag compoundTag, CallbackInfo callbackInfo) {
        AwakenWorldData toSave = Awaken.worldProperties != null ? Awaken.worldProperties : new AwakenWorldData();
        JsonUtil.save(new File(worldDir, AwakenWorldData.FILE_NAME), toSave);
    }

    @Inject(at = @At("HEAD"), method = "readProperties")
    public void readProperties(CallbackInfoReturnable<LevelProperties> callbackInfo) {
        Awaken.worldProperties = JsonUtil.load(
                new File(worldDir, AwakenWorldData.FILE_NAME),
                AwakenWorldData.class,
                new AwakenWorldData()
        );
    }
}
