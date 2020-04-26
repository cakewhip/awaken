package com.kqp.awaken.item.bow;

import net.minecraft.entity.projectile.PersistentProjectileEntity;

/**
 * Class for creating bows that automatically light arrows on fire.
 */
public class FlameBowItem extends AwakenBowItem {
    public FlameBowItem(double baseDamage) {
        super(baseDamage);
    }

    @Override
    public void modifyProjectileEntity(PersistentProjectileEntity projectileEntity) {
        super.modifyProjectileEntity(projectileEntity);
        projectileEntity.setOnFireFor(200);
    }
}
