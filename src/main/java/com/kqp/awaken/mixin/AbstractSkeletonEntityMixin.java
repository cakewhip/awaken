package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.util.MobDecorator;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to give skeletons better stats and armor post-awakening.
 */
@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin {
    @Inject(at = @At("TAIL"), method = "initAttributes")
    protected void overrideAttributes(CallbackInfo callbackInfo) {
        AbstractSkeletonEntity ase = (AbstractSkeletonEntity) (Object) this;
        if (ase instanceof SkeletonEntity && Awaken.worldProperties.isWorldAwakened()) {
            SkeletonEntity skeleton = (SkeletonEntity) (Object) this;
            boolean bm = Awaken.worldProperties.isBloodMoonActive();

            skeleton.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(bm ? 0.38D : 0.36D);
            skeleton.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(bm ? 15.0D : 10.0D);
        }
    }

    @Inject(at = @At("TAIL"), method = "initEquipment")
    protected void newEquipment(LocalDifficulty difficulty, CallbackInfo callbackInfo) {
        AbstractSkeletonEntity ase = (AbstractSkeletonEntity) (Object) this;
        if (ase instanceof SkeletonEntity && Awaken.worldProperties.isWorldAwakened()) {
            MobDecorator.giveArmor((SkeletonEntity) (Object) this);
        }
    }
}
