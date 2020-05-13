package com.kqp.awaken.item.material;

import com.kqp.awaken.init.AwakenItems;
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
public class AwakenArmorMaterial implements ArmorMaterial {
    public static final AwakenArmorMaterial DRAGON_SCALE = new AwakenArmorMaterial("dragon_scale", 37, new int[] { 6, 8, 10, 6 }, 14, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 4.0F, 0.5F, () -> {
        return Ingredient.ofItems(AwakenItems.ENDER_DRAGON_SCALE);
    });

    public static final AwakenArmorMaterial WITHER_BONE = new AwakenArmorMaterial("wither_bone", 37, new int[] { 6, 8, 10, 6 }, 14, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 4.0F, 0.5F, () -> {
        return Ingredient.ofItems(AwakenItems.WITHER_RIB);
    });

    public static final AwakenArmorMaterial SALVIUM = new AwakenArmorMaterial("salvium", 45, new int[] { 7, 9, 12, 7 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 5.0F, 0.5F, () -> {
        return Ingredient.ofItems(AwakenItems.SALVIUM_INGOT);
    });

    public static final AwakenArmorMaterial VALERIUM = new AwakenArmorMaterial("valerium", 45, new int[] { 7, 10, 12, 7 }, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 7.5F, 0.5F, () -> {
        return Ingredient.ofItems(AwakenItems.VALERIUM_INGOT);
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

    public static AwakenArmorMaterial trinketMaterial(String name, int durability) {
        return new AwakenArmorMaterial(name, durability, new int[] {0, 0, 0, 0}, 1, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0F, 0F, () -> Ingredient.EMPTY);
    }
}
