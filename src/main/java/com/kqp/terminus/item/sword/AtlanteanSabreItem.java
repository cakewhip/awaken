package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for the Atlantean Sabre.
 */
public class AtlanteanSabreItem extends TerminusSwordItem {
    public AtlanteanSabreItem() {
        super(TerminusToolMaterial.PHASE_0_SPECIAL);
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

    /**
     * Propels the player forward.
     *
     * @param player Player to propel forward
     */
    public static void propel(PlayerEntity player) {
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
        player.addVelocity(h, k, l);
        player.setPushCooldown(20);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new LiteralText("Right-click while swimming for a speed boost"));
    }
}
