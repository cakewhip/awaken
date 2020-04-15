package com.kqp.awaken.item.sword;

import com.kqp.awaken.item.tool.AwakenSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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
public class EnderianCutlassItem extends AwakenSwordItem {
    public EnderianCutlassItem(ToolMaterial material) {
        super(material);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));

        player.getItemCooldownManager().set(this, 60);

        if (!world.isClient) {
            EnderPearlEntity pearl = new EnderPearlEntity(world, player);

            pearl.setItem(new ItemStack(Items.ENDER_PEARL));
            pearl.setProperties(player, player.pitch, player.yaw, 0.0F, 1.5F, 1.0F);

            world.spawnEntity(pearl);
        }

        if (!player.abilities.creativeMode) {
            itemStack.damage(5, player, (xyz) -> {});
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText("Right-click to throw an ender pearl"));
    }
}
