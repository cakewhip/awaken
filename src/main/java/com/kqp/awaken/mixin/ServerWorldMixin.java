package com.kqp.awaken.mixin;

import com.kqp.awaken.data.trigger.SleepTrigger;
import com.kqp.awaken.init.Awaken;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to trigger sleep events.
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(at = @At("HEAD"), method = "wakeSleepingPlayers")
    public void wakePlayers(CallbackInfo callbackInfo) {
        Awaken.worldProperties.triggers.forEach(trigger -> {
            if (trigger instanceof SleepTrigger) {
                trigger.activate();
            }
        });
    }
}
