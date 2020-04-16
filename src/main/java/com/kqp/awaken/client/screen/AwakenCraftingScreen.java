package com.kqp.awaken.client.screen;

import com.kqp.awaken.client.slot.AwakenCraftingResultSlot;
import com.kqp.awaken.client.slot.AwakenLookUpResultSlot;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.Reagent;
import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import com.kqp.awaken.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.List;

/**
 * Screen for Awaken's crafting system.
 */
public class AwakenCraftingScreen extends HandledScreen<AwakenCraftingScreenHandler> {
    public static final String TITLE_TRANSLATION_KEY = Util.createTranslationKey("gui", new Identifier(Awaken.MOD_ID, "awaken_crafting"));
    public static final String RECIPE_LOOK_UP_TRANSLATION_KEY = Util.createTranslationKey("gui", new Identifier(Awaken.MOD_ID, "awaken_crafting_recipe_look_up"));
    private static final Identifier TEXTURE = new Identifier(Awaken.MOD_ID, "textures/gui/crafting_2.png");

    /**
     * Position of the crafting outputs scroll bar.
     */
    public float outputScrollPosition = 0.0F;

    /**
     * Position of the recipe look-up scroll bar.
     */
    public float lookUpScrollPosition = 0.0F;

    public AwakenCraftingScreen(AwakenCraftingScreenHandler screenHandler, PlayerInventory playerInventory) {
        super(screenHandler, playerInventory, new TranslatableText(TITLE_TRANSLATION_KEY));
        this.backgroundWidth = 256;
        this.backgroundHeight = 166;
        this.passEvents = false;

        syncCraftingResultScrollbar();
    }

