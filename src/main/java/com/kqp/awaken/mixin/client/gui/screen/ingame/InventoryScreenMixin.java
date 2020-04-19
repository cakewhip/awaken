package com.kqp.awaken.mixin.client.gui.screen.ingame;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.init.client.AwakenClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to add the navigation tabs to reach the Awaken crafting screen.
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Awaken.MOD_ID, "textures/gui/crafting_2.png");

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void catchMouseClickEvent(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (button == 0) {
            double aX = mouseX - this.x;
            double aY = mouseY - this.y;

            if (aX > 29 && aX < 57) {
                if (aY > -32 && aY < 0) {
                    AwakenClient.triggerOpenCraftingMenu();

                    callbackInfo.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "drawBackground", at = @At("HEAD"))
    public void drawCraftingTab(float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
        this.drawTexture(this.x + 29, this.y - 28, 28, 166, 28, 32);

        this.setZOffset(100);
        this.itemRenderer.zOffset = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemStack = new ItemStack(Blocks.CRAFTING_TABLE);
        this.itemRenderer.renderGuiItem(itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.zOffset = 0.0F;
        this.setZOffset(0);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    public void drawPlayerTab(float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
        this.drawTexture(this.x, this.y - 28, 0, 198, 28, 32);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void drawTooltips(int mouseX, int mouseY, float delta, CallbackInfo callbackInfo) {
        double aX = mouseX - this.x;
        double aY = mouseY - this.y;

        if (aY > -24 && aY < 0) {
            if (aX > 0 && aX < 28) {
                this.renderTooltip("Player", mouseX, mouseY);
            } else if (aX > 29 && aX < 57) {
                this.renderTooltip("Crafting", mouseX, mouseY);
            }
        }
    }
}
