package com.kqp.awaken.item.effect;

import com.kqp.awaken.util.ItemUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

/**
 * Extends EffectAttributeEquippable and only applies effect if the full set is equipped.
 */
public class SetBonusEquippable extends EffectAttributeEquippable implements ArmorListener {
    @Override
    public void equip(ItemStack itemStack, PlayerEntity player) {
        update(player);
    }

    @Override
    public void unEquip(ItemStack itemStack, PlayerEntity player) {
        update(player);
    }

    @Override
    public void onOtherEquip(ItemStack stack, PlayerEntity player) {
        update(player);
    }

    @Override
    public void onOtherUnEquip(ItemStack stack, PlayerEntity player) {
        update(player);
    }

    public void update(PlayerEntity player) {
        if (wearingFullSet(player)) {
            super.applyEffects(player);
        } else {
            super.removeEffects(player);
        }
    }

    public boolean wearingFullSet(PlayerEntity player) {
        ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);

        if (helmet == ItemStack.EMPTY) {
            return false;
        } else {
            return ItemUtil.wearingFullSet(player, ((ArmorItem) helmet.getItem()).getMaterial());
        }
    }
}
