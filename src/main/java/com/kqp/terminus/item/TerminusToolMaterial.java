package com.kqp.terminus.item;

import com.kqp.terminus.Terminus;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

/**
 * Used to create custom tool materials.
 */
public enum TerminusToolMaterial implements ToolMaterial {
    CELESTIAL(4, 1000, 10.0F, 7.0F, 18, () -> {
        return Ingredient.ofItems(new ItemConvertible[] { Terminus.TItems.CELESTIAL_STEEL_INGOT });
    }),
    PHASE_0_SWORD(-1, -1, -1.0F, 5F, -1, () -> {
        return Ingredient.ofItems();
    }),
    JANG_KATANA(-1, -1, -1.0F, 14F, -1, () -> {
        return Ingredient.ofItems();
    });

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    TerminusToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
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
    public float getMiningSpeed() {
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
}
