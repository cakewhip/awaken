package com.kqp.awaken.mixin.entity.player;

import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.init.AwakenDimensions;
import com.kqp.awaken.item.effect.ArmorListener;
import com.kqp.awaken.item.effect.Equippable;
import com.kqp.awaken.item.effect.SpecialItemRegistry;
import com.kqp.awaken.item.trinket.FlyingItem;
import com.kqp.awaken.world.dimension.NullSpaceTraveler;
import com.kqp.awaken.world.placer.NullSpacePlacer;
import com.kqp.awaken.world.placer.OverworldPlacer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Random;

/**
 * Used to:
 * Apply damage buffs
 * Detect item equips/unequips
 * Add custom attributes
 * Buff melee damage
 * Transport player to and from the null space
 * Rocket boots go brrrrrrrrrrrrrrrr
 */
@Mixin(PlayerEntity.class)
// @Implements(@Interface(iface = NullSpaceTraveler.class, prefix = "vw$"))
public class PlayerEntityMixin implements NullSpaceTraveler, PlayerFlightProperties {
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

    @Inject(method = "tick", at = @At("HEAD"))
    public void detectEquippableArmor(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient()) {
            DefaultedList<ItemStack> curr = player.inventory.armor;

            if (PLAYER_ARMOR_TRACKER.containsKey(player)) {
                DefaultedList<ItemStack> prev = PLAYER_ARMOR_TRACKER.get(player);

                for (int i = 0; i < curr.size(); i++) {
                    ItemStack currItemStack = curr.get(i);
                    ItemStack prevItemStack = prev.get(i);

                    // TODO: make this prettier

                    if (!ItemStack.areItemsEqual(currItemStack, prevItemStack)) {
                        if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(prevItemStack.getItem())) {
                            SpecialItemRegistry.EQUIPPABLE_ARMOR.get(prevItemStack.getItem()).unEquip(prevItemStack, player);
                        }

                        if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(currItemStack.getItem())) {
                            SpecialItemRegistry.EQUIPPABLE_ARMOR.get(currItemStack.getItem()).equip(currItemStack, player);
                        }

                        for (int j = 0; j < 4 && j != i; j++) {
                            ItemStack equipStack = player.inventory.armor.get(i);

                            if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(prevItemStack.getItem())) {
                                Equippable equippable = SpecialItemRegistry.EQUIPPABLE_ARMOR.get(prevItemStack.getItem());

                                if (equippable instanceof ArmorListener) {
                                    ((ArmorListener) equippable).onOtherUnEquip(prevItemStack, player);
                                }
                            }

                            if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(currItemStack.getItem())) {
                                Equippable equippable = SpecialItemRegistry.EQUIPPABLE_ARMOR.get(currItemStack.getItem());

                                if (equippable instanceof ArmorListener) {
                                    ((ArmorListener) equippable).onOtherEquip(currItemStack, player);
                                }
                            }
                        }
                    }
                }
            }

            DefaultedList<ItemStack> clone = DefaultedList.ofSize(4, ItemStack.EMPTY);
            for (int i = 0; i < clone.size(); i++) {
                clone.set(i, curr.get(i).copy());
            }

            PLAYER_ARMOR_TRACKER.put(player, clone);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void detectEquippableItems(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient()) {
            ItemStack curr = player.inventory.getMainHandStack();

            if (PLAYER_HELD_TRACKER.containsKey(player)) {
                ItemStack prev = PLAYER_HELD_TRACKER.get(player);

                if (!ItemStack.areItemsEqual(curr, prev)) {
                    if (SpecialItemRegistry.EQUIPPABLE_ITEM.containsKey(prev.getItem())) {
                        SpecialItemRegistry.EQUIPPABLE_ITEM.get(prev.getItem()).unEquip(prev, player);
                    }

                    if (SpecialItemRegistry.EQUIPPABLE_ITEM.containsKey(curr.getItem())) {
                        SpecialItemRegistry.EQUIPPABLE_ITEM.get(curr.getItem()).equip(curr, player);
                    }
                }
            }

            PLAYER_HELD_TRACKER.put(player, curr.copy());
        }
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void addRangedDamageAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> callbackInfoReturnable) {
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.RANGED_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.BOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.CROSSBOW_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.TRIDENT_DAMAGE);

        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.MELEE_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.SWORD_DAMAGE);
        callbackInfoReturnable.getReturnValue().add(AwakenEntityAttributes.AXE_DAMAGE);
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
