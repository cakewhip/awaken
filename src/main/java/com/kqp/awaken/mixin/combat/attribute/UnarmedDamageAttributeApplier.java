package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to apply unarmed damage attribute.
 */
@Mixin(PlayerEntity.class)
public class UnarmedDamageAttributeApplier {
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0))
    private float applyUnarmedDamage(float f) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Apply unarmed attribute
        if (player.getMainHandStack().isEmpty()) {
            EntityAttributeInstance unarmedAttribute = player.getAttributeInstance(AwakenEntityAttributes.UNARMED_DAMAGE);
            unarmedAttribute.setBaseValue(f);

            f = (float) unarmedAttribute.getValue();
        }

        return f;
    }
}
