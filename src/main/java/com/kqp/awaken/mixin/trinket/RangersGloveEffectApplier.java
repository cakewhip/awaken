package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the ranger's glove effect.
 */
@Mixin(BowItem.class)
public class RangersGloveEffectApplier {
    /**
     * TODO: figure out why the cancel causes a visual error
     */
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void applyRangersGloveEffect(ItemStack arrowStack, int amt,
                                         ItemStack bowStack, World world, LivingEntity user, int remainingUseTicks) {
        boolean consume = true;

        if (user instanceof PlayerEntity) {
            if (user.getRandom().nextFloat() <
                    user.getAttributeInstance(AwakenEntityAttributes.AMMO_CONSUMPTION).getValue()) {
                consume = false;
            }
        }

        if (consume) {
            arrowStack.decrement(amt);
        }
    }
}
