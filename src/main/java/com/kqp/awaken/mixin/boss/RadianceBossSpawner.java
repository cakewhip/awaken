package com.kqp.awaken.mixin.boss;

import com.kqp.awaken.entity.mob.RadianceEntity;
import com.kqp.awaken.init.AwakenEntities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public class RadianceBossSpawner {
    private static BlockPattern radianceBossPattern;

    @Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
    private void searchForPattern(BlockHitResult hitResult, CallbackInfo callbackInfo) {
        PotionEntity potionEntity = (PotionEntity) (Object) this;
        World world = potionEntity.world;

        if (!world.isClient) {
            ItemStack itemStack = potionEntity.getStack();

            if (itemStack.getItem() instanceof SplashPotionItem) {
                Potion potion = PotionUtil.getPotion(itemStack);
                List<StatusEffectInstance> list = PotionUtil.getPotionEffects(itemStack);

                boolean hasRegen = false;

                for (StatusEffectInstance statusEffectInstance : list) {
                    if (statusEffectInstance.getEffectType() == StatusEffects.REGENERATION) {
                        if (statusEffectInstance.getAmplifier() > 0) {
                            hasRegen = true;
                            break;
                        }
                    }
                }

                if (hasRegen) {
                    BlockPattern blockPattern = getRadianceBossPattern();
                    BlockPattern.Result result = blockPattern.searchAround(world, hitResult.getBlockPos());

                    if (result != null) {
                        for (int i = 0; i < blockPattern.getWidth(); i++) {
                            for (int j = 0; j < blockPattern.getHeight(); j++) {
                                CachedBlockPosition blockPos = result.translate(i, j, 0);
                                world.setBlockState(blockPos.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
                                world.syncWorldEvent(2001, blockPos.getBlockPos(), Block.getRawIdFromState(blockPos.getBlockState()));
                            }
                        }

                        RadianceEntity radiance = AwakenEntities.RADIANCE.create(world);
                        radiance.refreshPositionAndAngles(result.translate(1, 2, 0).getBlockPos(), 0F, 0F);
                        radiance.onSpawn();
                        world.spawnEntity(radiance);

                        for (int i = 0; i < blockPattern.getWidth(); i++) {
                            for (int j = 0; j < blockPattern.getHeight(); j++) {
                                world.updateNeighbors(result.translate(i, j, 0).getBlockPos(), Blocks.AIR);
                            }
                        }

                        callbackInfo.cancel();
                    }
                }
            }
        }
    }

    private static BlockPattern getRadianceBossPattern() {
        if (radianceBossPattern == null) {
            radianceBossPattern = BlockPatternBuilder.start().aisle("^^^", "#*#", "~#~")
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SOUL_SAND)))
                    .where('*', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.GLOWSTONE)))
                    .where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL))))
                    .where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR)))
                    .build();
        }

        return radianceBossPattern;
    }
}
