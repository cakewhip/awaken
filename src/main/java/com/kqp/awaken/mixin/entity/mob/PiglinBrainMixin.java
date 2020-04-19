package com.kqp.awaken.mixin.entity.mob;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.init.AwakenBlocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.DynamicSerializableBoolean;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to add the ender dragon head -> enderian hell forge barter.
 */
@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {
    @Inject(method = "playerInteract", at = @At("HEAD"), cancellable = true)
    private static void addEnderianHellForgeBarter(PiglinEntity piglin, PlayerEntity player, Hand hand, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ItemStack itemStack = player.getStackInHand(hand);
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(piglin.world);

        if (awakenLevelData.isWorldAwakened() && itemStack.getItem() == Items.DRAGON_HEAD) {
            ItemStack itemStack2 = itemStack.split(1);
            piglin.equipStack(EquipmentSlot.OFFHAND, itemStack2);
            piglin.method_25939(EquipmentSlot.OFFHAND);

            piglin.getBrain().remember(MemoryModuleType.ADMIRING_ITEM, DynamicSerializableBoolean.of(true), 120L);
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    @Inject(method = "consumeOffHandItem", at = @At("HEAD"), cancellable = true)
    private static void consumeOffHandItem(PiglinEntity piglin, boolean bl, CallbackInfo callbackInfo) {
        ItemStack itemStack = piglin.getStackInHand(Hand.OFF_HAND);

        if (itemStack.getItem() == Items.DRAGON_HEAD) {
            piglin.swingHand(Hand.OFF_HAND);
            LookTargetUtil.give(piglin, new ItemStack(AwakenBlocks.ENDERIAN_HELL_FORGE), findGround(piglin).add(0.0D, 1.0D, 0.0D));
            piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);

            callbackInfo.cancel();
        }
    }

    private static Vec3d findGround(PiglinEntity piglin) {
        Vec3d vec3d = TargetFinder.findGroundTarget(piglin, 4, 2);
        return vec3d == null ? piglin.getPos() : vec3d;
    }
}
