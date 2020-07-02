package com.kqp.awaken.mixin.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the wisdom attribute.
 */
@Mixin(ExperienceOrbEntity.class)
public class WisdomAttributeApplier {
    @Redirect(
            method = "onPlayerCollision",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V")
    )
    private void applyWisdomAttribute(PlayerEntity player, int experience) {
        EntityAttributeInstance wisdomAttrib = player.getAttributeInstance(AwakenEntityAttributes.WISDOM);

        experience = AttributeUtil.applyAttribute(wisdomAttrib, experience, player.getRandom());

        player.addExperience(experience);
    }
}
