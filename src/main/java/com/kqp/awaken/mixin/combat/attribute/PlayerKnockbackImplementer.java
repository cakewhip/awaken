package com.kqp.awaken.mixin.combat.attribute;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Implements Mojang's attack knockback entity attribute for players.
 */
@Mixin(PlayerEntity.class)
public class PlayerKnockbackImplementer {
    /**
     * It's not actually registered when players are created.
     * Crazy, right?
     *
     * @param callbackInfoReturnable
     */
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void addCustomAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> callbackInfoReturnable) {
        callbackInfoReturnable.getReturnValue().add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    /**
     * Redirects knockback calls to use the attribute instance value.
     *
     * @param target
     * @param f
     * @param d
     * @param e
     */
    @Redirect(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(FDD)V")
    )
    private void applyKnockbackAttribute(LivingEntity target, float f, double d, double e) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        EntityAttributeInstance knockbackInstance = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        double old = knockbackInstance.getBaseValue();

        knockbackInstance.setBaseValue(0.4);
        target.takeKnockback((float) knockbackInstance.getValue(), d, e);
        knockbackInstance.setBaseValue(old);
    }

    /**
     * Knock back is usually only added if the weapon is enchanted with knock back 1.
     * Here, I bypass the "x > 0" conditional by lowering the 0 constant to -1.
     *
     * @param j
     * @return
     */
    @ModifyConstant(
            method = "attack",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V")
            ),
            constant = @Constant(
                    intValue = 0,
                    expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO
            )
    )
    private int bypassKnockbackEnchantmentConditional(int j) {
        return -1;
    }

    /**
     * Sometimes it cancels sprinting even when the player isn't sprinting, causing a graphical hiccup.
     * This is resolved by only cancelling sprinting when the player is actually sprinting.
     *
     * @param set
     */
    @Redirect(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V")
    )
    private void cancelSprintSet(PlayerEntity player, boolean set) {
        if (player.isSprinting() != set) {
            player.setSprinting(set);
        }
    }
}
