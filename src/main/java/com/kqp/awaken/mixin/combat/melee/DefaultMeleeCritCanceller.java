package com.kqp.awaken.mixin.combat.melee;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to cancel the default melee crit check.
 */
@Mixin(PlayerEntity.class)
public class DefaultMeleeCritCanceller {
    /**
     * "b13" is the flag for determining whether the attack is critical
     *
     * @param b13
     * @return
     */
    @ModifyVariable(
            method = "attack",
            at = @At(value = "STORE"),
            index = 8,
            ordinal = 2,
            name = "b13"
    )
    private boolean cancelCritFlag(boolean b13) {
        return false;
    }
}
