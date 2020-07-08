package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class BiomeRingAbilityApplier {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("biome_ring_buff")
            .addEntityAttributeMultiplier(AwakenEntityAttributes.MELEE_DAMAGE, 0.02)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.RANGED_DAMAGE, 0.02)
            .addEntityAttributeAddition(AwakenEntityAttributes.CRIT_CHANCE, 0.03)
            .addEntityAttributeMultiplier(AwakenEntityAttributes.WISDOM, 0.05)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.01)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_ARMOR, 2);

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void checkBiomeRings(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        boolean appliedBuff = false;

        if (AwakenAbilities.WORLD_RING.get(player).flag) {
            appliedBuff = true;
            BUFF.applyTo(player);

        } else {
            Biome currentBiome = player.world.getBiome(player.getBlockPos());

            for (AwakenAbilities.BiomeRingAbility biomeRingAbility : AwakenAbilities.BIOME_RINGS) {
                if (biomeRingAbility.get(player).flag) {
                    if (biomeRingAbility.biomes.contains(currentBiome)) {
                        appliedBuff = true;
                        BUFF.applyTo(player);
                        break;
                    }
                }
            }
        }

        if (!appliedBuff) {
            BUFF.removeFrom(player);
        }
    }
}
