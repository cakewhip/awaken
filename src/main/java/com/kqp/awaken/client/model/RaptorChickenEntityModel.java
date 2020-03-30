package com.kqp.awaken.client.model;

import com.google.common.collect.ImmutableList;
import com.kqp.awaken.entity.RaptorChickenEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

/**
 * Broken raptor chicken model.
 * Rest in peace.
 */
public class RaptorChickenEntityModel<T extends RaptorChickenEntity> extends AnimalModel<T> {
    private final ModelPart head;
    private final ModelPart torso;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart leftLower;
    private final ModelPart rightLower;

    public RaptorChickenEntityModel() {
        textureWidth = 64;
        textureHeight = 64;

        head = new ModelPart(this);
        head.setPivot(-8.0F, 16.0F, 8.0F);
        head.addCuboid(40, 52, 5.0F, -9.0F, -16.0F, 6, 6, 6, 0.0F);
        head.addCuboid(48, 48, 5.0F, -7.0F, -18.0F, 6, 2, 2, 0.0F);
        head.addCuboid(50, 45, 6.0F, -5.0F, -18.0F, 4, 1, 2, 0.0F);

        torso = new ModelPart(this);
        torso.setPivot(-8.0F, 16.0F, 8.0F);
        torso.addCuboid(0, 55, 3.0F, -5.0F, -3.0F, 10, 8, 1, 0.0F);
        torso.addCuboid(0, 49, 3.0F, -5.0F, -14.0F, 10, 5, 1, 0.0F);
        torso.addCuboid(0, 0, 3.0F, -5.0F, -13.0F, 10, 10, 10, 0.0F);

        leftWing = new ModelPart(this);
        leftWing.setPivot(-8.0F, 16.0F, 8.0F);
        leftWing.addCuboid(0, 20, 13.0F, -5.0F, -12.0F, 2, 6, 8, 0.0F);
        leftWing.addCuboid(12, 20, 13.0F, 1.0F, -10.0F, 2, 2, 6, 0.0F);
        leftWing.addCuboid(21, 20, 13.0F, 3.0F, -8.0F, 1, 2, 4, 0.0F);

        rightWing = new ModelPart(this);
        rightWing.setPivot(-8.0F, 16.0F, 8.0F);
        rightWing.addCuboid(0, 20, 1.0F, -5.0F, -12.0F, 2, 6, 8, 0.0F);
        rightWing.addCuboid(12, 20, 1.0F, 1.0F, -10.0F, 2, 2, 6, 0.0F);
        rightWing.addCuboid(22, 20, 2.0F, 3.0F, -8.0F, 1, 2, 4, 0.0F);

        leftLower = new ModelPart(this);
        leftLower.setPivot(-8.0F, 16.0F, 8.0F);
        leftLower.addCuboid(30, 0, 10.0F, 5.0F, -8.0F, 1, 3, 1, 0.0F);
        leftLower.addCuboid(30, 7, 9.0F, 7.0F, -10.0F, 3, 1, 2, 0.0F);

        rightLower = new ModelPart(this);
        rightLower.setPivot(-8.0F, 16.0F, 8.0F);
        rightLower.addCuboid(30, 0, 5.0F, 5.0F, -8.0F, 1, 3, 1, 0.0F);
        rightLower.addCuboid(30, 7, 4.0F, 7.0F, -10.0F, 3, 1, 2, 0.0F);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.torso.pitch = 1.5707964F;
        this.rightLower.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftLower.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
        this.rightWing.roll = customAngle;
        this.leftWing.roll = -customAngle;
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.torso, this.rightWing, this.leftWing, this.rightLower, this.leftLower);
    }
}