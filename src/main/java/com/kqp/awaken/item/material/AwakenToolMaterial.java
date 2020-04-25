package com.kqp.awaken.item.material;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

/**
 * Used to create custom tool materials.
 */
public class AwakenToolMaterial implements ToolMaterial {
    private final int miningLevel;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final float attackSpeed;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    private AwakenToolMaterial(int miningLevel, int durability, float miningSpeed, float attackDamage, float attackSpeed, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    public float getAttackSpeed() {
        return this.attackSpeed;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public static AwakenToolMaterial sword(int durability, float attackDamage, float attackSpeed, int enchantability, Item... repairItems) {
        return AwakenToolMaterial.tool(-1, durability, 6F, attackDamage, attackSpeed, enchantability, repairItems);
    }

    public static AwakenToolMaterial tool(int miningLevel, int durability, float miningSpeed, float attackDamage, float attackSpeed, int enchantability, Item... repairItems) {
        return new AwakenToolMaterial(miningLevel, durability, miningSpeed, attackDamage, attackSpeed, enchantability, () -> Ingredient.ofItems(repairItems));
    }
}
