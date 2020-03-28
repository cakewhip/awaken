package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.JsonUtil;
import com.kqp.terminus.data.TerminusWorldProperties;
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
 * Used to save Terminus world data.
 */
@Mixin(WorldSaveHandler.class)
public abstract class WorldSaveHandlerMixin implements PlayerSaveHandler {
    @Shadow
    @Final
    private File worldDir;

    @Inject(at = @At("HEAD"), method = "saveWorld")
    public void saveWorld(LevelProperties levelProperties, @Nullable CompoundTag compoundTag, CallbackInfo callbackInfo) {
        TerminusWorldProperties toSave = Terminus.worldProperties != null ? Terminus.worldProperties : new TerminusWorldProperties();
        JsonUtil.save(new File(worldDir, TerminusWorldProperties.FILE_NAME), toSave);
    }

    @Inject(at = @At("HEAD"), method = "readProperties")
    public void readProperties(CallbackInfoReturnable<LevelProperties> callbackInfo) {
        Terminus.worldProperties = JsonUtil.load(
                new File(worldDir, TerminusWorldProperties.FILE_NAME),
                TerminusWorldProperties.class,
                new TerminusWorldProperties()
        );
    }
}
