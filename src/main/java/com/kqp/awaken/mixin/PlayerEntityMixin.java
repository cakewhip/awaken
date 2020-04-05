package com.kqp.awaken.mixin;

import com.kqp.awaken.item.effect.SpecialItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply damage buffs.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"))
    private float applyDamageEffects(ItemStack itemStack, EntityGroup group, Entity entity) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        float damage = EnchantmentHelper.getAttackDamage(itemStack, group);


        for (ItemStack invStack : player.inventory.main) {
            Item i = invStack.getItem();

            if (SpecialItemRegistry.DAMAGE_MODIFIERS.containsKey(i)) {
                damage = SpecialItemRegistry.DAMAGE_MODIFIERS.get(i).applyDamageModifier(damage, player, entity, itemStack);
            }
        }

        for (ItemStack invStack : player.inventory.armor) {
            Item i = invStack.getItem();

            if (SpecialItemRegistry.DAMAGE_MODIFIERS.containsKey(i)) {
                damage = SpecialItemRegistry.DAMAGE_MODIFIERS.get(i).applyDamageModifier(damage, player, entity, itemStack);
            }
        }

        return damage;
    }
}
