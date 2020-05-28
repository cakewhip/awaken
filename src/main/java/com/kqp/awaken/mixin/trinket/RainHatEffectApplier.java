package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Used to apply the rain hat effect, which is also used by the angler's tackle box.
 */
@Mixin(FishingBobberEntity.class)
public class RainHatEffectApplier {
    @ModifyVariable(method = "tickFishingLogic", at = @At(value = "STORE", ordinal = 0))
    private int applyRainHatEffect(int i) {
        PlayerEntity owner = ((FishingBobberEntity) (Object) this).getOwner();

        if (TrinketUtil.hasAnyTrinkets(owner, AwakenItems.Trinkets.RAIN_HAT, AwakenItems.Trinkets.ANGLERS_TACKLE_BOX)) {
            return i + 1;
        }

        return i;
    }
}
