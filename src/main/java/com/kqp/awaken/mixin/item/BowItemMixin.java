package com.kqp.awaken.mixin.item;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.bow.AwakenBowItem;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to:
 * Modify bow projectiles.
 * Apply the ranger's glove effect.
 */
@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArrowItem;createArrow(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    public PersistentProjectileEntity modifyArrow(ArrowItem arrowItem, World world, ItemStack stack, LivingEntity shooter) {
        PersistentProjectileEntity projectileEntity = arrowItem.createArrow(world, stack, shooter);

        if ((Object) this instanceof AwakenBowItem) {
            AwakenBowItem bowItem = (AwakenBowItem) (Object) this;
            bowItem.modifyProjectileEntity(projectileEntity);
        }

        ((RangedWeaponProjectile) projectileEntity).setType(RangedWeaponProjectile.Type.BOW);

        return projectileEntity;
    }

    /**
     * TODO: figure out why the cancel causes a visual error
     */
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"))
    private void applyRangersGloveEffect(ItemStack arrowStack, int amt,
                                         ItemStack bowStack, World world, LivingEntity user, int remainingUseTicks) {
        boolean consume = true;

        if (user instanceof PlayerEntity && TrinketUtil.hasTrinket((PlayerEntity) user, AwakenItems.Trinkets.RANGERS_GLOVE) || true) {
            if (user.getRandom().nextFloat() < 0.20F) {
                consume = false;
            }
        }

        if (consume) {
            System.out.println("CONSUMING");
            arrowStack.decrement(amt);
        }
    }
}
