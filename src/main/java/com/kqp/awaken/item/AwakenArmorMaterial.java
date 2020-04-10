package com.kqp.awaken.item;

import com.kqp.awaken.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

/**
 * Used to create custom armor materials.
 */
public enum AwakenArmorMaterial implements ArmorMaterial {
    WITHER_SCALE("wither_scale", 40, new int[] { 6, 8, 10, 6 }, 14, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 5.0F, 0.5F, () -> {
        return Ingredient.ofItems(Awaken.TItems.ENDER_DRAGON_SCALE, Awaken.TItems.WITHER_RIB);
    }),
    SALVIUM("salvium", 45, new int[] { 7, 9, 12, 7 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 5.0F, 0.5F, () -> {
        return Ingredient.ofItems(Awaken.Groups.SALVIUM.INGOT);
    }),
    VALERIUM("valerium", 45, new int[] { 7, 10, 12, 7 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 7.5F, 0.5F, () -> {
        return Ingredient.ofItems(Awaken.Groups.VALERIUM.INGOT);
    }),
    CELESTIAL_STEEL("celestial_steel", 44, new int[] { 3, 6, 8, 3 }, 24, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 24.0F, 0.5F, () -> {
        return Ingredient.ofItems(Awaken.TItems.CELESTIAL_STEEL_INGOT);
    });

    private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredientSupplier;

    AwakenArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> ingredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = ingredientSupplier;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    @Environment(EnvType.CLIENT)
    public String getName() {
        return this.name;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }
}
