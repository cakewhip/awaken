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
        AwakenEntityAttributes.NEW_ATTRIBUTES.forEach(callbackInfoReturnable.getReturnValue()::add);
    }
}
