package com.kqp.awaken.item.bow;

import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating bows that apply a status effect to hit entities.
 */
public class StatusEffectBowItem extends AwakenBowItem {
    public StatusEffect effect;
    public int duration, amplifier;

    public StatusEffectBowItem(double baseDamage, boolean infinity, StatusEffect effect, int duration, int amplifier) {
        super(baseDamage, infinity);

        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public PersistentProjectileEntity createProjectileEntity(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ArrowEntity arrowEntity = new ArrowEntity(world, user) {
            @Override
            protected void onEntityHit(EntityHitResult hitResult) {
                super.onEntityHit(hitResult);

                if (hitResult.getEntity() instanceof LivingEntity) {
                    ((LivingEntity) hitResult.getEntity()).addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
                }
            }
        };

        arrowEntity.initFromStack(stack);

        return arrowEntity;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText(String.format(
                "Applies %s %s for %.2f seconds",
                effect.getName().asFormattedString(),
                I18n.translate("enchantment.level." + (amplifier + 1)),
                (duration / 20F)
        )));
    }
}
