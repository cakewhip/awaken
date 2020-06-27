package com.kqp.awaken.mixin.player.flight;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.item.trinket.FlightTrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Used to rocket boots go brrrrrrrrrrrrrrrr.
 */
@Mixin(PlayerEntity.class)
public class PlayerFlightImplementer implements PlayerFlightProperties {
    public boolean secondSpacing;
    public boolean flying;
    public int flyTime;
    public boolean floating;;

    @Inject(method = "tick", at = @At("HEAD"))
    public void makeRocketBootsGoBrrrr(CallbackInfo callbackInfo) {
        PlayerEntity player = ((PlayerEntity) (Object) this);
        if (this.canFly()) {
            if (player.isOnGround()) {
                flyTime = ((FlightTrinketItem) this.getFlyingItemStack().getItem()).getMaxFlyTime();

                this.setFloating(false);
                this.setSecondSpacing(false);
            } else {
                if (this.isSecondSpacing()) {
                    Random r = player.getRandom();

                    if (flyTime > 0) {
                        this.setFlying(true);
                        this.setFloating(false);

                        flyTime = Math.max(0, flyTime - 1);

                        player.world.addParticle(ParticleTypes.LAVA,
                                player.getX(), player.getY(), player.getZ(),
                                r.nextDouble() - r.nextDouble(), -r.nextDouble(), r.nextDouble() - r.nextDouble()
                        );
                    } else {
                        this.setFlying(false);
                        this.setFloating(false);

                        if (canFloat()) {
                            this.setFloating(true);

                            player.world.addParticle(ParticleTypes.POOF,
                                    player.getX(), player.getY(), player.getZ(),
                                    r.nextDouble() - r.nextDouble(), -r.nextDouble(), r.nextDouble() - r.nextDouble()
                            );
                        }
                    }
                } else {
                    this.setFlying(false);
                    this.setFloating(false);
                }
            }
        }
    }

    @Override
    public void setSecondSpacing(boolean secondSpacing) {
        this.secondSpacing = secondSpacing;
    }

    @Override
    public boolean isSecondSpacing() {
        return secondSpacing;
    }

    @Override
    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    @Override
    public boolean isFlying() {
        return flying;
    }

    @Override
    public ItemStack getFlyingItemStack() {
        Inventory trinkets = TrinketsApi.getTrinketsInventory((PlayerEntity) (Object) this);

        for (int i = 0; i < trinkets.size(); i++) {
            ItemStack itemStack = trinkets.getStack(i);

            if (itemStack.getItem() instanceof FlightTrinketItem) {
                return itemStack;
            }
        }

        return null;
    }

    @Override
    public boolean canFly() {
        return getFlyingItemStack() != null;
    }

    @Override
    public int getFlyTime() {
        return 0;
    }

    @Override
    public void setFlyTime(int flyTime) {
        this.flyTime = flyTime;
    }

    @Override
    public boolean canFloat() {
        return ((FlightTrinketItem) getFlyingItemStack().getItem()).canFloat();
    }

    @Override
    public boolean isFloating() {
        return floating;
    }

    @Override
    public void setFloating(boolean floating) {
        this.floating = floating;
    }
}
