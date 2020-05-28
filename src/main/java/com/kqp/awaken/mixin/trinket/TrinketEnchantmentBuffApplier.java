package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.item.trinket.AwakenTrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
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
public class TrinketEnchantmentBuffApplier {
    @Inject(method = "getEquipmentLevel", at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void applyTrinketEffects(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> callbackInfo,
                                            Iterable<ItemStack> _, int i) {
        if (entity instanceof PlayerEntity) {
            final AtomicInteger newLevel = new AtomicInteger(i);
            Inventory trinkets = TrinketsApi.getTrinketsInventory((PlayerEntity) entity);

            for (int j = 0; j < trinkets.size(); j++) {
                ItemStack itemStack = trinkets.getStack(j);

                if (itemStack.getItem() instanceof AwakenTrinketItem) {
                    AwakenTrinketItem trinket = (AwakenTrinketItem) itemStack.getItem();

                    trinket.getEntityFeatures().ifPresent(features -> {
                        newLevel.set(newLevel.get() + features.getEnchantmentModifiers().getOrDefault(enchantment, 0));
                    });
                }
            }

            i = newLevel.get();
        }

        callbackInfo.setReturnValue(i);
    }
}
