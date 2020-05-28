package com.kqp.awaken.mixin.awakening;

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
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ElderGuardianEntity;
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
 * Used to listen for deaths to trigger the awakening.
 */
@Mixin(LivingEntity.class)
public abstract class MobDeathListener {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        World world = entity.world;

        if (!world.isClient) {
            if (source.getAttacker() instanceof PlayerEntity) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());
                MinecraftServer server = world.getServer();

                if (entity instanceof EnderDragonEntity && !awakenLevelData.isPostDragon()) {
                    awakenLevelData.setPostDragon();
                    Broadcaster.broadcastMessage(server, "The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (entity instanceof WitherEntity && !awakenLevelData.isPostWither()) {
                    awakenLevelData.setPostWither();
                    Broadcaster.broadcastMessage(server, "Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (entity instanceof ElderGuardianEntity && !awakenLevelData.isPostElderGuardian()) {
                    awakenLevelData.setPostElderGuardian();
                    Broadcaster.broadcastMessage(server, "A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }
}
