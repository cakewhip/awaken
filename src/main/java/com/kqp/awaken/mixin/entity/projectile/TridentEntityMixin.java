package com.kqp.awaken.mixin.entity.projectile;

import com.kqp.awaken.entity.attribute.RangedWeaponProjectile;
import com.kqp.awaken.item.trident.AwakenTridentItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to apply the trident attribute modifier.
 */
@Mixin(TridentEntity.class)
public class TridentEntityMixin {
    @Inject(method = "<init>*", at = @At("RETURN"))
    public void tagTrident(World world, LivingEntity owner, ItemStack stack, CallbackInfo callbackInfo) {
        if (stack.getItem() instanceof AwakenTridentItem) {
            ((TridentEntity) (Object) this).setDamage(((AwakenTridentItem) stack.getItem()).getDamage());
        }

        ((RangedWeaponProjectile) this).setType(RangedWeaponProjectile.Type.TRIDENT);
    }
}
