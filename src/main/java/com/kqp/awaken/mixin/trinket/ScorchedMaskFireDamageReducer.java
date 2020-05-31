package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to apply the fire damage reducing effect of the scorched mask.
 */
@Mixin(LivingEntity.class)
public abstract class ScorchedMaskFireDamageReducer {
    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
    private float applyScorchedMaskEffect(float amount, DamageSource source, float amount2) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (source.isFire() && AwakenAbilities.SCORCHED_MASK_EFFECT.get(player).flag) {
                amount *= 0.25F;
            }
        }

        return amount;
    }
}
