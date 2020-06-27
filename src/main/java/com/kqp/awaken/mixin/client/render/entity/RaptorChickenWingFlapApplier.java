package com.kqp.awaken.mixin.client.render.entity;

import com.kqp.awaken.client.entity.RaptorChickenRenderer;
import com.kqp.awaken.entity.mob.RaptorChickenEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to animate the raptor chicken trinket.
 */
@Mixin(LivingEntityRenderer.class)
public class RaptorChickenWingFlapApplier<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(method = "getAnimationProgress", at = @At("HEAD"), cancellable = true)
    public void addRaptorChickenWingFlap(T entity, float tickDelta, CallbackInfoReturnable<Float> callbackInfo) {
        if (entity instanceof RaptorChickenEntity) {
            RaptorChickenEntity raptorChicken = (RaptorChickenEntity) entity;
            callbackInfo.setReturnValue(((RaptorChickenRenderer) (Object) this).getAnimationProgress(raptorChicken, tickDelta));
        }
    }
}


