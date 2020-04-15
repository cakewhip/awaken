package com.kqp.awaken.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Items;
import net.minecraft.screen.SmithingScreenHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * Used to remove netherite armor recipes.
 */
@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin {
    private static final ImmutableMap<Object, Object> TOOL_RECIPES;

    @Redirect(method = "updateResult", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/screen/SmithingScreenHandler;RECIPES:Ljava/util/Map;"))
    private final Map removeNetheriteArmorRecipes() {
        return TOOL_RECIPES;
    }

    static {
        TOOL_RECIPES = ImmutableMap.builder()
                .put(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD)
                .put(Items.DIAMOND_AXE, Items.NETHERITE_AXE)
                .put(Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE)
                .put(Items.DIAMOND_HOE, Items.NETHERITE_HOE)
                .put(Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL)
                .build();
    }
}
