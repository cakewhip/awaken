package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
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
        if (player.hasVehicle() && EquipmentUtil.hasTrinket(player, AwakenItems.Trinkets.COMBAT_SADDLE)) {
            f *= 1.04;
        }

        return f;
    }
}
