package com.kqp.awaken.mixin.effect;

import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used to apply trinket enchantment modifiers.
 */
@Mixin(EnchantmentHelper.class)
public class TrinketEFGEnchantmentApplier {
    @Inject(method = "getEquipmentLevel", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void applyTrinketEffects(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> callbackInfo,
                                            Iterable<ItemStack> _, int i) {
        if (entity instanceof PlayerEntity) {
            final AtomicInteger newLevel = new AtomicInteger(i);

            EquipmentUtil.getAllEntityFeatureGroupProviders((PlayerEntity) entity).forEach(features -> {
                newLevel.set(newLevel.get() + features.getEnchantmentModifiers().getOrDefault(enchantment, 0));
            });

            i = newLevel.get();
        }

        callbackInfo.setReturnValue(i);
    }
}
