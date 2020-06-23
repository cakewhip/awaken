package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the damage mitigation attribute.
 */
@Mixin(PlayerEntity.class)
public abstract class DamageMitigationApplier extends LivingEntity {
    protected DamageMitigationApplier(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyDamageMitigation(LivingEntity player, DamageSource source, float amount) {
        double damageMitigation = player.getAttributeValue(AwakenEntityAttributes.DAMAGE_MITIGATION);

        amount *= (1D - damageMitigation);

        return super.damage(source, amount);
    }
}
