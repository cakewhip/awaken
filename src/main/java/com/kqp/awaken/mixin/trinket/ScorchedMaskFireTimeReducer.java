package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to apply fire time reducing effect of the scorched mask.
 */
@Mixin(Entity.class)
public class ScorchedMaskFireTimeReducer {
    @ModifyVariable(method = "setOnFireFor", name = "i", at = @At(value = "STORE", ordinal = 0))
    private int applyScorchedMaskEffect(int i) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (AwakenAbilities.SCORCHED_MASK_EFFECT.get(player).flag) {
                i *= 0.25;
            }
        }

        return i;
    }
}
