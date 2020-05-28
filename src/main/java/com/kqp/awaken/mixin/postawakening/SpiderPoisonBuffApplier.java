package com.kqp.awaken.mixin.postawakening;

import com.kqp.awaken.data.AwakenLevelData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to give spider's poison damage post-awakening.
 */
@Mixin(LivingEntity.class)
public abstract class SpiderPoisonBuffApplier {
    @Inject(method = "damage", at = @At("RETURN"))
    public void applySpiderPoison(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // Check if damage is applied
        if (callbackInfoReturnable.getReturnValue()) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(livingEntity.world.getServer());

            // Apply spider poison
            if (awakenLevelData.isWorldAwakened()) {
                if (source.getAttacker() instanceof SpiderEntity) {

                    if (livingEntity.getRandom().nextFloat() < 0.5F) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 3 * 20, 0));
                    }
                }
            }
        }
    }
}
