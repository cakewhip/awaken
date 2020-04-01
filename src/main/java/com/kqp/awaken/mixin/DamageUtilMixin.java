package com.kqp.awaken.mixin;

import net.minecraft.entity.DamageUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override the vanilla armor damage calculation.
 */
@Mixin(DamageUtil.class)
public class DamageUtilMixin {
    @Inject(at = @At("HEAD"), method = "getDamageLeft", cancellable = true)
    private static void overrideArmorDamageMitigationCalculation(float damage, float armor, float armorToughness, CallbackInfoReturnable<Float> callbackInfo) {
        // Each point of armor decreases damage by 0.32F
        float newDamage = damage - armor * 0.32F;

        // Each point of armor toughness decreases damage by 1%
        newDamage *= 1F - (armorToughness / 100F);

        callbackInfo.setReturnValue(Math.max(0.05F, newDamage));
    }
}
