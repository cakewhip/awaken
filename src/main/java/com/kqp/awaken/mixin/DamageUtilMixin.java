package com.kqp.awaken.mixin;

import net.minecraft.entity.DamageUtil;
import net.minecraft.util.math.MathHelper;
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
        // Each point of armor (with a bonus from armor toughness) subtracts damage
        float newDamage = damage - armor * (0.173469387755102F + armorToughness / 100F);

        // Each point of armor toughness decreases damage by 0.1%
        newDamage *= 1F - (armorToughness * 0.1F / 100F);

        // Calculate a minimum damage so the player takes some damage, also affected by armor toughness
        float minDamage = 1F - (clampPercentage(armorToughness) * 0.5F / 100F);

        callbackInfo.setReturnValue(Math.max(minDamage, newDamage));
    }

    @Inject(at = @At("HEAD"), method = "getDamageInflicted", cancellable = true)
    private static void overrideArmorEnchantmentDamageMitigationCalculation(float damage, float protection, CallbackInfoReturnable<Float> callbackInfo) {
        // Each level of protection adds a 0.5% damage mitigation
        float multiplier = 1F - (0.5F * clampPercentage(protection) / 16F);

        callbackInfo.setReturnValue(damage * multiplier);
    }

    private static float clampPercentage(float value) {
        return MathHelper.clamp(value, 0F, 100F);
    }
}
