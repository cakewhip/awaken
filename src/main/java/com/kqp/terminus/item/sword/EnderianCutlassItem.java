package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for the Enderian Cutlass.
 */
public class EnderianCutlassItem extends TerminusSwordItem {
    public EnderianCutlassItem() {
        super(TerminusToolMaterial.PHASE_0_SWORD);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity && remainingUseTicks <= 7180) {
            PlayerEntity player = (PlayerEntity) user;

            // Find what block the player is looking at
            BlockHitResult result = (BlockHitResult) player.rayTrace(128.0D, 0F, false);

            // Only teleport if a solid block is found
            if (world.getBlockState(result.getBlockPos()).getBlock() != Blocks.AIR) {
                BlockPos pos = result.getBlockPos().offset(result.getSide());

                for (int i = 0; i < 32; ++i) {
                    world.addParticle(ParticleTypes.PORTAL, player.getX(), player.getY() + world.random.nextDouble() * 2.0D, player.getZ(), world.random.nextGaussian(), 0.0D, world.random.nextGaussian());
                }

                player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.5F, 1.0F);

                player.requestTeleport(pos.getX(), pos.getY(), pos.getZ());
                player.fallDistance = 0.0F;
                player.damage(DamageSource.MAGIC, 4.0F);
            }
        }
    }

    /**
     * Overriden to tell the player when they can teleport.
     *
     * @param world             World
     * @param user              User of item
     * @param stack             ItemStack
     * @param remainingUseTicks Ticks remaining
     */
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;

            if (remainingUseTicks == 7180) {
                player.playSound(SoundEvents.BLOCK_COMPARATOR_CLICK, 0.25F, 1.0F);
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 7200;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        return TypedActionResult.consume(itemStack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText("Right-click to teleport"));
    }
}
