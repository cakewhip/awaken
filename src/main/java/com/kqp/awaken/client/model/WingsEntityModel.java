package com.kqp.awaken.client.model;

import com.google.common.collect.ImmutableList;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * Model class for the  wings.
 *
 * @param <T> something lol
 */
public class WingsEntityModel<T extends LivingEntity> extends AnimalModel<T> {
    private static final float MAX_WING_FLUTTER_ANGLE = (float) (Math.PI / 4);

    private static final float ANIMATION_DURATION = 16F;
    private static final float HALF_POINT = 8F;

    /**
     * Because speed and velocity are inversely related,
     * we need to subtract the velocity from some constant.
     * It makes sense for this constant to be the max flying speed.
     * Which is declared here.
     */
    private static final float MAX_FLYING_SPEED = 2F;

    private final ModelPart rightWing;
    private final ModelPart leftWing;

    private float flutterTickTimer;

    public WingsEntityModel() {
        leftWing = new ModelPart(this, 0, 0);
        this.leftWing.addCuboid(-16.0F, 0.0F, 0.0F, 16.0F, 24.0F, 2.0F, 1.0F);

        this.rightWing = new ModelPart(this, 0, 0);
        this.rightWing.mirror = true;
        this.rightWing.addCuboid(0.0F, 0.0F, 0.0F, 16.0F, 24.0F, 2.0F, 1.0F);
    }

    /**
     * Pitch is the angle between the wings and the body
     * Roll is the angle between the wings themselves
     * Yaw is the flutter effect, decreasing pushes the wings away from the torso
     *
     * @param entity
     * @param limbAngle
     * @param limbDistance
     * @param customAngle
     * @param headYaw
     * @param headPitch
     */
    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        float tickDelta = MinecraftClient.getInstance().getTickDelta();

        // Idle wing angles
        leftWing.pivotX = 1F;
        leftWing.pitch = (float) (Math.PI / -16);
        leftWing.roll = (float) (Math.PI / -16);
        leftWing.yaw = (float) (Math.PI - Math.PI / 8);

        if (entity instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) entity;

            // We do flutter animation if they're flying, floating or still mid-flutter animation
            if (flightProperties.isFlying() || flightProperties.isFloating() || flutterTickTimer != 0) {
                double velY = entity.getVelocity().y;
                float animationSpeed = 0.5F;

                if (velY > 0) {
                    animationSpeed = (float) (MAX_FLYING_SPEED - velY) / 2F * 1.15F;
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

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.leftWing, this.rightWing);
    }
}
