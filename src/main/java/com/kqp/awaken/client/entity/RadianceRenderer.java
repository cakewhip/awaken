package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.RadianceModel;
import com.kqp.awaken.entity.mob.RadianceEntity;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class RadianceRenderer extends MobEntityRenderer<RadianceEntity, RadianceModel<RadianceEntity>> {
    private static final Identifier INVULNERABLE_TEXTURE = Awaken.id("textures/entity/radiance/invulnerable.png");
    private static final Identifier TEXTURE = Awaken.id("textures/entity/radiance/radiance.png");

    public RadianceRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new RadianceModel<>(0.0F), 1.0F);
    }

    @Override
    protected int getBlockLight(RadianceEntity RadianceEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(RadianceEntity RadianceEntity) {
        int i = RadianceEntity.getInvulnerableTimer();
        return i > 0 && (i > 80 || i / 5 % 2 != 1) ? INVULNERABLE_TEXTURE : TEXTURE;
    }

    @Override
    protected void scale(RadianceEntity RadianceEntity, MatrixStack matrixStack, float f) {
        float g = 2.0F;
        int i = RadianceEntity.getInvulnerableTimer();
        if (i > 0) {
            g -= ((float) i - f) / 220.0F * 0.5F;
        }

        matrixStack.scale(g, g, g);
    }
}
