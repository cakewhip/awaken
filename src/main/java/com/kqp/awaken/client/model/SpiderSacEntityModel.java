package com.kqp.awaken.client.model;

import com.kqp.awaken.entity.SpiderSacEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpiderSacEntityModel<T extends SpiderSacEntity> extends EntityModel<T> {
    private final ModelPart sacBottom, sacTop;

    public SpiderSacEntityModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.sacBottom = new ModelPart(this, 0, 0);
        this.sacBottom.addCuboid(-7F, 10F, -7F, 14F, 14F, 14F);
        this.sacBottom.setPivot(0F, 0F, 0F);

        this.sacTop = new ModelPart(this, 0, 28);
        this.sacTop.addCuboid(-5F, 6F, -5F, 10F, 4F, 10F);
        this.sacTop.setPivot(0F, 0F, 0F);

    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.sacBottom.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        this.sacTop.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }
}
