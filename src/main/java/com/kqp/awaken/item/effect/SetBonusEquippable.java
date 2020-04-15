package com.kqp.awaken.item.effect;

import com.kqp.awaken.util.ItemUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

/**
 * Extends EffectAttributeEquippable and only applies effect if the full set is equipped.
 */
public class SetBonusEquippable extends EffectAttributeEquippable {
    @Override
    public void equip(ItemStack itemStack, PlayerEntity player) {
        if (ItemUtil.wearingFullSet(player, ((ArmorItem) itemStack.getItem()).getMaterial())) {
            super.equip(itemStack, player);
        }
    }
}
