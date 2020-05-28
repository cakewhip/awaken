package com.kqp.awaken.mixin.entity;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.effect.EntityEquipmentListener;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.util.Broadcaster;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Used to:
 * Detect deaths for triggering the awakening.
 * Deny riptide status when using the Atlantean sword.
 * Give spiders poison effect post-awakening.
 * Apply the silky glove effect.
 * Apply the stick of dynamite, lightning bottle, and electrified dynamite effects.
 * Apply the shock-wave shield effect.
 * Apply the scorched mask effect.
 * Listen for equipping and un-equipping.
 * Apply the shulker charm and glove effect.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity le = (LivingEntity) (Object) this;
        World world = le.world;

        if (!world.isClient) {
            if (source.getAttacker() instanceof PlayerEntity) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());
                MinecraftServer server = world.getServer();

                if (le instanceof CowEntity && !awakenLevelData.isPostDragon()) {
                    awakenLevelData.setPostDragon();
                    Broadcaster.broadcastMessage(server, "The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (le instanceof SheepEntity && !awakenLevelData.isPostWither()) {
                    awakenLevelData.setPostWither();
                    Broadcaster.broadcastMessage(server, "Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (le instanceof PigEntity && !awakenLevelData.isPostElderGuardian()) {
                    awakenLevelData.setPostElderGuardian();
                    Broadcaster.broadcastMessage(server, "A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                } else if (le instanceof ZombieEntity && !awakenLevelData.isPostRaid()) {
                    awakenLevelData.setPostRaid();
                    Broadcaster.broadcastMessage(server, "A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }

    @Inject(method = "isUsingRiptide", at = @At("HEAD"), cancellable = true)
    public void fixRiptideReturn(CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getMainHandStack().getItem() instanceof AtlanteanSabreItem) {
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void implementSpiderPoison(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // Check if damage is applied
        if (callbackInfoReturnable.getReturnValue()) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(livingEntity.world.getServer());

            // Apply spider poison
            if (awakenLevelData.isWorldAwakened()) {
                if (source.getAttacker() instanceof SpiderEntity) {

                    if (livingEntity.getRandom().nextFloat() < 0.5F) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 3 * 20, 0));
                    }
                }
            }
        }
    }

    @Inject(method = "getAttributeValue", at = @At("HEAD"), cancellable = true)
    public void applyMeleeDamageAttributes(EntityAttribute entityAttribute, CallbackInfoReturnable<Double> callbackInfo) {
        if (((Object) this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (entityAttribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                double damage = player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.MELEE_DAMAGE);
                attribute.setBaseValue(damage);
                damage = attribute.getValue();
                attribute.setBaseValue(0D);

                ItemStack held = player.getMainHandStack();
                attribute = null;

                if (held.getItem() instanceof SwordItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.SWORD_DAMAGE);
                } else if (held.getItem() instanceof AxeItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.AXE_DAMAGE);
                }

                if (attribute != null) {
                    attribute.setBaseValue(damage);
                    damage = attribute.getValue();
                    attribute.setBaseValue(0D);
                }

                callbackInfo.setReturnValue(damage);
            }
        }
    }

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void overideFlyingMovementSpeed(float slipperiness, CallbackInfoReturnable<Float> callbackInfo) {
        LivingEntity entity = ((LivingEntity) (Object) this);

        if (this instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) entity;

            if (flightProperties.canFly() && !entity.isOnGround()) {
                callbackInfo.setReturnValue((float) (entity.getMovementSpeed() * 0.28663526131445843));
            }
        }
    }

    @Inject(method = "damage", at = @At(value = "RETURN"))
    private void applyDynamiteAndLightningEffects(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValue()) {
            LivingEntity target = (LivingEntity) (Object) this;
            Entity entity = source.getAttacker();

            if (entity != null && !entity.world.isClient) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;

                    boolean dynamite = TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.DYNAMITE_STICK);
                    boolean lightning = TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.LIGHTNING_BOTTLE);
                    boolean both = TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.ELECTRIFYING_DYNAMITE);

                    float explosionChance = both ? 0.10F : 0.06F;
                    float lightningChance = both ? 0.12F : 0.08F;

                    if (dynamite || both) {
                        if (player.getRandom().nextFloat() < explosionChance) {
                            player.world.createExplosion(
                                    player,
                                    source.setExplosive(),
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    1.5F + player.getRandom().nextFloat() * 1.5F,
                                    false,
                                    Explosion.DestructionType.NONE
                            );
                        }
                    }

                    if (lightning || both) {
                        if (player.getRandom().nextFloat() < lightningChance) {
                            LightningEntity lightningEntity = new LightningEntity(
                                    player.world,
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    true
                            );

                            ((ServerWorld) player.world).addLightning(lightningEntity);

                            doLightningDamage(lightningEntity, player);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
    private void applySilkyGloveEffect(CallbackInfoReturnable<Boolean> callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (!player.isSpectator()) {
                if (player.horizontalCollision) {
                    if (TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.SILKY_GLOVE)) {
                        callbackInfo.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
    private void applyShockwaveShieldEffect(LivingEntity attacker, CallbackInfo callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            if (TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.SHOCKWAVE_SHIELD)) {
                if (player.getRandom().nextFloat() < 0.75F) {
                    attacker.takeKnockback(0.5F * 1.5F, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());

                    callbackInfo.cancel();
                }
            }
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0)
    private float applyScorchedMaskEffect(float amount, DamageSource source, float amount2) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (source.isFire() && TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.SCORCHED_MASK)) {
                amount *= 0.25F;
            }
        }

        return amount;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerChunkManager;sendToOtherNearbyPlayers(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/Packet;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void listenForEquips(CallbackInfo callbackInfo,
                                 EquipmentSlot[] equipmentSlots,
                                 int numEquipment,
                                 int index,
                                 EquipmentSlot currentSlot,
                                 ItemStack prevItemStack,
                                 ItemStack currItemStack) {
        LivingEntity living = (LivingEntity) (Object) this;
        Optional<ItemStack> equip = Optional.empty(), unEquip = Optional.empty();

        if (!currItemStack.isEmpty()) {
            equip = Optional.of(currItemStack);
        }

        if (!prevItemStack.isEmpty()) {
            unEquip = Optional.of(prevItemStack);
        }

        if (equip.isPresent() || unEquip.isPresent()) {
            Set<ItemStack> listeners = new HashSet();

            for (EquipmentSlot slot : equipmentSlots) {
                ItemStack equippedStack = living.getEquippedStack(slot);

                if (equippedStack.getItem() instanceof EntityEquipmentListener) {
                    listeners.add(equippedStack);
                }
            }

            // The equipment found in the previous loop does not contain the previously equipped stack
            if (prevItemStack.getItem() instanceof EntityEquipmentListener) {
                listeners.add(prevItemStack);
            }

            for (ItemStack itemStack : listeners) {
                EntityEquipmentListener listener = (EntityEquipmentListener) itemStack.getItem();

                equip.ifPresent(equippedStack -> listener.onEquip(living, itemStack, equippedStack, currentSlot));
                unEquip.ifPresent(unEquippedStack -> listener.onUnEquip(living, itemStack, unEquippedStack, currentSlot));
            }
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void applyShulkerCharm(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        // Check if damage is applied
        if (callbackInfoReturnable.getReturnValue()) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;

            if (source.getAttacker() instanceof PlayerEntity) {
                if (TrinketUtil.hasAnyTrinkets((PlayerEntity) source.getAttacker(), AwakenItems.Trinkets.SHULKER_CHARM, AwakenItems.Trinkets.SHULKER_GLOVE)) {
                    if (source.isProjectile()) {
                        if (livingEntity.getRandom().nextFloat() < 0.15F) {
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3 * 20, 0));
                        }
                    }
                }
            }
        }
    }

    /**
     * We don't want the channeller of the lightning being hurt by it's own attack.
     */
    private static void doLightningDamage(LightningEntity lightning, LivingEntity channeller) {
        List<Entity> list = lightning.world.getEntities(
                lightning,
                new Box(lightning.getX() - 3.0D, lightning.getY() - 3.0D, lightning.getZ() - 3.0D, lightning.getX() + 3.0D, lightning.getY() + 6.0D + 3.0D, lightning.getZ() + 3.0D),
                entity -> entity.isAlive() && entity != channeller
        );

        for (Entity entity : list) {
            entity.onStruckByLightning(lightning);
        }

        if (channeller != null && channeller instanceof ServerPlayerEntity) {
            Criteria.CHANNELED_LIGHTNING.trigger((ServerPlayerEntity) channeller, list);
        }
    }
}
