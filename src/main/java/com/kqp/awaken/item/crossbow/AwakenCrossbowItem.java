package com.kqp.awaken.item.crossbow;

import com.kqp.awaken.loot.AwakenRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for custom crossbows.
 */
public class AwakenCrossbowItem extends CrossbowItem {
    public AwakenRarity rarity = AwakenRarity.OKAY;

    /**
     * Base damage, subject to additional damage by arrow used.
     * The vanilla calculates as such: 2 + (powerLevel + 1) * 0.5
     */
    public double baseDamage;

    public AwakenCrossbowItem(double baseDamage) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));

        this.baseDamage = baseDamage;
    }

    /**
     * Modify the projectile entity that the bow will fire.
     *
     * @return ProjectileEntity the projectile entity that the bow will shoot
     */
    public void modifyProjectileEntity(PersistentProjectileEntity projectileEntity) {
        projectileEntity.setDamage(this.baseDamage);
    }

    public Item setRarity(AwakenRarity rarity) {
        this.rarity = rarity;

        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        BaseText firstLine = (BaseText) tooltip.get(0);
        firstLine.setStyle(firstLine.getStyle().withColor(this.rarity.color));
    }
}
