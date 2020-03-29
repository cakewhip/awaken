package com.kqp.terminus.client.entity;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.entity.RaptorChickenEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * Renderer for the raptor chicken.
 */
public class RaptorChickenRenderer<T extends MobEntity> extends MobEntityRenderer<T, ChickenEntityModel<T>> {
    public RaptorChickenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new ChickenEntityModel<>(), 0.45F);
    }

    @Override
    public Identifier getTexture(T entity) {
        return new Identifier(Terminus.MOD_ID, "textures/entity/raptor_chicken.png");
    }

    /**
     * This method is actually overriden using a mixin.
     * See {@link net.minecraft.client.render.entity.LivingEntityRenderer}
     *
     * @param chickenEntity Raptor chicken entity
     * @param f tickDelta
     * @return something???
     */
    public float getAnimationProgress(RaptorChickenEntity chickenEntity, float f) {
        float g = MathHelper.lerp(f, chickenEntity.field_6736, chickenEntity.field_6741);
        float h = MathHelper.lerp(f, chickenEntity.field_6738, chickenEntity.field_6743);
        return (MathHelper.sin(g) + 1.0F) * h;
    }

    @Override
    public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.scale(1.5F, 1.65F, 1.5F);

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
