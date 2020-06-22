package com.kqp.awaken.mixin.fierymoon;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.mixin.accessor.MobEntityAccessor;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

/**
 * Used to buff the zombified piglin during fiery moon and make it insta-target players and villagers
 */
@Mixin(ZombifiedPiglinEntity.class)
public class FMZombifiedPiglinImplementer {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("zombified_piglin_fiery_moon_buffs")
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.25D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MAX_HEALTH, 1D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.025D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void buffZombifiedPiglin(EntityType<? extends ZoglinEntity> entityType, World world, CallbackInfo callbackInfo) {
        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                ZombifiedPiglinEntity zombiePiglin = (ZombifiedPiglinEntity) (Object) this;

                BUFF.applyToAndHeal(zombiePiglin);
            }
        }
    }

    @Inject(method = "initCustomGoals", at = @At("RETURN"))
    private void targetPlayersAndVillagers(CallbackInfo callbackInfo) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();

        targetSelector.add(1, new FollowTargetGoal(
                (ZombifiedPiglinEntity) (Object) this,
                PlayerEntity.class,
                false
        ));

        targetSelector.add(1, new FollowTargetGoal(
                (ZombifiedPiglinEntity) (Object) this,
                VillagerEntity.class,
                false
        ));
    }

    @Inject(method = "initEquipment", at = @At("RETURN"))
    private void addEquipment(LocalDifficulty difficulty, CallbackInfo callbackInfo) {
        ZombifiedPiglinEntity zombiePiglin = (ZombifiedPiglinEntity) (Object) this;
        World world = zombiePiglin.world;

        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                giveGoldEquipment(zombiePiglin);
            }
        }
    }

    private static void giveGoldEquipment(HostileEntity entity) {
        Random r = entity.getRandom();

        if (r.nextFloat() < 0.9F)
            entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
        if (r.nextFloat() < 0.75F)
            entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
        if (r.nextFloat() < 0.75F)
            entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));

        if (r.nextFloat() < 0.75F && !entity.getEquippedStack(EquipmentSlot.CHEST).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.CHEST), 25, false);
        if (r.nextFloat() < 0.5F && !entity.getEquippedStack(EquipmentSlot.LEGS).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.LEGS), 25, false);
        if (r.nextFloat() < 0.5F && !entity.getEquippedStack(EquipmentSlot.FEET).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.FEET), 25, false);
        if (r.nextFloat() < 0.75F && !entity.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.MAINHAND), 25, false);
    }
}
