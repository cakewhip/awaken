package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to apply the shulker charm effect, which is also used by the shulker glove.
 */
@Mixin(LivingEntity.class)
public abstract class ShulkerCharmEffectApplier {
    @Inject(method = "damage", at = @At("RETURN"))
    public void applyShulkerCharm(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // Check if damage is applied
        if (callbackInfoReturnable.getReturnValue()) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;

            if (source.getAttacker() instanceof PlayerEntity) {
                if (AwakenAbilities.SHULKER_CHARM_EFFECT.get((PlayerEntity) source.getAttacker()).flag) {
                    if (source.isProjectile()) {
                        if (livingEntity.getRandom().nextFloat() < 0.15F) {
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3 * 20, 0));
                        }
                    }
                }
            }
        }
    }
}
