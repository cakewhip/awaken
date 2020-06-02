package com.kqp.awaken.mixin.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to force sweeping edge.
 */
@Mixin(PlayerEntity.class)
public class SweepingEdgeForcer {
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0))
    private boolean forceSweepingEdge(boolean b14) {
        return ((PlayerEntity) (Object) this).getStackInHand(Hand.MAIN_HAND).getItem() instanceof SwordItem;
    }
}
