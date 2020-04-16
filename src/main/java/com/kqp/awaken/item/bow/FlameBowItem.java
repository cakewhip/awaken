package com.kqp.awaken.item.bow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Class for creating bows that automatically light arrows on fire.
 */
public class FlameBowItem extends AwakenBowItem {
    public FlameBowItem(double baseDamage, boolean infinity) {
        super(baseDamage, infinity);
    }

    @Override
    public PersistentProjectileEntity createProjectileEntity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ArrowEntity arrowEntity = new ArrowEntity(world, user);
        arrowEntity.initFromStack(stack);

        arrowEntity.setOnFireFor(100);

        return arrowEntity;
    }
}
