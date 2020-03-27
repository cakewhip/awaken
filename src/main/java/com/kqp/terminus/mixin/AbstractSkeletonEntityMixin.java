package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.MobDecorator;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin {
    @Inject(at = @At("TAIL"), method = "initAttributes")
    protected void overrideAttributes(CallbackInfo callbackInfo) {
        AbstractSkeletonEntity ase = (AbstractSkeletonEntity) (Object) this;
        if (ase instanceof SkeletonEntity && Terminus.worldProperties.isWorldAwakened()) {
            SkeletonEntity skeleton = (SkeletonEntity) (Object) this;

            skeleton.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.36D);
            skeleton.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(3.0D);
        }
    }

    @Inject(at = @At("TAIL"), method = "initEquipment")
    protected void newEquipment(LocalDifficulty difficulty, CallbackInfo callbackInfo) {
        AbstractSkeletonEntity ase = (AbstractSkeletonEntity) (Object) this;
        if (ase instanceof SkeletonEntity && Terminus.worldProperties.isWorldAwakened()) {
            MobDecorator.giveArmor((SkeletonEntity) (Object) this);
        }
    }
}
