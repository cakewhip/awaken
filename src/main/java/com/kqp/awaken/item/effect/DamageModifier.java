package com.kqp.awaken.item.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Functional interface for applying a damage modifier.
 */
public interface DamageModifier {
    float applyDamageModifier(float damage, PlayerEntity attacker, Entity target, ItemStack itemStack);
}
