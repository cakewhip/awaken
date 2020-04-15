package com.kqp.awaken.item.material;

import com.kqp.awaken.init.AwakenItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

/**
 * Used to create custom tool materials.
 */
public class AwakenToolMaterial implements ToolMaterial {
    public static final AwakenToolMaterial CELESTIAL_STEEL = new AwakenToolMaterial(
            4,
            1000,
            10.0F,
            10.0F,
            18,
            () -> Ingredient.ofItems(AwakenItems.CELESTIAL_STEEL_INGOT));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    private AwakenToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return miningLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    public static AwakenToolMaterial newSwordMaterial(int itemDurability, float attackDamage, int enchantability) {
        return new AwakenToolMaterial(-1, itemDurability, 6F, attackDamage, enchantability, () -> Ingredient.EMPTY);
    }

    public static AwakenToolMaterial newToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability) {
        return new AwakenToolMaterial(miningLevel, itemDurability, miningSpeed, attackDamage, enchantability, () -> Ingredient.EMPTY);
    }
}
