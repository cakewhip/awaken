package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.MobDecorator;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to give zombies better stats and equipment post-awakening.
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {
    @Inject(at = @At("TAIL"), method = "initAttributes")
    protected void overrideAttributes(CallbackInfo callbackInfo) {
        if (Terminus.worldProperties.isWorldAwakened()) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;

            zombie.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
            zombie.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(6.0D);
            zombie.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(12.0D);
        }
    }

    @Inject(at = @At("TAIL"), method = "initEquipment")
    protected void newEquipment(LocalDifficulty difficulty, CallbackInfo callbackInfo) {
        if (Terminus.worldProperties.isWorldAwakened()) {
            MobDecorator.giveArmor((ZombieEntity) (Object) this);
            MobDecorator.giveSword((ZombieEntity) (Object) this);
        }
    }
}
