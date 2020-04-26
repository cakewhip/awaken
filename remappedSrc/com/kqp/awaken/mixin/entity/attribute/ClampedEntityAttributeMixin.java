package com.kqp.awaken.mixin.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to remove the hard-coded health and armor limits.
 */
@Mixin(ClampedEntityAttribute.class)
public abstract class ClampedEntityAttributeMixin extends EntityAttribute {
    protected ClampedEntityAttributeMixin(String translationKey, double fallback) {
        super(translationKey, fallback);
    }

    @Inject(method = "clamp", at = @At("HEAD"), cancellable = true)
    private void overrideClamp(double value, CallbackInfoReturnable callbackInfo) {
        ClampedEntityAttribute attribute = (ClampedEntityAttribute) (Object) this;
        String id = this.getTranslationKey();

        if (id.equals("generic.max_health") || id.equals("generic.armor")) {
            callbackInfo.setReturnValue(value);
        } else if (id.equals("generic.armor_toughness")) {
            callbackInfo.setReturnValue(MathHelper.clamp(value, 0, 100));
        }
    }
}