    /**
     * Overriden to render the tooltips for the tabs.
     *
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);
        this.drawMouseoverTooltip(mouseX, mouseY);

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

    /**
     * Overriden to add the reagents needed to craft the hovered slot.
     *
     * @param text
     * @param x
     * @param y
     */
    @Override
    public void renderTooltip(List<String> text, int x, int y) {
        if ((this.focusedSlot instanceof AwakenCraftingResultSlot && getScreenHandler().craftingResultRecipes != null)
                || (this.focusedSlot instanceof AwakenLookUpResultSlot && getScreenHandler().lookUpRecipes != null)) {
            int currentIndex;
            List<AwakenRecipe> recipes;

            if (this.focusedSlot instanceof AwakenCraftingResultSlot) {
                currentIndex = ((AwakenCraftingResultSlot) this.focusedSlot).currentIndex;
                recipes = getScreenHandler().craftingResultRecipes;
            } else {
                currentIndex = ((AwakenLookUpResultSlot) this.focusedSlot).currentIndex;
                recipes = getScreenHandler().lookUpRecipes;
            }

            if (currentIndex < recipes.size()) {
                AwakenRecipe recipe = recipes.get(currentIndex);
                String recipeType = AwakenRecipeManager.getRecipeTypeOf(recipe);


                text.add("---");

                if (!recipeType.equals(RecipeType.TWO_BY_TWO)) {
                    text.add(new TranslatableText("awaken.recipe_type.requires." + recipeType).asFormattedString());
                }

                text.add("To Craft: ");
                for (Reagent reagent : recipe.reagents.keySet()) {
                    String reagentLine = recipe.reagents.get(reagent) + " x " + reagent.toString();
                    List<String> split = this.textRenderer.wrapStringToWidthAsList(reagentLine, 126);

                    text.add(split.get(0));

                    // Used to bump up the wrapped lines
                    int offset = (recipe.reagents.get(reagent) + " x ").length();

                    if (split.size() > 1) {
                        for (int i = 1; i < split.size(); i++) {
                            // Cool trick to insert 'offset' amount of spaces
                            text.add(String.format("%" + offset + "s", "") + split.get(i));
                        }
                    }
                }
            }
        }

        super.renderTooltip(text, x, y);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.textRenderer.draw(this.title.asFormattedString(), 8.0F, 8.0F, 4210752);
        this.textRenderer.draw(new TranslatableText(RECIPE_LOOK_UP_TRANSLATION_KEY).asFormattedString(), 186.0F, 8.0F, 4210752);
        this.textRenderer.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float) (this.backgroundHeight - 96 + 4), 4210752);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);

        this.drawTexture(i, j - 28, 0, 166, 28, 32);

        this.drawTexture(i, j, 0, 0, 176, 166);

        this.drawTexture(i + 176 + 2, j, 178, 0, 78, 166);

        this.drawTexture(i + 156, j + 18 + (int) ((float) (52 - 15) * this.outputScrollPosition), 56 + (this.hasOutputsScrollbar() ? 0 : 12), 166, 12, 15);

        this.drawTexture(i + 242, j + 48 + (int) ((float) (106 - 11) * this.lookUpScrollPosition), 80 + (this.hasRecipeLookUpScrollbar() ? 0 : 6), 166, 6, 11);

        this.drawTexture(i + 29, j - 28, 28, 198, 28, 32);

        this.setZOffset(100);
        this.itemRenderer.zOffset = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemStack = new ItemStack(Blocks.CRAFTING_TABLE);
        this.itemRenderer.renderGuiItem(itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.zOffset = 0.0F;
        this.setZOffset(0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        double aX = mouseX - this.x;
        double aY = mouseY - this.y;

        if (aX >= 0 && aY >= 0 && aX < 176 && aY < this.backgroundHeight) {
            if (this.hasOutputsScrollbar()) {
                int i = (this.getScreenHandler().craftingResults.size() + 8 - 1) / 8 - 3;
                this.outputScrollPosition = (float) ((double) this.outputScrollPosition - amount / (double) i);
                this.outputScrollPosition = MathHelper.clamp(this.outputScrollPosition, 0.0F, 1.0F);
                syncCraftingResultScrollbar();

                return true;
            }
        } else if (aX >= 176 + 2 && aY >= 0 && aX < 176 + 2 + 78 && aY < this.backgroundHeight) {
            if (this.hasRecipeLookUpScrollbar()) {
                int i = (this.getScreenHandler().lookUpResults.size() + 3 - 1) / 3 - 6;
                this.lookUpScrollPosition = (float) ((double) this.lookUpScrollPosition - amount / (double) i);
                this.lookUpScrollPosition = MathHelper.clamp(this.lookUpScrollPosition, 0.0F, 1.0F);

                this.getScreenHandler().scrollLookUpResults(this.lookUpScrollPosition);
                syncLookUpResultScrollbar();

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double aX = mouseX - this.x;
            double aY = mouseY - this.y;

            if (aX > 0 && aX < 28) {
                if (aY > -32 && aY < 0) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeDouble(MouseUtil.getMouseX());
                    buf.writeDouble(MouseUtil.getMouseY());

                    ClientSidePacketRegistry.INSTANCE.sendToServer(AwakenNetworking.CLOSE_CRAFTING_C2S_ID, buf);
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean hasOutputsScrollbar() {
        return this.getScreenHandler().shouldShowOutputsScrollbar();
    }

    private boolean hasRecipeLookUpScrollbar() {
        return this.getScreenHandler().shouldShowLookUpResultsScrollbar();
    }

    /**
     * Sends the server the position of the scroll bar.
     */
    public void syncCraftingResultScrollbar() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(outputScrollPosition);

        ClientSidePacketRegistry.INSTANCE.sendToServer(AwakenNetworking.SYNC_CRAFTING_SCROLLBAR_ID, buf);
    }


    /**
     * Sends the server the position of the scroll bar.
     */
    public void syncLookUpResultScrollbar() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(lookUpScrollPosition);

        ClientSidePacketRegistry.INSTANCE.sendToServer(AwakenNetworking.SYNC_LOOK_UP_SCROLLBAR_ID, buf);
    }
}
