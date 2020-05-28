package com.kqp.awaken.mixin.piglin;

import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to make piglins admire the ender dragon head.
 */
@Mixin(PiglinEntity.class)
public class EnderDragonHeadAdmirationApplier {
    @Inject(method = "getActivity", at = @At("HEAD"), cancellable = true)
    private void addCorsairTokenAdmiration(CallbackInfoReturnable<PiglinEntity.Activity> callbackInfoReturnable) {
        PiglinEntity piglin = (PiglinEntity) (Object) this;

        if (piglin.getOffHandStack().getItem() == Items.DRAGON_HEAD) {
            callbackInfoReturnable.setReturnValue(PiglinEntity.Activity.ADMIRING_ITEM);
        }
    }
}
