package com.kqp.awaken.mixin.entity.player;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.entity.player.PlayerReference;
import com.kqp.awaken.init.AwakenDimensions;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.trinket.FlyingItem;
import com.kqp.awaken.util.TrinketUtil;
import com.kqp.awaken.world.dimension.NullSpaceTraveler;
import com.kqp.awaken.world.placer.NullSpacePlacer;
import com.kqp.awaken.world.placer.OverworldPlacer;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
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
public class PlayerEntityMixin implements NullSpaceTraveler, PlayerFlightProperties {
    @Shadow
    protected HungerManager hungerManager;

    public boolean returnMarker = false;

    public boolean secondSpacing;
    public boolean flying;
    public int flyTime;
    public boolean floating;

    private static final HashMap<PlayerEntity, DefaultedList<ItemStack>> PLAYER_ARMOR_TRACKER = new HashMap();
    private static final HashMap<PlayerEntity, ItemStack> PLAYER_HELD_TRACKER = new HashMap();

    @Inject(method = "tick", at = @At("HEAD"))
    public void doNullSpaceTravel(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient) {

            if (player.dimension == DimensionType.OVERWORLD && player.getY() <= -8D) {
                FabricDimensions.teleport(player, AwakenDimensions.NULL_SPACE, new NullSpacePlacer());
            }

            if (player.dimension == AwakenDimensions.NULL_SPACE && this.returnMarker) {
                FabricDimensions.teleport(player, DimensionType.OVERWORLD, new OverworldPlacer());
                this.setReturnMarker(false);
            }
        }
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void addCustomAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> callbackInfoReturnable) {
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.RANGED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.BOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.CROSSBOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.TRIDENT_DAMAGE);

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
                flyTime = this.getFlyingItem().getMaxFlyTime();

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
            System.out.println("saddled");
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
    public void setReturnMarker(boolean returnMarker) {
        this.returnMarker = returnMarker;
    }

    @Override
    public boolean getReturnMarker() {
        return returnMarker;
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
    public FlyingItem getFlyingItem() {
        Item currentItem = ((PlayerEntity) (Object) this).getEquippedStack(EquipmentSlot.FEET).getItem();

        return currentItem instanceof FlyingItem ? (FlyingItem) currentItem : null;
    }

    @Override
    public boolean canFly() {
        return getFlyingItem() != null;
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
        return getFlyingItem().canFloat();
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
