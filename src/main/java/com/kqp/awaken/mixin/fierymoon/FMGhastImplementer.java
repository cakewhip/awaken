package com.kqp.awaken.mixin.fierymoon;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.mixin.accessor.MobEntityAccessor;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to buff the ghast during fiery moon and also have it not get stuck when spawned.
 */
@Mixin(GhastEntity.class)
public class FMGhastImplementer {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("ghast_fiery_moon_buffs")
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MAX_HEALTH, 1D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ARMOR, 0.5D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void buffGhast(EntityType<? extends GhastEntity> entityType, World world, CallbackInfo callbackInfo) {
        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                GhastEntity ghast = (GhastEntity) (Object) this;

                BUFF.applyToAndHeal(ghast);
            }
        }
    }

    /**
     * Used to prevent ghasts from spawning and suffocating immediately.
     *
     * @param source
     * @param amount
     * @param callbackInfo
     */
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void fixBadSpawn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        GhastEntity ghast = (GhastEntity) (Object) this;

        if (source == DamageSource.IN_WALL && ghast.age < 20) {
            int airBlocks = 0;
            BlockPos current = ghast.getBlockPos();

            while (airBlocks < 3 && current.getY() < 96) {
                if (ghast.world.getBlockState(current).getBlock().is(Blocks.AIR)) {
                    airBlocks++;
                } else {
                    airBlocks = 0;
                }

                current = current.up();
            }

            if (airBlocks >= 3) {
                ghast.refreshPositionAndAngles(current, 0F, 0F);
                callbackInfo.setReturnValue(false);
            }
        }
    }

    @Inject(method = "initGoals", at = @At("RETURN"))
    private void targetVillagers(CallbackInfo callbackInfo) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();

        targetSelector.add(1, new FollowTargetGoal(
                (GhastEntity) (Object) this,
                VillagerEntity.class,
                false
        ));
    }
}
