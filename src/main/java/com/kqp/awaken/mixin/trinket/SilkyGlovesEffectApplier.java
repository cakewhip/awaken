package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to apply the silky glove effect.
 */
@Mixin(LivingEntity.class)
public abstract class SilkyGlovesEffectApplier {
    @Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
    private void applySilkyGloveEffect(CallbackInfoReturnable<Boolean> callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (!player.isSpectator()) {
                if (player.horizontalCollision) {
                    if (AwakenAbilities.SILKY_GLOVE_EFFECT.get(player).flag) {
                        callbackInfo.setReturnValue(true);
                    }
                }
            }
        }
    }
}
