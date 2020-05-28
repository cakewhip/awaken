package com.kqp.awaken.mixin.entity.player;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.entity.player.PlayerReference;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.trinket.flight.FlightTrinketItem;
import com.kqp.awaken.util.TrinketUtil;
import com.mojang.authlib.GameProfile;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Random;

/**
 * Used to:
 * Apply damage buffs.
 * Add custom attributes.
 * Buff melee damage.
 * Transport player to and from the null space.
 * Rocket boots go brrrrrrrrrrrrrrrr.
 * Set player reference in HungerManager.
 * Apply unarmed damage attribute.
 */
@Mixin(PlayerEntity.class)
// @Implements(@Interface(iface = NullSpaceTraveler.class, prefix = "vw$"))
public class PlayerEntityMixin implements PlayerFlightProperties {
    @Shadow
    protected HungerManager hungerManager;

    public boolean secondSpacing;
    public boolean flying;
    public int flyTime;
    public boolean floating;

    private static final HashMap<PlayerEntity, DefaultedList<ItemStack>> PLAYER_ARMOR_TRACKER = new HashMap();
    private static final HashMap<PlayerEntity, ItemStack> PLAYER_HELD_TRACKER = new HashMap();

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void addCustomAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> callbackInfoReturnable) {
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.RANGED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.BOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.CROSSBOW_DAMAGE);

        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.MELEE_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.SWORD_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.AXE_DAMAGE);

        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.UNARMED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.POTION_DAMAGE);
    }

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
                        this.setFloating(true);
                        this.setFlying(false);

                        player.world.addParticle(ParticleTypes.POOF,
                                player.getX(), player.getY(), player.getZ(),
                                r.nextDouble() - r.nextDouble(), -r.nextDouble(), r.nextDouble() - r.nextDouble()
                        );
                    }
                } else {
                    this.setFlying(false);
                    this.setFloating(false);
                }
            }
        }
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void setPlayerReference(World world, BlockPos blockPos, GameProfile gameProfile, CallbackInfo callbackInfo) {
        ((PlayerReference) hungerManager).setPlayer((PlayerEntity) (Object) this);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0))
    private float applyUnarmedDamage(float f) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Apply combat saddle effect
        if (player.hasVehicle() && TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.COMBAT_SADDLE)) {
            f *= 1.04;
        }

        // Apply unarmed attribute
        if (player.getMainHandStack().isEmpty()) {
            EntityAttributeInstance unarmedAttribute = player.getAttributeInstance(AwakenEntityAttributes.UNARMED_DAMAGE);
            unarmedAttribute.setBaseValue(f);

            f = (float) unarmedAttribute.getValue();
        }

        return f;
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
