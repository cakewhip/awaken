package com.kqp.awaken.mixin.attribute;

import com.kqp.awaken.entity.attribute.ImmunityTimeProvider;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Applies the dodge chance attribute.
 * Every time damage is attempted to be applied, a roll is made
 * to determine if the player dodged the attack. If successful,
 * the player's attack cooldowns are reset.
 */
@Mixin(LivingEntity.class)
public class DodgeChanceAttributeApplier {
    @Shadow
    protected float lastDamageTaken;

    @Inject(
            method = "damage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"),
            cancellable = true
    )
    private void applyDodgeAttribute(DamageSource source,
                                     float amount,
                                     CallbackInfoReturnable<Boolean> callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) (Object) this);

            if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) {
                EntityAttributeInstance dodgeAttribute = player.getAttributeInstance(AwakenEntityAttributes.DODGE_CHANCE);

                if (AttributeUtil.rollAttribute(dodgeAttribute, player.getRandom())) {
                    callbackInfo.setReturnValue(false);
                    System.out.println("DODGED!");

                    lastDamageTaken = amount;
                    player.timeUntilRegen = ((ImmunityTimeProvider) player).getImmunityTime();
                    player.maxHurtTime = 10;
                    player.hurtTime = player.maxHurtTime;

                    callbackInfo.cancel();
                }
            }
        }
    }
}
