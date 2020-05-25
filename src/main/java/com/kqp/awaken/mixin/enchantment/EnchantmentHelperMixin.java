package com.kqp.awaken.mixin.enchantment;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Used to:
 * Apply the guardian and glacial aglet effects.
 * Apply the lucky tackle effect.
 * Apply the snorkel mask effect.
 * Apply the anchor effect.
 */
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getEquipmentLevel", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void applyTrinketEffects(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> callbackInfo,
                                            Iterable<ItemStack> _, int i) {
        if (enchantment == Enchantments.DEPTH_STRIDER && TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.GUARDIAN_AGLET)) {
            callbackInfo.setReturnValue(Math.max(i, 2));
        } else if (enchantment == Enchantments.FROST_WALKER && TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.GLACIAL_AGLET)) {
            callbackInfo.setReturnValue(Math.max(i, 1));
        } else if (enchantment == Enchantments.LUCK_OF_THE_SEA) {
            callbackInfo.setReturnValue(i + 1);
        } else if (enchantment == Enchantments.RESPIRATION && TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.SNORKEL_MASK)) {
            callbackInfo.setReturnValue(i + 1);
        } else if (enchantment == Enchantments.LURE && TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.ANCHOR)) {
            callbackInfo.setReturnValue(i + 1);
        }
    }
}
