package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusSwordItem;
import com.kqp.terminus.loot.TerminusRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for the Jang Katana.
 * TODO: make this a generic class that takes in a ToolMaterial and a list of status effects to apply
 */
public class JangKatanaItem extends TerminusSwordItem {
    public JangKatanaItem() {
        super(TerminusToolMaterial.JANG_KATANA);
    }

    @Override
    public boolean hasEnchantmentGlint(ItemStack stack) {
        return true;
    }

    /**
     * Applies effects for the Jang Katana.
     *
     * @param stack    ItemStack
     * @param world    World
     * @param entity   Entity in possession of the item
     * @param slot     Slot the item is in
     * @param selected If the item is being held
     */
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) entity;

            le.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20, 1));
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20, 1));
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 1));
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("When equipped:"));
        tooltip.add(new LiteralText("Regeneration II"));
        tooltip.add(new LiteralText("Speed II"));
        tooltip.add(new LiteralText("Resistance II"));
    }
}
