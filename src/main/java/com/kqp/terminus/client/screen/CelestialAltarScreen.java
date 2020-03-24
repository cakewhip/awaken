package com.kqp.terminus.client.screen;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.kqp.terminus.client.container.CelestialAltarResultSlot;
import com.kqp.terminus.recipe.ComparableItemStack;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class CelestialAltarScreen extends ContainerScreen<CelestialAltarContainer> {
    private static final Identifier TEXTURE = new Identifier("terminus", "textures/gui/container/celestial_altar.png");

    public float scrollPosition = 0.0F;

    public CelestialAltarScreen(CelestialAltarContainer container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.containerHeight = 166;
        this.passEvents = true;

        syncScrollbar();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);
        this.drawMouseoverTooltip(mouseX, mouseY);
    }

    @Override
    public void renderTooltip(List<String> text, int x, int y) {
        if (this.focusedSlot instanceof CelestialAltarResultSlot && container.recipes != null) {
            int currentIndex = ((CelestialAltarResultSlot) this.focusedSlot).currentIndex;

            if(container.recipes.size() > currentIndex) {
                TerminusRecipe recipe = container.recipes.get(currentIndex);

                text.add("---");
                text.add("To Craft: ");
                for (ComparableItemStack cis : recipe.recipe.keySet()) {
                    System.out.println(cis.item.getName());
                    Text itemText = new LiteralText("").append(cis.item.getName());
                    text.add(recipe.recipe.get(cis) + " " + itemText.asFormattedString());
                }
            }
        }


        super.renderTooltip(text, x, y);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 2), 4210752);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.containerWidth) / 2;
        int j = (this.height - this.containerHeight) / 2;
        this.blit(i, j, 0, 0, this.containerWidth, 6 * 18 + 17);
        this.blit(i, j + 6 * 18 + 17, 0, 126, this.containerWidth, 96);

        this.blit(i + 156, j + 18 + (int)((float)(55 - 18) * this.scrollPosition), 232 + (this.hasScrollbar() ? 0 : 12), 0, 12, 15);
    }

    @Override public boolean mouseScrolled(double d, double e, double amount) {
        if (!this.hasScrollbar()) {
            return false;
        } else {
            int i = (((CelestialAltarContainer)this.container).outputs.size() + 8 - 1) / 8 - 3;
            this.scrollPosition = (float)((double)this.scrollPosition - amount / (double)i);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0F, 1.0F);
            syncScrollbar();
            return true;
        }
    }

    private boolean hasScrollbar() {
        return ((CelestialAltarContainer)this.container).shouldShowScrollbar();
    }

    public void syncScrollbar() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(scrollPosition);

        ClientSidePacketRegistry.INSTANCE.sendToServer(Terminus.TNetworking.SYNC_SCROLLBAR_ID, buf);
    }
}
