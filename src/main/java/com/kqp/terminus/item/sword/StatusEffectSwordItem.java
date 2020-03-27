package com.kqp.terminus.item.sword;

import com.kqp.terminus.item.tool.TerminusSwordItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class StatusEffectSwordItem extends TerminusSwordItem {
    public StatusEffect effect;
    public int duration, amplifier;

    public StatusEffectSwordItem(ToolMaterial material, StatusEffect effect, int duration, int amplifier) {
        super(material);

        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));

        return true;
    }
}
