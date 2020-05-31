package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to apply combat saddle effect.
 */
@Mixin(PlayerEntity.class)
public class CombatSaddleEffectApplier {
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0))
    private float applyUnarmedDamage(float f) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Apply combat saddle effect
        if (player.hasVehicle()) {
            EntityAttributeInstance mountedDamageAttrib =
                    player.getAttributeInstance(AwakenEntityAttributes.MOUNTED_DAMAGE);

            mountedDamageAttrib.setBaseValue(f);
            f = (float) mountedDamageAttrib.getValue();
        }

        return f;
    }
}
