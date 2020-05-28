package com.kqp.awaken.mixin.player;

import com.kqp.awaken.entity.player.PlayerReference;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to set the player reference in the hunger manager.
 */
@Mixin(PlayerEntity.class)
public class HungerManagerPlayerReferenceSetter {
    @Shadow
    protected HungerManager hungerManager;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void setPlayerReference(World world, BlockPos blockPos, GameProfile gameProfile, CallbackInfo callbackInfo) {
        ((PlayerReference) hungerManager).setPlayer((PlayerEntity) (Object) this);
    }
}
