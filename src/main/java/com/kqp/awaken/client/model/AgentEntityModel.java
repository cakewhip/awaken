package com.kqp.awaken.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.MobEntity;

@Environment(EnvType.CLIENT)
public class AgentEntityModel<T extends MobEntity> extends BipedEntityModel<T> {
    public AgentEntityModel() {
        super(0F, 0F, 64, 64);

        this.head = new ModelPart(this, 0, 0);
        this.head.addCuboid(0F, 0F, 0F, 0F, 0F, 0F);
        this.head.setPivot(0.0F, 0.0F, 0.0F);
        this.helmet = new ModelPart(this, 0, 0);
        this.helmet.addCuboid(0F, 0F, 0F, 0F, 0F, 0F);
        this.helmet.setPivot(0.0F, 0.0F, 0.0F);

        this.torso = new ModelPart(this, 0, 0);
        this.torso.addCuboid(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
        this.torso.setPivot(0.0F, 13.0F, 0.0F);

        this.rightArm = new ModelPart(this, 40, 8);
        this.rightArm.addCuboid(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
        this.rightArm.setPivot(-5.0F, 13.0F, 0.0F);

        this.leftArm = new ModelPart(this, 40, 8);
        this.leftArm.mirror = true;
        this.leftArm.addCuboid(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
        this.leftArm.setPivot(5.0F, 13.0F, 0.0F);

        this.rightLeg = new ModelPart(this, 40, 0);
        this.rightLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
        this.rightLeg.setPivot(-3.0F, 18.0F, 0.0F);

        this.leftLeg = new ModelPart(this, 40, 0);
        this.leftLeg.mirror = true;
        this.leftLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);
        this.leftLeg.setPivot(3.0F, 18.0F, 0.0F);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);

        this.torso.pivotX = 0.0F;
        this.torso.pivotY = 13.0F;
        this.torso.pivotZ = 0.0F;

        this.rightArm.pivotX = -5.0F;
        this.rightArm.pivotY = 13.0F;
        this.rightArm.pivotZ = 0.0F;

        this.leftArm.pivotX = 5.0F;
        this.leftArm.pivotY = 13.0F;
        this.leftArm.pivotZ = 0.0F;

        this.rightLeg.pivotX = -3.0F;
        this.rightLeg.pivotY = 18.0F;
        this.rightLeg.pivotZ = 0.1F;

        this.leftLeg.pivotX = 3.0F;
        this.leftLeg.pivotY = 18.0F;
        this.leftLeg.pivotZ = 0.1F;
    }
}
