package com.kqp.terminus.mixin;

import com.kqp.terminus.client.entity.RaptorChickenRenderer;
import com.kqp.terminus.entity.RaptorChickenEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to animate the raptor chicken wings.
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(at = @At("HEAD"), method = "getAnimationProgress", cancellable = true)
    public void addRaptorChickenWingFlap(T entity, float tickDelta, CallbackInfoReturnable<Float> callbackInfo) {
        if (entity instanceof RaptorChickenEntity) {
            RaptorChickenEntity raptorChicken = (RaptorChickenEntity) entity;
            callbackInfo.setReturnValue(((RaptorChickenRenderer) (Object) this).getAnimationProgress(raptorChicken, tickDelta));
        }
    }
}


