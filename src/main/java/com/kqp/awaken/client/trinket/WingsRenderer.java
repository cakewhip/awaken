package com.kqp.awaken.client.trinket;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.List;

/**
 * Model class for the trinket.
 */
public class WingsRenderer implements TrinketRenderer {
    private static final float MAX_WING_FLUTTER_ANGLE = (float) (Math.PI / 4);

    private static final float ANIMATION_DURATION = 16F;
    private static final float HALF_POINT = 8F;

    private final ModelPart rightWing;
    private final ModelPart leftWing;

    private final List<ModelPart> partList;

    private float flutterTickTimer;

    public WingsRenderer() {
        leftWing = new ModelPart(64, 32, 0, 0);
        this.leftWing.addCuboid(-16.0F, 0.0F, 0.0F, 16.0F, 24.0F, 2.0F, 1.0F);

        this.rightWing = new ModelPart(64, 32, 0, 0);
        this.rightWing.mirror = true;
        this.rightWing.addCuboid(0.0F, 0.0F, 0.0F, 16.0F, 24.0F, 2.0F, 1.0F);

        partList = Arrays.asList(leftWing, rightWing);
    }


    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, PlayerEntityModel<AbstractClientPlayerEntity> playerModel, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        ItemStack itemStack = TrinketsApi.getTrinketComponent(player).getStack(slot);
        Identifier itemId = Registry.ITEM.getId(itemStack.getItem());
        Identifier texture = new Identifier(itemId.getNamespace(), "textures/entity/trinket/" + itemId.getPath() + ".png");

        matrixStack.push();
        matrixStack.translate(0.0D, 0.0D, 0.125D);

        setAngles(player);

        VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, RenderLayer.getEntityCutoutNoCull(texture), false, itemStack.hasEnchantments());
        partList.forEach(part -> {
            part.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
        });

        matrixStack.pop();
    }

    /**
     * Pitch is the angle between the trinket and the body
     * Roll is the angle between the trinket themselves
     * Yaw is the flutter effect, decreasing pushes the trinket away from the torso
     */
    private void setAngles(PlayerEntity player) {
        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        // Idle wing angles
        leftWing.pivotX = 1F;
        leftWing.pitch = (float) (Math.PI / -16);
        leftWing.roll = (float) (Math.PI / -16);
        leftWing.yaw = (float) (Math.PI - Math.PI / 8);

        if (player instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) player;

            // We do flutter animation if they're flying, floating or still mid-flutter animation
            if (flightProperties.isFlying() || flightProperties.isFloating() || flutterTickTimer != 0) {
                double velY = player.getVelocity().y;
                float animationSpeed = 0.75F;

                if (velY > 0) {
                    animationSpeed = (float) ((1F + velY) * 1F);
                }

                // If they're not flying still and the tick delta pushes the flutter timer over,
                // Reset to 0 so that it doesn't restart next loop
                if (!flightProperties.isFlying() && !flightProperties.isFloating() && flutterTickTimer + tickDelta > ANIMATION_DURATION) {
                    flutterTickTimer = 0;
                } else {
                    flutterTickTimer += tickDelta * animationSpeed;
                    flutterTickTimer %= ANIMATION_DURATION;
                }

                float flutterAnimMult = flutterTickTimer % HALF_POINT;

                if (flutterTickTimer > HALF_POINT) {
                    flutterAnimMult = HALF_POINT - flutterAnimMult;
                }

                flutterAnimMult /= HALF_POINT;

                leftWing.yaw -= flutterAnimMult * MAX_WING_FLUTTER_ANGLE;
            } else {
                flutterTickTimer = 0;
            }
        }

        // Set right wing's props to mirror the left wing
        this.rightWing.pivotX = -this.leftWing.pivotX;
        this.rightWing.yaw = -this.leftWing.yaw;
        this.rightWing.pivotY = this.leftWing.pivotY;
        this.rightWing.pitch = this.leftWing.pitch;
        this.rightWing.roll = -this.leftWing.roll;
    }
}
