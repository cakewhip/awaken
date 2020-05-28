package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to add custom entity attributes.
 */
@Mixin(PlayerEntity.class)
public class CustomEntityAttributeAdder {
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void addCustomAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> callbackInfoReturnable) {
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.RANGED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.BOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.CROSSBOW_DAMAGE);

        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.MELEE_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.SWORD_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.AXE_DAMAGE);

        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.UNARMED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.POTION_DAMAGE);
    }
}
