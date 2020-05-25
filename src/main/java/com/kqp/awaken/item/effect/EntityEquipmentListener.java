package com.kqp.awaken.item.effect;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface EntityEquipmentListener {
    void onEquip(LivingEntity entity, ItemStack itemStack, ItemStack equippedStack, EquipmentSlot equipmentSlot);
    void onUnEquip(LivingEntity entity, ItemStack itemStack, ItemStack unEquippedStack, EquipmentSlot equipmentSlot);
}
