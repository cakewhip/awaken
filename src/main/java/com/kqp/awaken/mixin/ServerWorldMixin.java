package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.data.trigger.SleepTrigger;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(at = @At("HEAD"), method = "method_23660")
    public void wakePlayers(CallbackInfo callbackInfo) {
        Awaken.worldProperties.triggers.forEach(trigger -> {
            if (trigger instanceof SleepTrigger) {
                trigger.activate();
            }
        });
    }
}
