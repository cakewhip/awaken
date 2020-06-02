package com.kqp.awaken.mixin.combat;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to cancel the default melee crit check.
 */
@Mixin(PlayerEntity.class)
public class MeleeCritCanceller {
    @ModifyVariable(method = "attack", at = @At(value = "STORE"))
    private boolean cancelCritFlag(boolean b13) {
        return false;
    }
}
