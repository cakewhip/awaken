package com.kqp.awaken.client.render.entity.feature;

import com.kqp.awaken.client.model.WingsEntityModel;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.item.trinket.FlightTrinketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WingsFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final WingsEntityModel<T> wingsModel = new WingsEntityModel<>();

    public WingsFeatureRenderer(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        ItemStack flightTrinketItemStack = ((PlayerFlightProperties) entity).getFlyingItemStack();

        if (flightTrinketItemStack != null) {
            FlightTrinketItem flightTrinketItem = (FlightTrinketItem) flightTrinketItemStack.getItem();
            if (flightTrinketItem.areWings) {
                Identifier texture = flightTrinketItem.getWingTexture();

                matrices.push();
                matrices.translate(0.0D, 0.0D, 0.125D);

                this.getContextModel().copyStateTo(this.wingsModel);
                this.wingsModel.setAngles(entity, limbDistance, limbDistance, customAngle, headYaw, headPitch);

                VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, this.wingsModel.getLayer(texture), false, flightTrinketItemStack.hasEnchantmentGlint());
                this.wingsModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

                matrices.pop();
            }
        }
    }
}
