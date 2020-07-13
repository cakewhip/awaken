package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(PlayerEntity.class)
public class BlueprintAbilityApplier {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("blueprint_buff")
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.075);

    // TODO: FIX
    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void checkBlueprint(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient) {
            if (AwakenAbilities.BLUEPRINT.get(player).flag) {
                ServerWorld serverWorld = (ServerWorld) player.world;

                ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(player.getBlockPos());
                AtomicInteger atomicInteger = new AtomicInteger(0);

                Registry.STRUCTURE_FEATURE.forEach(structureFeature -> {
                    serverWorld.getStructureAccessor().getStructuresWithChildren(chunkSectionPos, structureFeature)
                            .forEach(start -> atomicInteger.incrementAndGet());
                });

                if (atomicInteger.get() > 0) {
                    BUFF.applyTo(player);
                } else {
                    BUFF.removeFrom(player);
                }
            }
        }
    }
}
