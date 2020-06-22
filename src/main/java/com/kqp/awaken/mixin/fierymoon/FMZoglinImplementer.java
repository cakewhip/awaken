package com.kqp.awaken.mixin.fierymoon;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to buff the zoglin during fiery moon and also make it target players
 */
@Mixin(ZoglinEntity.class)
public class FMZoglinImplementer {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("zoglin_fiery_moon_buffs")
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.25D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MAX_HEALTH, 1D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, 0.5D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.02D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void buffZoglin(EntityType<? extends ZoglinEntity> entityType, World world, CallbackInfo callbackInfo) {
        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                ZoglinEntity zoglin = (ZoglinEntity) (Object) this;

                BUFF.applyToAndHeal(zoglin);
            }
        }
    }

    /**
     * Replace targeting conditional with one that targets players and friendly mobs.
     *
     * @param entity
     * @param callbackInfo
     */
    @Inject(
            method = "method_26936", // Conditional for targeting enemies
            at = @At("HEAD"),
            cancellable = true
    )
    private static void targetPlayersOnly(LivingEntity entity, CallbackInfoReturnable<Boolean> callbackInfo) {
        World world = entity.world;

        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                callbackInfo.setReturnValue(
                        (entity.getType() == EntityType.PLAYER && EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(entity))
                                || entity.getType() == EntityType.VILLAGER
                );
            }
        }
    }
}
