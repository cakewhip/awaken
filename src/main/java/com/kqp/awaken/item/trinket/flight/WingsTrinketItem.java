package com.kqp.awaken.item.trinket.flight;

import com.kqp.awaken.client.model.WingsEntityModel;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WingsTrinketItem extends FlightTrinketItem {
    private static final WingsEntityModel<AbstractClientPlayerEntity> MODEL = new WingsEntityModel<>();

    public WingsTrinketItem(int durability, double maxFlySpeed, double flySpeed, int flyTime) {
        super(SlotGroups.CHEST, Slots.CAPE, durability, maxFlySpeed, flySpeed, flyTime, true);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(String slot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityModel<AbstractClientPlayerEntity> playerModel, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        ItemStack itemStack = TrinketsApi.getTrinketComponent(player).getStack(slot);
        Identifier texture = getTexture();

        matrices.push();
        matrices.translate(0.0D, 0.0D, 0.125D);

        playerModel.copyStateTo(MODEL);
        MODEL.setAngles(player, 0F, 0F, 0F, headYaw, headPitch);

        VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, MODEL.getLayer(texture), false, itemStack.hasEnchantmentGlint());
        MODEL.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTexture() {
        Identifier id = Registry.ITEM.getId(this);

        return new Identifier(id.getNamespace(), "textures/entity/wings/" + id.getPath() + ".png");
    }
}
