package com.kqp.awaken.mixin.awakening;

import com.kqp.awaken.world.data.AwakenLevelData;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to trigger the awakening.
 */
@Mixin(BedBlock.class)
public class AwakeningTrigger {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void triggerAwakening(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> callbackInfoReturnable) {
        if (!world.isClient) {
            if (world.getDimension().isEnd()) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());

                if (!awakenLevelData.isWorldAwakened()
                        && awakenLevelData.isPostRaid()
                        && awakenLevelData.isPostDragon()
                        && awakenLevelData.isPostWither()
                        && awakenLevelData.isPostElderGuardian()) {
                    awakenLevelData.setAwakening();

                    callbackInfoReturnable.setReturnValue(ActionResult.SUCCESS);

                    player.changeDimension(DimensionType.OVERWORLD_REGISTRY_KEY);
                    Broadcaster.broadcastMessage(world.getServer(), "New ores have generated!", Formatting.LIGHT_PURPLE, false, true);
                }
            }
        }
    }
}
