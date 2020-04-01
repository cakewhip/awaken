package com.kqp.awaken.mixin;

import net.minecraft.entity.attribute.AbstractEntityAttribute;
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
public abstract class ClampedEntityAttributeMixin extends AbstractEntityAttribute {
    protected ClampedEntityAttributeMixin(EntityAttribute parent, String id, double defaultValue) {
        super(parent, id, defaultValue);
    }

    @Inject(at = @At("HEAD"), method = "clamp", cancellable = true)
    private void overrideClamp(double value, CallbackInfoReturnable callbackInfo) {
        ClampedEntityAttribute attribute = (ClampedEntityAttribute) (Object) this;
        String id = attribute.getId();

        if (id.equals("generic.maxHealth") || id.equals("generic.armor")) {
            callbackInfo.setReturnValue(value);
        } else if (id.equals("generic.armorToughness")) {
            callbackInfo.setReturnValue(MathHelper.clamp(value, 0 ,100));
        }
    }
}
