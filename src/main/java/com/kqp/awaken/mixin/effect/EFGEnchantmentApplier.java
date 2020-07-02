package com.kqp.awaken.mixin.effect;

import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to trick EnchantmentHelper into thinking that EFG items are enchanted.
 */
@Mixin(EnchantmentHelper.class)
public class EFGEnchantmentApplier {
    @Inject(
            method = "onTargetDamaged",
            at = @At("HEAD")
    )
    private static void applyEFGEnchantmentsOnTargetDamaged(LivingEntity user, Entity target, CallbackInfo callbackInfo) {
        if (user instanceof PlayerEntity) {
            Consumer consumer = (enchantment, i) -> enchantment.onTargetDamaged(user, target, i);
            PlayerEntity player = (PlayerEntity) user;

            applyEnchants(consumer, player);
        }
    }

    @Inject(
            method = "onUserDamaged",
            at = @At("HEAD")
    )
    private static void applyEFGEnchantmentsOnUserDamaged(LivingEntity user, Entity attacker, CallbackInfo callbackInfo) {
        if (user instanceof PlayerEntity) {
            Consumer consumer = (enchantment, i) -> {
                enchantment.onUserDamaged(user, attacker, i);
            };
            PlayerEntity player = (PlayerEntity) user;

            applyEnchants(consumer, player);
        }
    }

    private static void applyEnchants(Consumer consumer, PlayerEntity player) {
        EquipmentUtil.getAllEntityFeatureGroupProviders(player).forEach(efg -> {
            efg.getEnchantmentModifiers().forEach(consumer::accept);
        });
    }

    @FunctionalInterface
    interface Consumer {
        void accept(Enchantment enchantment, int level);
    }
}
