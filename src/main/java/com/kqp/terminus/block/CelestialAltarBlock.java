package com.kqp.terminus.block;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.container.BlockContext;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CelestialAltarBlock extends Block {
    private static final Text CONTAINER_NAME = new TranslatableText("container.celestial_altar", new Object[0]);

    public CelestialAltarBlock() {
        super(FabricBlockSettings.of(Material.STONE)
                .strength(35.0F, 12.0F)
                .lightLevel(4)
                .build()
        );
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(Terminus.TContainers.CELESTIAL_ALTAR_ID, player, buf -> buf.writeBlockPos(pos));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public NameableContainerFactory createContainerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerFactory((i, playerInventory, playerEntity) -> {
            return new CelestialAltarContainer(i, playerInventory, BlockContext.create(world, pos));
        }, CONTAINER_NAME);
    }
}
