package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AtlanteanSabreItem extends TerminusSwordItem {
    public AtlanteanSabreItem() {
        super(TerminusToolMaterial.PHASE_0_SWORD);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) user;

            if (player.isSubmergedInWater() && player.isSwimming()) {
                propel(player);
            }
        }
    }

    public void propel(PlayerEntity player) {
        float f = player.yaw;
        float g = player.pitch;
        float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
        float k = -MathHelper.sin(g * 0.017453292F);
        float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
        float m = MathHelper.sqrt(h * h + k * k + l * l);
        float n = 0.08F;
        h *= n / m;
        k *= n / m;
        l *= n / m;
        player.addVelocity((double)h, (double)k, (double)l);
        player.setPushCooldown(20);
    }
}
