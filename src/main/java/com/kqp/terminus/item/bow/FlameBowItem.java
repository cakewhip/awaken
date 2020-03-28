package com.kqp.terminus.item.bow;

import com.kqp.terminus.item.tool.TerminusBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Class for creating bows that automatically light arrows on fire.
 */
public class FlameBowItem extends TerminusBowItem {
    public FlameBowItem(double baseDamage, boolean infinity) {
        super(baseDamage, infinity);
    }

    @Override
    public ProjectileEntity createProjectileEntity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ArrowEntity arrowEntity = new ArrowEntity(world, user);
        arrowEntity.initFromStack(stack);

        arrowEntity.setOnFireFor(100);

        return arrowEntity;
    }
}
