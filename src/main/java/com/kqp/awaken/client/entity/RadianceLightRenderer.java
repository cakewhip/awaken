package com.kqp.awaken.client.entity;

import com.kqp.awaken.entity.projectile.RadianceLightEntity;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RadianceLightRenderer extends EntityRenderer<RadianceLightEntity> {
    private static final Identifier TEXTURE = Awaken.id("textures/entity/radiance/radiance.png");

    private final SkullEntityModel model = new SkullEntityModel();

    public RadianceLightRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(RadianceLightEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vcProv, int i) {
        matrices.push();
        matrices.scale(-1F, -1F, 1F);

        float yaw = MathHelper.lerpAngle(entity.prevYaw, entity.yaw, g);
        float pitch = MathHelper.lerpAngle(entity.prevPitch, entity.pitch, g);

        VertexConsumer vc = vcProv.getBuffer(model.getLayer(getTexture(entity)));
        model.method_2821(0F, yaw, pitch);
        model.render(matrices, vc, i, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);

        matrices.pop();
        super.render(entity, f, g, matrices, vcProv, i);
    }

    @Override
    protected int getBlockLight(RadianceLightEntity witherSkullEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(RadianceLightEntity entity) {
        return TEXTURE;
    }
}
